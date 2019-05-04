package com.instant.message.controller

import com.instant.message.entity.GroupChat
import com.instant.message.entity.User
import com.instant.message.service.GroupChatService
import com.instant.message.service.GroupService
import com.instant.message.service.UserService
import org.apache.shiro.SecurityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import java.util.ArrayList
import java.util.HashMap
import javax.servlet.http.HttpSession


@Controller
class HomeController{
    @Autowired
    private lateinit var groupService: GroupService
    @Autowired
    private lateinit var groupChatService: GroupChatService
    @Autowired
    private lateinit var userService: UserService



    @RequestMapping(value = ["index"])
    fun index(): ModelAndView {
        val view = ModelAndView()
        val user = SecurityUtils.getSubject().principal as User?
        if (user == null) {
            view.viewName = "login/login"
            return view
        }
        view.viewName = "home/index"
        view.addObject("user", user)
        return view
    }

    @RequestMapping(value = ["message/{id}"])
    fun message(@PathVariable("id") id: Int, session: HttpSession): ModelAndView {
        val view = ModelAndView()
        view.viewName = "home/message"
        val toUser = userService.selectByPrimaryKey(id)
        view.addObject("toUser", toUser)
        val user = session.getAttribute("user") as User
        view.addObject("currentUser", user)
        return view
    }

    @RequestMapping(value = ["toMessageGroupChat/{id}"])
    fun toMessageGroupChat(@PathVariable("id") id: Int, session: HttpSession): ModelAndView {
        val view = ModelAndView()
        view.viewName = "home/messageGroupChat"
        view.addObject("toGroup", id)
        val user = session.getAttribute("user") as User
        view.addObject("currentUser", user)
        return view
    }


    @RequestMapping(value = ["getGroups"])
    @ResponseBody
    fun getGroups(): Map<String, Any?> {
        val groups = groupService.toList(HashMap())
        val list = ArrayList<String?>()
        val objects = ArrayList<Any?>()
        for (g in groups) {
            objects.add(g.getUsers())
            list.add(g.getName())
        }
        val map = HashMap<String, Any>()
        map["groups"] = list
        map["users"] = objects
        return map
    }

    @RequestMapping(value = ["getGroupChats"])
    @ResponseBody
    fun getGroupChats(): List<GroupChat> {
        val user = SecurityUtils.getSubject().principal as User
        val map = HashMap<String, Any>()
        map["userId"] = user.getId()
        return groupChatService.toList(map)


    }
}