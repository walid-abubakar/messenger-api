package com.messenger.messengerapi.service

import com.messenger.messengerapi.model.User
import com.messenger.messengerapi.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService (val userRepository: UserRepository) {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    fun attemptRegistration(userDetails: User): User {
        try {
            if (!userExists(userDetails.username)) {
                val user = User()
                user.username = userDetails.username
                userRepository.save(user)
                return user
            }
            throw ResponseStatusException(HttpStatus.CONFLICT, "User already exists")
        } catch (throwable: Throwable) {
            log.error("Registration failed for user: ${userDetails.username}")
            throw throwable
        }
    }

    fun userExists(username: String): Boolean { // TODO: Better name
        return userRepository.findByUsername(username) != null
    }

    fun getUserById(id: Long): User {
        val userOptional = userRepository.findById(id)
        if (userOptional.isPresent) {
            return userOptional.get()
        }
        throw RuntimeException("User does not exist")
    }
}