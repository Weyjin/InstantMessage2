package com.instant.message.controller

import com.instant.message.service.LoginTokenService
import com.instant.message.service.QRCodeService
import com.instant.message.service.UserService
import org.apache.catalina.User
import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.UsernamePasswordToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import java.io.IOException
import java.util.*
import javax.servlet.http.HttpSession

@Controller
class LoginController {
    @Autowired
    private lateinit var  userService: UserService
    @Autowired
    private lateinit var qrCodeService: QRCodeService
    @Autowired
    private lateinit var tokenService: LoginTokenService

    @RequestMapping(value =["login"])
    @Throws(IOException::class)
    fun index(): ModelAndView {
        val view = ModelAndView()
        view.viewName = "login/login"
        val uuid = UUID.randomUUID().toString()
        val code = qrCodeService.createQRCode(uuid, 200, 200)
        view.addObject("code", code)
        view.addObject("uuid", uuid)

        return view
    }

    @RequestMapping(value = ["login"], method = [RequestMethod.POST])
    @ResponseBody
    fun login(name: String, password: String, session: HttpSession): ResponseEntity<Map<String, Any>> {
        val map = HashMap<String, Any>()

        map["name"] = name
        map["password"] = password

        val user :com.instant.message.entity.User?= userService.selectByOnly(map)
        val m = HashMap<String, Any>()

        when(user){
            null->{
                m["msg"] = "失败"
                return ResponseEntity(m, HttpStatus.NOT_FOUND)
            }
            else->{
                val token = UsernamePasswordToken(user.getName(), user.getPassword())
                val subject = SecurityUtils.getSubject()
                subject.login(token)

                session.setAttribute("user", user)
                m["msg"] = "成功"
                return ResponseEntity(m, HttpStatus.OK)
           }
        }
    }


    @RequestMapping(value = ["login/token"], method = [RequestMethod.POST])
    @ResponseBody
    fun loginToken(code: String, session: HttpSession): ResponseEntity<Map<String, Any>> {

        val user:com.instant.message.entity.User? = tokenService.selectUserByToken(code)
        val m = HashMap<String, Any>()
        when(user){
            null->{
                m["msg"] = "失败"
                return ResponseEntity(m, HttpStatus.NOT_FOUND)
            }
            else->{
                val token = UsernamePasswordToken(user.getName(), user.getPassword())
                val subject = SecurityUtils.getSubject()
                subject.login(token)
                session.setAttribute("user", user)
                m["msg"] = "成功"
                return ResponseEntity(m, HttpStatus.OK)
            }
        }

    }

    /**
     * 注销
     * @return
     */
    @RequestMapping(value = ["/logout"])
    fun logout(session: HttpSession): ModelAndView {
        val view = ModelAndView()
        view.viewName = "redirect:/login"
        session.setAttribute("user", null)
        SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject())
        return view
    }
}