package com.github.xfl03.aadebt.json.auth

import com.github.xfl03.aadebt.json.Response

data class AuthRegRequest(
        var name: String,
        var email: String,
        var password: String
)

data class AuthRegResponse(var id: Int, var token: String) : Response()