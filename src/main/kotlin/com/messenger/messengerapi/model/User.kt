package com.messenger.messengerapi.model

import org.springframework.format.annotation.DateTimeFormat
import java.time.Instant
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "`user`")
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    @Column(unique = true)
    var username: String = "",

    @DateTimeFormat
    var createdDate: Date = Date.from(Instant.now())
) {
//    @OneToMany(mappedBy = "sender", targetEntity = Message::class)
//    private var sentMessages: Collection<Message>? = null
//
//    @OneToMany(mappedBy = "recipient", targetEntity = Message::class)
//    private var receivedMessages: Collection<Message>? = null
}