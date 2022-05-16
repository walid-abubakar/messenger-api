package com.messenger.messengerapi.service

import com.messenger.messengerapi.model.Chat
import com.messenger.messengerapi.model.Message
import com.messenger.messengerapi.model.User
import com.messenger.messengerapi.repository.ChatRepository
import com.messenger.messengerapi.repository.MessageRepository
import com.messenger.messengerapi.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class MessageService(
    val messageRepository: MessageRepository,
    val chatRepository: ChatRepository,
    val chatService: ChatService,
    val userRepository: UserRepository
) {
    fun sendMessage(sender: User, recipientId: Long, msg: String): Message {
        val optional = userRepository.findById(recipientId)
        if (optional.isPresent) {
            val recipient = optional.get()
            if (msg.isNotEmpty()) {
                val chat: Chat = if (chatService.chatExists(sender, recipient)) {
                    chatService.getChat(sender, recipient) as Chat
                } else chatService.createChat(sender, recipient)
                val message = Message(sender, recipient, msg, chat)

                chatRepository.save(chat)
                messageRepository.save(message)
                return message
            }
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")
        }
        throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send message")
    }
}