package com.github.xfl03.aadebt.android.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.github.xfl03.aadebt.R
import com.github.xfl03.aadebt.android.list.BasicListAdapter
import com.github.xfl03.aadebt.android.util.Constant
import com.github.xfl03.aadebt.android.util.GsonRequest
import com.github.xfl03.aadebt.json.aa.GroupInfo
import com.github.xfl03.aadebt.json.debt.DebtDebtRequest
import com.github.xfl03.aadebt.json.debt.DebtDebtResponse
import com.github.xfl03.aadebt.json.debt.DebtDetailInfo
import kotlinx.android.synthetic.main.activity_debt_list.*

class DebtListActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue
    private lateinit var listAdapter: BasicListAdapter<DebtDetailInfo>

    private var id = 0
    private var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_list)

        requestQueue = Volley.newRequestQueue(this)

        if (intent != null) {
            id = intent.getIntExtra("id", 0)
            if (intent.hasExtra("name")) {
                name = intent.getStringExtra("name")
            }
        }

        val viewManager = LinearLayoutManager(this)
        listAdapter = BasicListAdapter()
        recyclerViewGroup2.apply {
            layoutManager = viewManager
            adapter = listAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@DebtListActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

        title = name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        debtList()
    }

    fun debtList() {
        val request = GsonRequest(
            DebtDebtResponse::class.java,
            Request.Method.POST,
            Constant.BASE_URL + "/api/debt/debt",
            DebtDebtRequest(id),
            Response.Listener { res ->
                if (res is DebtDebtResponse) {
                    val data = BasicListAdapter.newDataList<DebtDetailInfo>()
                    data.addAll(res.debts)
                    listAdapter.update(data)
                }
            }
        )
        requestQueue.add(request)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
