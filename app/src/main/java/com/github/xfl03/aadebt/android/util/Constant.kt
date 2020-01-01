package com.github.xfl03.aadebt.android.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.text.SimpleDateFormat
import java.util.*

class Constant {
    companion object {
        const val DEBUG = false
        const val DEBUG_TAG = "AADEBT_DEBUG"
        const val BASE_URL = "https://aa.csl.littleservice.cn"
        val GSON: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd").create()
        val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd")

        const val OP_REFRESH = 1
        const val OP_CLOSE = 2
        const val OP_REFRESH_TOKEN = 4

        val TYPES = arrayOf(
            "其它", "正餐", "零食", "生活", "交通",
            "教育", "电器", "游戏", "娱乐", "通讯",
            "服饰", "礼物", "捐赠"
        )

        fun log(msg: String) {
            Log.d(DEBUG_TAG, msg)
        }

        fun toast(context: Context, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        fun parseAmount(amount: Int): String {
            return if (amount % 10 == 0)
                String.format("%d.%d", amount / 100, (amount % 100) / 10)
            else
                String.format("%d.%02d", amount / 100, amount % 100)
        }
    }
}