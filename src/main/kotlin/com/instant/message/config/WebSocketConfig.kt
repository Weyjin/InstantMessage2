package com.instant.message.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.server.standard.ServerEndpointExporter

@Configuration
open class WebSocketConfig {
    @Bean
    open fun serverEndpointExporter(): ServerEndpointExporter {
        return ServerEndpointExporter()
    }
}