package com.messenger.messengerapi.service

import com.messenger.messengerapi.model.Message
import com.messenger.messengerapi.vm.MessageVM
import org.springframework.stereotype.Component

@Component
class MessageMapper {
    fun toMessageVM(msg: Message) : MessageVM {
        return MessageVM(msg.id, msg.sender?.id, msg.recipient?.id, msg.chat?.id, msg.body, msg.createdAt)
    }
}