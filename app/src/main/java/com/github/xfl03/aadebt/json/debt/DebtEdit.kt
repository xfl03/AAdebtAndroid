package com.github.xfl03.aadebt.json.debt

import java.sql.Date

data class DebtEditRequest(
        val groupId: Int,
        val debtId: Int,
        var name: String,
        var amount: Int,
        var type: Int,
        var date: Date
)