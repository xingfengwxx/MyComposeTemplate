import android.app.Activity
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.hjq.permissions.permission.base.IPermission
import com.wangxingxing.mycomposeapp.aop.permission.Permissions
import com.wangxingxing.mycomposeapp.utils.PermissionHelper
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

@Aspect
class PermissionsAspect {

    // 尝试多种切点表达式以确保匹配
    @Pointcut("execution(@com.wangxingxing.mycomposeapp.aop.permission.Permissions * *(..))")
    fun requestPermissionByExecution() {
    }
    
    @Pointcut("call(@com.wangxingxing.mycomposeapp.aop.permission.Permissions * *(..))")
    fun requestPermissionByCall() {
    }
    
    @Pointcut("execution(* *(..)) && @annotation(com.wangxingxing.mycomposeapp.aop.permission.Permissions)")
    fun requestPermissionByAnnotation() {
    }
    
    // 组合所有可能的切点
    @Pointcut("requestPermissionByExecution() || requestPermissionByCall() || requestPermissionByAnnotation()")
    fun requestPermission() {
    }

    @Around("requestPermission() && @annotation(permissions)")
    @Throws(Throwable::class)
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint, permissions: Permissions) {
        // 添加调试日志，确认切面是否被调用
        LogUtils.d("PermissionsAspect: aroundJoinPoint called for method: ${joinPoint.signature.name}")
        LogUtils.d("PermissionsAspect: target object type: ${joinPoint.`this`.javaClass.simpleName}")
        
        val context = getContext(joinPoint.`this`)
        if (context == null) {
            LogUtils.e("PermissionsAspect: Unable to get context from join point. Target: ${joinPoint.`this`.javaClass.name}")
            // 即使没有 context，也尝试执行原方法，避免完全阻塞
            try {
                joinPoint.proceed()
            } catch (throwable: Throwable) {
                LogUtils.e("PermissionsAspect: Error executing method without context", throwable)
                throwable.printStackTrace()
            }
            return
        }
        
        LogUtils.d("PermissionsAspect: Context obtained: ${context.javaClass.simpleName}")

        // 1. 直接使用 .permission(String...) 方法，这是最简洁的方式
        val permissionsToRequest = permissions.value
        if (permissionsToRequest.isEmpty()) {
            // 如果没有请求任何权限，直接执行原方法
            try {
                joinPoint.proceed()
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
            return
        }

        val requestPermissionLists = PermissionHelper.allPermissions.filter { it.permissionName in permissionsToRequest }
        LogUtils.i("PermissionsAspect: Requesting permissions: $requestPermissionLists")
        LogUtils.d("PermissionsAspect: Permissions to request: ${permissionsToRequest.joinToString()}")

        XXPermissions.with(context)
            // 2. 直接传入字符串数组，XXPermissions 内部会自动处理
            .permissions(requestPermissionLists)
            .request(object : OnPermissionCallback {

                override fun onResult(grantedList: MutableList<IPermission>, deniedList: MutableList<IPermission>) {
                    val allGranted = deniedList.isEmpty()
                    if (!allGranted) {
                        // 判断请求失败的权限是否被用户勾选了不再询问的选项
                        val doNotAskAgain = XXPermissions.isDoNotAskAgainPermissions(context, deniedList)
                        // 在这里处理权限请求失败的逻辑
                        // ......
                        if (doNotAskAgain) {
                            ToastUtils.showShort("永久拒绝授权，请手动授予权限")
                        }
                        ToastUtils.showShort("部分权限未授予")
                        return
                    }
                    // 在这里处理权限请求成功的逻辑
                    try {
                        // 所有权限都授予了，执行原方法
                        joinPoint.proceed()
                    } catch (throwable: Throwable) {
                        throwable.printStackTrace()
                    }
                }
            })
    }

    private fun getContext(obj: Any): Activity? {
        val context = when (obj) {
            is Activity -> {
                LogUtils.d("PermissionsAspect: Context from Activity")
                obj
            }
            is Fragment -> {
                LogUtils.d("PermissionsAspect: Context from Fragment")
                obj.activity
            }
            else -> {
                // 对于 ViewModel 或其他类型，尝试获取当前 Activity
                LogUtils.d("PermissionsAspect: Attempting to get top activity for type: ${obj.javaClass.simpleName}")
                val topActivity = ActivityUtils.getTopActivity()
                if (topActivity != null) {
                    LogUtils.d("PermissionsAspect: Top activity obtained: ${topActivity.javaClass.simpleName}")
                } else {
                    LogUtils.e("PermissionsAspect: Top activity is null!")
                }
                topActivity
            }
        }
        return context
    }
}


