package com.github.xfl03.aadebt.json.aa

import com.github.xfl03.aadebt.json.Response

data class AAnewRequest(
        var name: String,
        var parts: List<String>
)

data class AAnewResponse(var groupId: Int, var name: String, var parts: List<PartInfo>) : Response()