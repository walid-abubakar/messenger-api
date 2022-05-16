package com.messenger.messengerapi.repository

import com.messenger.messengerapi.model.Message
import org.springframework.data.repository.CrudRepository

interface MessageRepository: CrudRepository<Message, Long> {
}