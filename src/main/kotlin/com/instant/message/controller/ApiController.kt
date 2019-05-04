package com.instant.message.controller

import com.instant.message.entity.Group
import com.instant.message.entity.GroupChat
import com.instant.message.entity.User
import com.instant.message.service.GroupChatService
import com.instant.message.service.GroupService
import com.instant.message.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import java.util.ArrayList
import java.util.HashMap

@Controller
@RequestMapping(value = ["api"])
class ApiController {
    @Autowired
    private lateinit var groupService: GroupService
    @Autowired
    private lateinit var groupChatService: GroupChatService
    @Autowired
    private lateinit var userService: UserService


    @RequestMapping(value = ["getGroups"])
    @ResponseBody
    fun getGroups(): List<Group> {
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
        return groups
    }

    @RequestMapping(value = ["getGroupChats"], method = [RequestMethod.POST])
    @ResponseBody
    fun getGroupChats(userId: String): List<GroupChat> {
        val map = HashMap<String, Any>()
        map["userId"] = userId
        return groupChatService.toList(map)
    }

    @RequestMapping(value = ["login"], method = [RequestMethod.POST])
    @ResponseBody
    fun login(name: String, password: String): MutableMap<String, Any?> {
        val map: MutableMap<String, Any> = HashMap()
        map["name"] = name
        map["password"] = password

        val user:User? = userService.selectByOnly(map)

        var m :MutableMap<String, Any?> = HashMap()
        if (user == null) {
            m["msg"] = "error"
            m["userName"] = ""
            m["userId"] = ""
            m["signature"] = ""
        } else {
            m["msg"] = "success"
            m["userName"] = user.getName()
            m["userId"] = user.getId()
            m["signature"] = user.getSignature()
        }
        return m
    }


}