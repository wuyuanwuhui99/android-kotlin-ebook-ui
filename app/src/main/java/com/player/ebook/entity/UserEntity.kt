package com.player.ebook.entity

import java.util.*

data class UserEntity(
    var userId: String,
    var createDate: String,
    var updateDate: String,
    var username: String,
    var telephone: String,
    var email: String,
    var avater: String,
    var birthday: String,
    var sex: String,
    var role: String,
    var password: String
)

