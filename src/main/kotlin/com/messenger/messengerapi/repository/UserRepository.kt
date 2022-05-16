package com.messenger.messengerapi.repository

import com.messenger.messengerapi.model.User
import org.springframework.data.repository.CrudRepository

interface UserRepository:CrudRepository<User, Long> {
    fun findByUsername(username: String): User?
}