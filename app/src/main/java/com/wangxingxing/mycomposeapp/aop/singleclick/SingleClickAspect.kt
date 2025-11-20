package com.wangxingxing.mycomposeapp.aop.singleclick

import com.blankj.utilcode.util.LogUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import java.util.Calendar

@Aspect
class SingleClickAspect {

    companion object {
        private const val TAG = "SingleClickAspect"
        private var lastClickTime: Long = 0
    }

    @Pointcut("execution(@com.wangxingxing.mycomposeapp.aop.singleclick.SingleClick * *(..)) && @annotation(singleClick)")
    fun requestSingleClick(singleClick: SingleClick) {}

    @Around("requestSingleClick(singleClick)")
    @Throws(Throwable::class)
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint, singleClick: SingleClick) {
        val currentTime = Calendar.getInstance().timeInMillis
        if (currentTime - lastClickTime > singleClick.value) {
            lastClickTime = currentTime
            LogUtils.i(TAG, "正常执行点击事件")
            joinPoint.proceed() // 正常执行
        } else {
            LogUtils.i(TAG, "重复点击，事件被拦截")
            // 拦截掉快速点击的事件
        }
    }
}
