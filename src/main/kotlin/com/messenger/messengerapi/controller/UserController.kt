package com.messenger.messengerapi.controller

import com.messenger.messengerapi.model.User
import com.messenger.messengerapi.service.UserMapper
import com.messenger.messengerapi.service.UserService
import com.messenger.messengerapi.vm.UserVM
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(val userService: UserService, val userMapper: UserMapper) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping
    @RequestMapping("/register")
    fun create(@Validated @RequestBody userRequest: User): ResponseEntity<UserVM> {
        val user = userService.attemptRegistration(userRequest)
        log.info("registering user: ${user.username}")
        return ResponseEntity.status(201).body(userMapper.toUserVM(user))
    }

    @GetMapping
    @RequestMapping("/{userId}")
    fun get(@PathVariable("userId") userId: Long): ResponseEntity<UserVM> {
        val user = userService.getUserById(userId)
        log.info("successfully retrieved data of user: ${user.username}")
        return ResponseEntity.ok().body(userMapper.toUserVM(user))
    }
}

