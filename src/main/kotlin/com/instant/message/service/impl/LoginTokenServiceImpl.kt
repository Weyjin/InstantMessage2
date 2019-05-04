package com.instant.message.service.impl

import com.instant.message.dao.LoginTokenDao
import com.instant.message.entity.LoginToken
import com.instant.message.entity.User
import com.instant.message.service.LoginTokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("loginTokenService")
class LoginTokenServiceImpl : LoginTokenService {
    @Autowired
    private lateinit var tokenDao: LoginTokenDao
    override fun selectUserByToken(token: String): User {
        return tokenDao.selectUserByToken(token)
    }

    override fun insert(token: LoginToken): Boolean {
        return tokenDao.insert(token)>0
    }
}