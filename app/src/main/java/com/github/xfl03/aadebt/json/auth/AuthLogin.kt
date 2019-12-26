package com.github.xfl03.aadebt.json.auth

import com.github.xfl03.aadebt.json.Response

data class AuthLoginRequest(
        var email: String,
        var password: String
)

data class AuthLoginResponse(var id: Int, var token: String) : Response()
