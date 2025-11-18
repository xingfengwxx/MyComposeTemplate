import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.hjq.permissions.permission.PermissionLists
import com.hjq.permissions.permission.base.IPermission
import com.wangxingxing.mycomposeapp.aop.permission.PermissionRequest
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import kotlin.reflect.KClass

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

        // 将 KClass<out IPermission> 转换为 IPermission 实例
        val permissionInstances = permissionRequest.permissions.map {
            it.java.newInstance()
        }.toTypedArray()

        XXPermissions.with(context)
            // 申请多个权限
            .permissions(permissionInstances)
            // 设置不触发错误检测机制（局部设置）
            //.unchecked()
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
                    // ......
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


