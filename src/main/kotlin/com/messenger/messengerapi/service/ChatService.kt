package com.messenger.messengerapi.service

import com.messenger.messengerapi.model.Chat
import com.messenger.messengerapi.model.User
import com.messenger.messengerapi.repository.ChatRepository
import org.springframework.stereotype.Service

@Service
class ChatService(val chatRepository: ChatRepository) {
    fun createChat(sender: User, recipient: User): Chat {
        return chatRepository.save(Chat(sender, recipient))
    }

    fun getChat(sender: User, recipient: User): Chat? {
        return when {
            chatRepository.findBySenderIdAndRecipientId(sender.id, recipient.id) != null ->
                chatRepository.findBySenderIdAndRecipientId(sender.id, recipient.id)
            chatRepository.findBySenderIdAndRecipientId(recipient.id, sender.id) != null ->
                chatRepository.findBySenderIdAndRecipientId(recipient.id, sender.id)
            else -> null
        }
    }

    fun getAllChats(userId: Long): ArrayList<Chat> {
        val chats: ArrayList<Chat> = ArrayList()
        chats.addAll(chatRepository.findBySenderId(userId))
        chats.addAll(chatRepository.findByRecipientId(userId))
        return chats
    }

    fun chatExists(sender: User, recipient: User): Boolean {
        return chatRepository.findBySenderIdAndRecipientId(sender.id, recipient.id) != null ||
                chatRepository.findBySenderIdAndRecipientId(recipient.id, sender.id) != null
    }

    fun userNameOfOther(chat: Chat, userId: Long): String {
        return if (chat.sender?.id == userId) chat.recipient?.username as String else chat.sender?.username as String
    }
}