package com.messenger.messengerapi.controller

import com.messenger.messengerapi.model.User
import com.messenger.messengerapi.repository.UserRepository
import com.messenger.messengerapi.service.MessageMapper
import com.messenger.messengerapi.service.MessageService
import com.messenger.messengerapi.vm.MessageVM
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// TODO: Validate requests
// TODO: Authentication

@RestController
@RequestMapping("/messages")
class MessageController(
    val messageService: MessageService,
    val userRepository: UserRepository,
    val messageMapper: MessageMapper) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping
    fun create(@RequestBody requestBody: MessageRequestBody): ResponseEntity<MessageVM> {
        try {
            val sender = userRepository.findByUsername(requestBody.senderUserName) as User
            val message = messageService.sendMessage(sender, requestBody.recipientId, requestBody.message)
            log.info("message sent to: ${message.recipient?.username}, from: ${sender.username}")
            return ResponseEntity.status(201).body(messageMapper.toMessageVM(message))
        } catch (throwable: Throwable) {
           log.error("Failed to send message for user: ${requestBody.senderUserName}, to user: ${requestBody.recipientId}")
           throw throwable
        }
    }
}

data class MessageRequestBody(
    val senderUserName: String,
    val recipientId: Long,
    val message: String
)