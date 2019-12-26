package com.github.xfl03.aadebt.json.debt

import java.sql.Date

data class DebtAddRequest(
        var groupId: Int,
        var name: String,
        var amount: Int,
        var type: Int,
        var date: Date,
        var special: Boolean = false
)