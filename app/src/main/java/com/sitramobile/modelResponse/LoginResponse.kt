package com.sitramobile.modelResponse

data class LoginResponse (
    val Role: String,
    val id: String,
    val message: String,
    val status: Boolean?=false
)