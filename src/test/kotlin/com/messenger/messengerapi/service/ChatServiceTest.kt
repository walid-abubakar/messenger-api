package com.messenger.messengerapi.service

import com.messenger.messengerapi.model.Chat
import com.messenger.messengerapi.model.User
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.Date

internal class ChatServiceTest {

    private val chatService = mockk<ChatService>()

    private val testUser1 = User(1L, "foo", Date.from(Instant.now()))
    private val testUser2 = User(2L, "bar", Date.from(Instant.now()))
    private val chat = Chat(testUser1, testUser2, 3L, Date.from(Instant.now()))

    @BeforeEach
    fun setUp() {
        every { chatService.createChat(testUser1, testUser2) } returns chat
        every { chatService.getChat(testUser1, testUser2) } returns chat
        every { chatService.getAllChats(1L) } returns arrayListOf(chat)
        every { chatService.chatExists(testUser1, testUser2) } returns true
        every { chatService.userNameOfOther(chat, 1L) } returns "bar"
    }

    @Test
    fun createChat() {
        assertEquals(chat, chatService.createChat(testUser1, testUser2))
    }

    @Test
    fun getChat() {
        assertEquals(chat, chatService.getChat(testUser1, testUser2))
    }

    @Test
    fun getAllChats() {
        assertEquals(arrayListOf(chat), chatService.getAllChats(1L))
    }

    @Test
    fun chatExists() {
        assertEquals(true, chatService.chatExists(testUser1, testUser2))
    }

    @Test
    fun userNameOfOther() {
        assertEquals("bar", chatService.userNameOfOther(chat, 1L))
    }
}