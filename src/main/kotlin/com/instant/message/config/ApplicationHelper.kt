package com.instant.message.config

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class ApplicationHelper : ApplicationContextAware {

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        ApplicationHelper.applicationContext = applicationContext
    }

    companion object {
        private var applicationContext: ApplicationContext? = null

        private fun getApplicationContext(): ApplicationContext? {
            return applicationContext
        }
        fun getBean(beanName: String): Any {
            return getApplicationContext()!!.getBean(beanName)
        }
    }


}