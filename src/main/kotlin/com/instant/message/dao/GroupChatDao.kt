package com.instant.message.dao

import com.instant.message.entity.GroupChat
import org.springframework.stereotype.Repository

@Repository
interface GroupChatDao : IBaseDao<GroupChat, Int>