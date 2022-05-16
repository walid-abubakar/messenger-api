package com.messenger.messengerapi.vm

import java.util.Date

data class MessageVM(
    val id: Long,
    val senderId: Long?,
    val recipientId: Long?,
    val chatId: Long?,
    val body: String?,
    val createdDate: Date
)