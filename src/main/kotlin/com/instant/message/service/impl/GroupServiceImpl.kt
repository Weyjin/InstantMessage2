package com.instant.message.service.impl

import com.instant.message.dao.GroupDao
import com.instant.message.entity.Group
import com.instant.message.service.GroupService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GroupServiceImpl : GroupService {

    @Autowired
    private lateinit var  groupDao: GroupDao

    override fun insert(`object`: Group): Boolean {
        return groupDao.insert(`object`)>0
    }

    override fun delete(id: Int): Boolean {
        return groupDao.delete(id)>0
    }

    override fun update(`object`: Group): Boolean {
        return groupDao.update(`object`)>0

    }

    override fun toList(map: Map<String, Any>): List<Group> {
        return groupDao.toList(map)
    }

    override fun selectByPrimaryKey(id: Int): Group {
       return groupDao.selectByPrimaryKey(id)
    }

    override fun selectByOnly(map: Map<String, Any>): Group {
        return groupDao.selectByOnly(map)
    }
}