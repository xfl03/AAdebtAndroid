package com.github.xfl03.aadebt.json.aa

import com.github.xfl03.aadebt.json.Response

data class AAaddRequest(
        var groupId: Int,
        var name: String,
        var payerId: Int,
        var details: List<DetailInfo>
)

data class AAaddResponse(var groupId: Int, var debtId: Int, var name: String, var payer: PartInfo, var amount: Int): Response()