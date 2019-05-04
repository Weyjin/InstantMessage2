package com.instant.message.aop

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.io.IOException

@Component
@Aspect
@Order(1)
class ExceptionAspect {

    @Pointcut(value = "execution(public * com.instant.message.*.*(..))")
    fun webException() {}

    @AfterThrowing(value = "webException()", throwing = "e")
    fun afterThrowing(joinPoint: JoinPoint, e: Throwable) {

        val className = joinPoint.target.javaClass.name
        val methodName = joinPoint.signature.name
        val args = joinPoint.args
        //开始打log
        println("异常:" + e.message)
        println("异常所在类：$className")
        println("异常所在方法：$methodName")
        println("异常中的参数：")
        println(methodName)
        for (i in args.indices) {
            println(args[i].toString())
        }
        redirect()

    }

    private fun redirect() {
        val response = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).response
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        if (request.session.getAttribute("user") == null) {
            try {
                response?.sendRedirect("/login")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}