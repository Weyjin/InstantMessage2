package com.instant.message

import com.instant.message.shiro.CloseListener
import com.instant.message.shiro.StartListener
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@MapperScan(basePackages = ["com.instant.message.dao"])
@ComponentScan(value = ["com.instant.message.*"])
open class MessageApplication

fun main(args: Array<String>) {
    val sa = SpringApplication(MessageApplication::class.java)
    sa.addListeners(StartListener())
    sa.addListeners(CloseListener())
    sa.run(*args)
}
