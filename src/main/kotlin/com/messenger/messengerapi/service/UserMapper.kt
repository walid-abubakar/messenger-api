package com.messenger.messengerapi.service

import com.messenger.messengerapi.model.User
import com.messenger.messengerapi.vm.UserVM
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun toUserVM(user: User) : UserVM {
        return UserVM(user.id, user.username, user.createdDate.toString())
    }
}