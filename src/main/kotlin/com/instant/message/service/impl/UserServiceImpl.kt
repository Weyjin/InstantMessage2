package com.instant.message.service.impl

import com.instant.message.dao.UserDao
import com.instant.message.entity.Result
import com.instant.message.entity.User

import com.instant.message.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("userService")
class UserServiceImpl :UserService{

    @Autowired
    lateinit var userDao: UserDao

    override fun selectByPrimaryKeyToMessage(id: Int): Result {
        return userDao.selectByPrimaryKeyToMessage(id)

    }

    override fun insert(`object`: User): Boolean {
        return userDao.insert(`object`)>0
    }

    override fun delete(id: Int): Boolean {
        return userDao.delete(id)>0
    }

    override fun update(`object`: User): Boolean {
        return userDao.update(`object`)>0
    }

    override fun toList(map: Map<String, Any>): List<User> {
        return userDao.toList(map)
    }

    override fun selectByPrimaryKey(id: Int): User {
        return userDao.selectByPrimaryKey(id)
    }

    override fun selectByOnly(map: Map<String, Any>): User {
        return userDao.selectByOnly(map)
    }



}