package com.instant.message.entity

class OneToOneMessage {
    private var currentUserId: String? = null
    private var toUserId: String? = null
    private var message: String? = null
    private var isCurrent: Boolean = false
    private var user: Result? = null

    fun getCurrentUserId(): String? {
        return currentUserId
    }

    fun setCurrentUserId(currentUserId: String) {
        this.currentUserId = currentUserId
    }

    fun getToUserId(): String? {
        return toUserId
    }

    fun setToUserId(toUserId: String) {
        this.toUserId = toUserId
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun isCurrent(): Boolean {
        return isCurrent
    }

    fun setCurrent(current: Boolean) {
        isCurrent = current
    }

    fun getUser(): Result? {
        return user
    }

    fun setUser(user: Result) {
        this.user = user
    }
}