package com.github.xfl03.aadebt.android.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.github.xfl03.aadebt.R
import com.github.xfl03.aadebt.android.list.BasicListAdapter
import com.github.xfl03.aadebt.android.util.Constant
import com.github.xfl03.aadebt.android.util.GsonRequest
import com.github.xfl03.aadebt.json.CommonResponse
import com.github.xfl03.aadebt.json.aa.AAlistResponse
import com.github.xfl03.aadebt.json.aa.AAnewRequest
import com.github.xfl03.aadebt.json.aa.AAnewResponse
import com.github.xfl03.aadebt.json.aa.GroupInfo
import com.github.xfl03.aadebt.json.debt.DebtNewRequest
import kotlinx.android.synthetic.main.activity_group_new.*

class GroupNewActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_new)

        requestQueue = Volley.newRequestQueue(this)

        buttonGroupNew.setOnClickListener {
            if (editTextGroupName.text.isNullOrBlank()) {
                Constant.toast(this, "请输入名称")
            } else {
                newGroup(editTextGroupName.text.toString())
            }
        }
    }

    fun newGroup(name: String) {
        val request = GsonRequest(
            CommonResponse::class.java,
            Request.Method.GET,
            Constant.BASE_URL + "/api/debt/new",
            DebtNewRequest(name),
            Response.Listener { res ->
                if (res is CommonResponse) {
                    if (res.code < 0) {
                        Constant.log(res.msg)
                        Constant.toast(this, "创建失败")
                    } else {
                        Constant.toast(this, "创建成功")
                        setResult(Constant.OP_REFRESH)
                        finish()
                    }
                }
            }
        )
        requestQueue.add(request)
    }
}
