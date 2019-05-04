package com.instant.message.dao

import com.instant.message.entity.Group
import org.springframework.stereotype.Repository

@Repository
interface GroupDao : IBaseDao<Group, Int>