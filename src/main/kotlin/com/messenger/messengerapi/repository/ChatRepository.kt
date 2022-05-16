package com.messenger.messengerapi.repository

import com.messenger.messengerapi.model.Chat
import org.springframework.data.repository.CrudRepository

interface ChatRepository:CrudRepository<Chat, Long> {
    fun findBySenderId(id: Long): List<Chat>
    fun findByRecipientId(id: Long): List<Chat>
    fun findBySenderIdAndRecipientId(senderId: Long, recipientId: Long): Chat?
}