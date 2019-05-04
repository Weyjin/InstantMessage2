package com.instant.message.dao

import com.instant.message.entity.Result
import com.instant.message.entity.User
import org.springframework.stereotype.Repository

@Repository
interface UserDao : IBaseDao<User, Int> {
     fun selectByGroupId(groupId: Int): List<Result>
     fun selectByPrimaryKeyToMessage(id: Int): Result
}