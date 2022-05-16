package com.messenger.messengerapi.service

import com.messenger.messengerapi.model.User
import com.messenger.messengerapi.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

internal class UserServiceTest {

    private val userRepository = mockk<UserRepository>()
    private val userService = mockk<UserService>()

    @BeforeEach
    fun setUp() {
        val user = User(1L, "foo", Date.from(Instant.now()))
        every { userRepository.findById(1L).get() } returns user
        every { userService.attemptRegistration(user) } returns user
        every { userRepository.findByUsername("foo") } returns user
        every { userService.userExists("foo") } returns true
        every { userService.getUserById(1L) } returns user
    }

    @Test
    fun findOneFromRepo() {
        val testUser = userRepository.findById(1)
        assertEquals(testUser.get(), userRepository.findById(1).get())

        val testUser2 = userRepository.findByUsername("foo")
        assertEquals(testUser2, userRepository.findByUsername("foo"))
    }

    @Test
    fun getOneFromService() {
        val testUser = userService.getUserById(1L)
        assertEquals(testUser, userService.getUserById(1L))
    }

    @Test
    fun exists() {
        assert(userService.userExists("foo"))
    }
}