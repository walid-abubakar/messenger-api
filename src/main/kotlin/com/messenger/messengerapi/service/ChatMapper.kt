package com.messenger.messengerapi.service

import com.messenger.messengerapi.model.Chat
import com.messenger.messengerapi.vm.ChatVM
import com.messenger.messengerapi.vm.MessageVM
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit

@Component
class ChatMapper(val messageMapper: MessageMapper, val chatService: ChatService) {

    fun toChatVM(chat: Chat, currentUserId: Long): ChatVM? {
        val chatMessages: ArrayList<MessageVM> = ArrayList()
        chat.messages?.mapTo(chatMessages) { msg -> messageMapper.toMessageVM(msg) }
        var filteredChatMessages: ArrayList<MessageVM> = ArrayList()
        chatMessages.filterTo(filteredChatMessages) { msg -> isAfterExpirationDate(msg)}
        if (filteredChatMessages.size > 3) {
            // TODO: don't hardcode message limit, inject from application config or use fetch using 'limit' on table query
            filteredChatMessages = (chatMessages.takeLast(100) as? ArrayList<MessageVM>)!!
        }
        val userNameOfOther = chatService.userNameOfOther(chat, currentUserId)
        return ChatVM(chat.id, userNameOfOther, filteredChatMessages)
    }

    fun toChatsVM(chats: ArrayList<Chat>, userId: Long): List<ChatVM?> {
        return chats.map { toChatVM(it, userId) }
    }

    // TODO:  Inject the expiration date via application config
     private fun isAfterExpirationDate(messageVM: MessageVM): Boolean {
         val timeOfMessage: Instant = messageVM.createdDate.toInstant()
         val limitDate: Instant = Instant.now().minus(30, ChronoUnit.DAYS)
        if (timeOfMessage.isAfter(limitDate))
            return true
        return false
    }
}