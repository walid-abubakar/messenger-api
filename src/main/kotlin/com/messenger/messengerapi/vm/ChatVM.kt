package com.messenger.messengerapi.vm

data class ChatVM(
    val chatId: Long,
    val otherUser: String,
    val messages: ArrayList<MessageVM>
)