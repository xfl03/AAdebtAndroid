package com.github.xfl03.aadebt.android.util

import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonRequest
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

import com.github.xfl03.aadebt.android.util.Constant.Companion.GSON
import com.github.xfl03.aadebt.json.CommonResponse

class GsonRequest<T : com.github.xfl03.aadebt.json.Response>(
    private val clazz: Class<T>,
    method: Int,
    url: String,
    private val requestBody: Any?,
    listener: Response.Listener<com.github.xfl03.aadebt.json.Response>,
    errorListener: Response.ErrorListener = defaultErrorListener
) : JsonRequest<com.github.xfl03.aadebt.json.Response>(
    method,
    url,
    if (requestBody == null) null else GSON.toJson(requestBody),
    listener,
    errorListener
) {
    companion object {
        private var token: String? = null
        fun setToken(newToken: String?) {
            token = newToken
        }

        private val defaultErrorListener = Response.ErrorListener {
            Constant.log("ERROR: $it")
        }
    }

    override fun getHeaders(): MutableMap<String, String> {
        if (Constant.DEBUG) {
            Constant.log(if (requestBody == null) "NOBODY" else GSON.toJson(requestBody))
        }
        return if (token.isNullOrBlank()) {
            //Constant.log("NOTOKEN")
            super.getHeaders()
        } else {
            //Constant.log(token!!)
            mutableMapOf(Pair("Authorization", "Bearer $token"))
        }
    }

    override fun parseNetworkResponse(response: NetworkResponse?): Response<com.github.xfl03.aadebt.json.Response> {
        return try {
            val json = String(
                response?.data ?: ByteArray(0),
                Charset.forName(HttpHeaderParser.parseCharset(response?.headers))
            )
            if (Constant.DEBUG) {
                Constant.log(json)
            }
            val res: com.github.xfl03.aadebt.json.Response
            res = try {
                GSON.fromJson(json, clazz)
            } catch (e: ClassCastException) {
                //Constant.log(e.toString())
                GSON.fromJson(json, CommonResponse::class.java)
            }
            Response.success(
                res,
                HttpHeaderParser.parseCacheHeaders(response)
            )
        } catch (e: Exception) {
            Response.error(ParseError(e))
        }
    }
}