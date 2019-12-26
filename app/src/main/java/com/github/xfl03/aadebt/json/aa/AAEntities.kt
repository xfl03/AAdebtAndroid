package com.github.xfl03.aadebt.json.aa

data class GroupInfo(var groupId: Int, var name: String, var locked: Boolean, var type: Int)

data class DebtDetailInfo(var debtId: Int, var name: String, var payer: PartInfo, var amount: Int)

data class PartInfo(var partId: Int, var name: String)

data class DetailInfo(
        var partId: Int,
        var amount: Int
)

data class PartDetailInfo(var partId: Int, var name: String, var paid: Int, var total: Int)

data class PartPayInfo(var from: PartInfo, var to: PartInfo, var amount: Int)