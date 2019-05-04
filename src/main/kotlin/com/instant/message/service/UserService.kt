package com.instant.message.service

import com.instant.message.entity.Result
import com.instant.message.entity.User

interface UserService :IBaseService<User, Int> {
     fun selectByPrimaryKeyToMessage(id: Int): Result
}