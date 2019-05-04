package com.instant.message.service

import com.instant.message.entity.LoginToken
import com.instant.message.entity.User

interface LoginTokenService {
     fun selectUserByToken(token: String): User
     fun insert(token: LoginToken): Boolean
}