import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.hjq.permissions.permission.base.IPermission
import com.wangxingxing.mycomposeapp.aop.permission.PermissionRequest
import com.wangxingxing.mycomposeapp.utils.PermissionHelper
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

@Aspect
class PermissionAspect {

    companion object {
        private const val TAG = "PermissionAspect"
    }

    @Pointcut("execution(@com.wangxingxing.mycomposeapp.aop.permission.PermissionRequest * *(..)) && @annotation(permissionRequest)")
    fun requestPermission(permissionRequest: PermissionRequest) {
    }

    @Around("requestPermission(permissionRequest)")
    @Throws(Throwable::class)
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint, permissionRequest: PermissionRequest) {
        val context = getContext(joinPoint.`this`)
        if (context == null) {
            Log.e(TAG, "Unable to get context from join point")
            return
        }

        // 1. 直接使用 .permission(String...) 方法，这是最简洁的方式
        val permissionsToRequest = permissionRequest.permissions
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
        LogUtils.i("requestPermissionLists: $requestPermissionLists")

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
        return when (obj) {
            is Activity -> obj
            is Fragment -> obj.activity
            else -> ActivityUtils.getTopActivity()
        }
    }
}


