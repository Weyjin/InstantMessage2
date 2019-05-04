package com.instant.message.shiro

import com.instant.message.config.ApplicationHelper
import com.instant.message.entity.User
import com.instant.message.service.UserService
import org.apache.shiro.authc.*
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.authz.SimpleAuthorizationInfo
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection
import java.util.ArrayList
import java.util.HashMap

class MyShiroRealm : AuthorizingRealm() {

    /**
     * 登录后授权
     * @param pc
     * @return
     */
    override fun doGetAuthorizationInfo(pc: PrincipalCollection): AuthorizationInfo {
        println("登录授权>>>")
        //getPrimaryPrincipal()获得的是
        //SimpleAuthenticationInfo()传进来的第一个参数
        val user = pc.primaryPrincipal as User?
        //String name=pc.getPrimaryPrincipal().toString();

        try {
            //User user=userService.getUserByName(name);
            if(user==null){
                throw UnknownAccountException()//没找到账号
            }

            //val id = user.getId()
            //val username = user.getName()


            //获得用户可以访问的url
            //List<String> urls=userService.permissionlist(username);
            // System.out.println("url>>> "+urls.toString());
            //授权
            val simpleAuthorizationInfo = SimpleAuthorizationInfo()
            //simpleAuthorizationInfo.addStringPermissions(urls);

            //设置角色
            val roles = ArrayList<String>()
            simpleAuthorizationInfo.addRoles(roles)

            return simpleAuthorizationInfo

        } catch (ex: Exception) {
            println("授权异常>>>")
            ex.printStackTrace()
        }

        throw UnknownAccountException()
    }

    /**
     * 登录认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Throws(AuthenticationException::class)
    override fun doGetAuthenticationInfo(token: AuthenticationToken): AuthenticationInfo {
        /**
         * UnknownAccountException 用户名未知...
         * IncorrectCredentialsException 凭据不正确，例如密码不正确 ...
         * LockedAccountException lae 用户被锁定，例如管理员把某个用户禁用...
         * ExcessiveAttemptsException 尝试认证次数多余系统指定次数 ...
         * AuthenticationException  其他未知错误
         */
        //通过表单接收的用户名
        val username = token.principal as String

        val map = HashMap<String, Any>()
        map["name"] = username
        val userService = ApplicationHelper.getBean("userService") as UserService
        val user:User?=userService.selectByOnly(map)

        if(user==null){
            throw UnknownAccountException()
        }

        val password = user.getPassword()
        return SimpleAuthenticationInfo(user, password, name)
    }
}