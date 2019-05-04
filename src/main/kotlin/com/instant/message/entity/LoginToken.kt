package com.instant.message.entity

class LoginToken {
    private var id: Int = 0
    private var token: String? = null
    private var userId: Int = 0


    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getToken(): String? {
        return token
    }

    fun setToken(token: String) {
        this.token = token
    }

    fun getUserId(): Int {
        return userId
    }

    fun setUserId(userId: Int) {
        this.userId = userId
    }
}