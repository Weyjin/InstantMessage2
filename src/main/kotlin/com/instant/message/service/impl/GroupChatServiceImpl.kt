package com.instant.message.service.impl

import com.instant.message.dao.GroupChatDao
import com.instant.message.entity.GroupChat
import com.instant.message.service.GroupChatService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("groupChatService")
open class GroupChatServiceImpl : GroupChatService{

    @Autowired
    private lateinit var  groupChatDao: GroupChatDao

    override fun insert(`object`: GroupChat): Boolean {
        return groupChatDao.insert(`object`)>0
    }

    override fun delete(id: Int): Boolean {
        return groupChatDao.delete(id)>0
    }

    override fun update(`object`: GroupChat): Boolean {
        return groupChatDao.update(`object`)>0
    }

    override fun toList(map: Map<String, Any>): List<GroupChat> {
        return groupChatDao.toList(map)
    }

    override fun selectByPrimaryKey(id: Int): GroupChat {
        return groupChatDao.selectByPrimaryKey(id)
    }

    override fun selectByOnly(map: Map<String, Any>): GroupChat {
        return groupChatDao.selectByOnly(map)
    }
}