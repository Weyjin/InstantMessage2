package com.instant.message.shiro

import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor
import org.apache.shiro.spring.web.ShiroFilterFactoryBean
import org.apache.shiro.web.mgt.DefaultWebSecurityManager
import org.apache.shiro.web.mgt.WebSecurityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.LinkedHashMap

@Configuration
open class ShiroConfiguration {

    //将自己的验证方式加入容器
    @Bean
    open fun myShiroRealm(): MyShiroRealm {
        return MyShiroRealm()
    }

    //权限管理，配置主要是Realm的管理认证
    @Bean
    open fun securityManager(): DefaultWebSecurityManager {
        val securityManager = DefaultWebSecurityManager()
        securityManager.setRealm(myShiroRealm())
        return securityManager
    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    open fun shiroFilterFactoryBean(securityManager: WebSecurityManager): ShiroFilterFactoryBean {
        val shiroFilterFactoryBean = ShiroFilterFactoryBean()
        shiroFilterFactoryBean.securityManager = securityManager


        val map = LinkedHashMap<String, String>()
        //登出
        map["/logout"] = "logout"
        map["**/login**"] = "anon"

        map["**/statics/**"] = "anon"
        map["**/websocket/**"] = "anon"
        map["**/api/**"] = "anon"
        map["**/home/**"] = "anon"
        map["/logout"] = "anon"
        //对所有用户认证
        //map["/***"] = "authc"

        //登录
        shiroFilterFactoryBean.loginUrl = "/login"
        //首页
        shiroFilterFactoryBean.successUrl = "/index"
        //错误页面，认证不通过跳转
        shiroFilterFactoryBean.unauthorizedUrl = "/error.html"
        shiroFilterFactoryBean.filterChainDefinitionMap = map
        for (s in shiroFilterFactoryBean.filterChainDefinitionMap.keys) {
            println(s)
        }
        return shiroFilterFactoryBean
    }

    //加入注解的使用，不加入这个注解不生效
    @Bean
    open fun authorizationAttributeSourceAdvisor(securityManager: DefaultWebSecurityManager): AuthorizationAttributeSourceAdvisor {
        val authorizationAttributeSourceAdvisor = AuthorizationAttributeSourceAdvisor()
        authorizationAttributeSourceAdvisor.securityManager = securityManager
        return authorizationAttributeSourceAdvisor
    }
}
