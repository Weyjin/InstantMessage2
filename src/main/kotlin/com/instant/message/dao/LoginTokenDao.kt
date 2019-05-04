package com.instant.message.dao

import com.instant.message.entity.LoginToken
import com.instant.message.entity.User
import org.springframework.stereotype.Repository

@Repository
interface LoginTokenDao: IBaseDao<LoginToken,Int> {

    fun selectUserByToken(token:String):User
}