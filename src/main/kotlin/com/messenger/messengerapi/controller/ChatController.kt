package com.messenger.messengerapi.controller

import com.messenger.messengerapi.model.User
import com.messenger.messengerapi.repository.UserRepository
import com.messenger.messengerapi.service.ChatMapper
import com.messenger.messengerapi.service.ChatService
import com.messenger.messengerapi.vm.ChatVM
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// TODO: Authentication & Authorization
// TODO: Validate requests
// TODO: Consider exception handling with @ControllerAdvice
// TODO: Consider pagination

@RestController
@RequestMapping("/chat-manager/users")
class ChatController(
    val chatService: ChatService,
    val userRepository: UserRepository,
    val chatMapper: ChatMapper) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping
    @RequestMapping("/{userName}")
    fun list(@PathVariable("userName") userName: String): ResponseEntity<List<ChatVM?>> {
        try {
            val user = userRepository.findByUsername(userName) as User
            val chats = chatService.getAllChats(user.id)
            val chatsVM = chatMapper.toChatsVM(chats, user.id)
            log.info("Successfully retrieved active chats for user: $userName")
            return ResponseEntity.ok().body(chatsVM)
        } catch (throwable: Throwable) {
            log.error("Failed to retrieve active chats for user: $userName, error: ${throwable.message}")
            throw throwable
        }
    }

    @GetMapping
    @RequestMapping("/{userName}/sender/{senderUserName}")
    fun getBySender(@PathVariable("userName") userName: String, @PathVariable("senderUserName") senderUserName: String): ResponseEntity<ChatVM> {
        try {
            val currentUser = userRepository.findByUsername(userName) as User
            val otherUser = userRepository.findByUsername(senderUserName) as User
            val chat = chatService.getChat(currentUser, otherUser)
            log.info("successfully retrieved chat session of users: $userName and $senderUserName")
            return ResponseEntity.ok().body(chat?.let { chatMapper.toChatVM(it, currentUser.id) })
        } catch (throwable: Throwable) {
            log.error("Failed to retrieve active chat for recipient: $userName from sender $senderUserName, error: ${throwable.message}")
            throw throwable
        }
    }
}