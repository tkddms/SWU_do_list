package com.example.swudolistapp

import com.google.gson.annotations.SerializedName

// DATA 형태 정의
data class PostItem (
    val code: String,
    val msg: String
)

data class RequestSubject(
    val subjects: String
)

data class User(
    var id: String? = null,
    var pw: String? = null,
    var email: String? = null,
    var subjects: String? = null,
)