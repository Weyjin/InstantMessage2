package com.instant.message.controller

import com.alibaba.fastjson.JSON
import com.instant.message.config.ApplicationHelper
import com.instant.message.entity.LoginToken
import com.instant.message.entity.ScanCode
import com.instant.message.entity.User
import com.instant.message.service.LoginTokenService
import com.instant.message.service.UserService
import org.apache.commons.codec.binary.Base64
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import javax.websocket.*
import javax.websocket.server.PathParam
import javax.websocket.server.ServerEndpoint


@Component
@ServerEndpoint(value = "/websocket/ScanCode/{socketId}/{userId}")
class WebSocketScanCode {
    companion object {
        private var onlineCount: Int = 0
        private val connections = ConcurrentHashMap<String, WebSocketScanCode>()
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
        if (userId.equals("0")) {
            return
        }
        try {
            val json = decode(userId)

            val scanCode = JSON.parseObject(json, ScanCode::class.java)
            val socketScanCode = connections[scanCode.getCode()]
            if (socketScanCode != null) {
                val userService = ApplicationHelper.getBean("userService") as UserService
                val user :User?= userService.selectByPrimaryKey(scanCode.getId())
                if (user != null) {
                    val loginTokenService = ApplicationHelper.getBean("loginTokenService") as LoginTokenService
                    val token = LoginToken()
                    token.setToken(scanCode.getCode())
                    token.setUserId(user.getId())
                    val success = loginTokenService.insert(token)
                    if (success) {
                        socketScanCode.session!!.basicRemote.sendText(token.getToken())
                        println("发送消息给浏览器执行登录")
                        //发送完消息之后，删除
                        connections.remove(socketId)
                        connections.remove(scanCode.getCode())
                    }

                }


            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun decode(encodeStr: String): String {
        var b = encodeStr.toByteArray()
        val base64 = Base64()
        b = base64.decode(b)
        return String(b)
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
    fun onMessage(message: String) {
        println("来自客户端的消息:$message")

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
        WebSocketScanCode.onlineCount++
    }

    @Synchronized
    fun subOnlineCount() {
        WebSocketScanCode.onlineCount--
    }
}
