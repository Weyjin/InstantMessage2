package com.instant.message.controller

import com.alibaba.fastjson.JSON
import com.instant.message.config.ApplicationHelper
import com.instant.message.entity.OneToOneMessage
import com.instant.message.service.UserService
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import javax.websocket.*
import javax.websocket.server.PathParam
import javax.websocket.server.ServerEndpoint

@Component
@ServerEndpoint(value = "/websocket/OneToOne/{socketId}/{userId}")
class WebSocketOneToOneOnLine {
    companion object {
        private var onlineCount: Int = 0
        private val connections = ConcurrentHashMap<String, WebSocketOneToOneOnLine>()
    }

    private var session: Session? = null
    private var id: String? = null
    private var socketId: String? = null

    @OnOpen
    fun onOpen(session: Session, @PathParam("socketId") socketId: String, @PathParam("userId") userId: String) {
        this.session = session
        this.socketId = socketId
        this.id = userId

        connections[socketId] = this     //添加到map中

        addOnlineCount()               // 在线数加
        println("有新连接加入！新用户：" + ",当前在线人数为" + getOnlineCount())


    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    fun onClose() {
        connections.remove(socketId)  // 从map中移除
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
    fun onMessage(message: String, session: Session) {
        println("来自客户端的消息:$message")

        val m = JSON.parseObject(message, OneToOneMessage::class.java)

        try {
            val userId = Integer.parseInt(id!!)
            val userService = ApplicationHelper.getBean("userService") as UserService

            val user = userService.selectByPrimaryKeyToMessage(userId)
            m.setUser(user)
            sendMessage(m.getToUserId(), m,session)
        } catch (e: IOException) {
            e.printStackTrace()
        }


    }

    @Throws(IOException::class)
    private fun sendMessage(toUserSocketId: String?, message: OneToOneMessage,session: Session) {
        println("开始发消息...")
        message.setCurrent(true)
        val json1 = JSON.toJSONString(message)
            session.basicRemote.sendText(json1)
            println("发送消息给自己:$json1")

        val toUser = connections[toUserSocketId]
        if (toUser != null) {
            message.setCurrent(false)
            val json2 = JSON.toJSONString(message)
            if (toUser.session!!.isOpen) {
                toUser.session!!.basicRemote.sendText(json2)
                println("发送消息给对方:$json2")

            } else {
                connections.remove(toUserSocketId)
            }


        }
        println("发完消息...")

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

    }


    @Synchronized
    fun getOnlineCount(): Int {
        return onlineCount
    }

    @Synchronized
    fun addOnlineCount() {
        WebSocketOneToOneOnLine.onlineCount++
    }

    @Synchronized
    fun subOnlineCount() {
        WebSocketOneToOneOnLine.onlineCount--
    }
}

