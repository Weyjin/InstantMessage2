package com.instant.message.shiro

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextClosedEvent

class CloseListener : ApplicationListener<ContextClosedEvent> {
    private val logger = LoggerFactory.getLogger(javaClass)
    override fun onApplicationEvent(event: ContextClosedEvent) {
        logger.info("程序停止")

    }
}