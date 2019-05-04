package com.instant.message.controller

import com.alibaba.fastjson.JSON
import com.instant.message.config.ApplicationHelper
import com.instant.message.entity.OneToOneMessage
import com.instant.message.service.UserService
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.HashSet
import java.util.concurrent.ConcurrentHashMap
import javax.websocket.*
import javax.websocket.server.PathParam
import javax.websocket.server.ServerEndpoint


@Component
@ServerEndpoint(value = "/websocket/OneToMultiple/{socketId}/{userId}")
class WebSocketOneToMultipleOnLine {
    companion object {
        private var onlineCount: Int = 0
        private val connections = ConcurrentHashMap<String, HashSet<WebSocketOneToMultipleOnLine>>()

    }

    private var session: Session? = null
    private var id: String? = null
    private var socketId: String? = null

    /**
     * 连接建立成功调用的方法
     *
     * @param session
     * 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    fun onOpen(session: Session, @PathParam("socketId") socketId: String, @PathParam("userId") userId: String) {
        this.session = session
        this.socketId = socketId
        this.id = userId
        if (connections.size == 0) {
            val list = HashSet<WebSocketOneToMultipleOnLine>()
            list.add(this)
            connections[socketId] = list     //添加到map中
        }
        if (connections.containsKey(socketId)) {
            connections[socketId]?.add(this)
        } else {
            val list = HashSet<WebSocketOneToMultipleOnLine>()
            list.add(this)
            connections[socketId] = list     //添加到map中
        }



        addOnlineCount()               // 在线数加
        println("有新连接加入！新用户：" + ",当前在线人数为" + getOnlineCount())


    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    fun onClose() {
        connections[socketId]?.remove(this)
        subOnlineCount()          // 在线数减
        println("有一连接关闭！当前在线人数为" + getOnlineCount())
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     * 客户端发送过来的消息
     * @param session
     * 可选的参数
     */
    @OnMessage
    fun onMessage(message: String,session: Session) {
        println("来自客户端的消息:$message")

        val m = JSON.parseObject(message, OneToOneMessage::class.java)

        try {
            val userId = Integer.parseInt(id!!)
            val userService = ApplicationHelper.getBean("userService") as UserService

            val user = userService.selectByPrimaryKeyToMessage(userId)
            m.setUser(user)
            sendMessage(socketId, m,session)
        } catch (e: IOException) {
            e.printStackTrace()
        }


    }

    @Throws(IOException::class)
    private fun sendMessage(groupId: String?, message: OneToOneMessage,session: Session) {


        val toGroups = connections[groupId]
        if (toGroups != null) {

            //TODO:有时会发生同一个组里别人收到，而自己收不到的情况
            for (w in toGroups) {
                if(w.session!!.id==session.id){
                    message.setCurrent(true)
                }else{
                    message.setCurrent(false)
                }
                val json = JSON.toJSONString(message)
                if (w.session!!.isOpen) {
                    w.session!!.basicRemote.sendText(json)
                    println("当前连接已打开:$json")

                } else {
                    connections[groupId]?.remove(w)
                    println("当前连接已关闭:$json")

                }

            }


        }

    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    fun onError(session: Session, error: Throwable) {
        println("发生错误")
        session.close()
        error.printStackTrace()
        connections[socketId]?.remove(this)

    }


    @Synchronized
    fun getOnlineCount(): Int {
        return onlineCount
    }

    @Synchronized
    fun addOnlineCount() {
        WebSocketOneToMultipleOnLine.onlineCount++
    }

    @Synchronized
    fun subOnlineCount() {
        WebSocketOneToMultipleOnLine.onlineCount--
    }


    override fun hashCode(): Int {
        return id!!.hashCode() * socketId!!.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        when(other){
            null->return false
            this->return true
            is WebSocketOneToMultipleOnLine->{
                val socket = other as WebSocketOneToMultipleOnLine?
                if (socket!!.id == this.id)
                    return true
            }
        }
        return false
    }
}