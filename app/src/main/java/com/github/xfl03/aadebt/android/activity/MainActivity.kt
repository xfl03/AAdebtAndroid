package com.github.xfl03.aadebt.android.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ShortcutManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
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
import com.github.xfl03.aadebt.json.aa.AAlistResponse
import com.github.xfl03.aadebt.json.aa.GroupInfo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var listAdapter: BasicListAdapter<GroupInfo>
    private lateinit var requestQueue: RequestQueue
    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)
        sp = getSharedPreferences("data", Context.MODE_PRIVATE)

        title = "AAdebt"

        val viewManager = LinearLayoutManager(this)
        listAdapter = BasicListAdapter {
            val intent = Intent(this, DebtActivity::class.java)
            intent.putExtra("id", it.groupId)
            intent.putExtra("name", it.name)
            startActivity(intent)
        }
        recyclerViewGroup.apply {
            layoutManager = viewManager
            adapter = listAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }


        if (sp.getString("token", "").isNullOrEmpty()) {
            requestLogin()
        } else {
            GsonRequest.setToken(sp.getString("token", null))
            loadGroupList()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode and Constant.OP_CLOSE != 0) {
            finish()
        }
        if (resultCode and Constant.OP_REFRESH != 0) {
            loadGroupList()
        }
        if (resultCode and Constant.OP_REFRESH_TOKEN != 0 && data != null) {
            val token = data.getStringExtra("token")
            sp.edit().putString("token", token).apply()
            GsonRequest.setToken(token)
        }
    }

    private fun loadGroupList() {
        val request = GsonRequest(
            AAlistResponse::class.java,
            Request.Method.GET,
            Constant.BASE_URL + "/api/aa/list",
            null,
            Response.Listener { res ->
                if (res is AAlistResponse) {
                    val data = BasicListAdapter.newDataList<GroupInfo>()
                    res.groups.forEach {
                        if (it.type == 1) {
                            data.add(it)
                        }
                    }
                    listAdapter.update(data)

                }
            }
        )
        requestQueue.add(request)
    }

    private fun logout() {
        sp.edit().putString("token", "").apply()
        requestLogin()
    }

    private fun requestLogin(){
        startActivityForResult(Intent(this, LoginActivity::class.java), 0)
    }
}
