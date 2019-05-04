package com.instant.message.shiro

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent


class StartListener : ApplicationListener<ContextRefreshedEvent> {
    private val logger = LoggerFactory.getLogger(javaClass)
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        logger.info("程序启动")
    }

}
