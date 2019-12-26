package com.github.xfl03.aadebt.android.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.github.xfl03.aadebt.R
import com.github.xfl03.aadebt.android.listener.FabTouchListener
import com.github.xfl03.aadebt.android.util.Constant
import com.github.xfl03.aadebt.android.util.GsonRequest
import com.github.xfl03.aadebt.json.debt.DebtCalRequest
import com.github.xfl03.aadebt.json.debt.DebtCalResponse
import kotlinx.android.synthetic.main.activity_debt.*

class DebtActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue
    private lateinit var sp: SharedPreferences

    private var id = 0
    private var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt)

        requestQueue = Volley.newRequestQueue(this)
        sp = getSharedPreferences("fab", Context.MODE_PRIVATE)

        if (intent != null) {
            id = intent.getIntExtra("id", 0)
            name = intent.getStringExtra("name")
        }

        title = name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fab.apply {
            x = sp.getFloat("x", 60f)
            y = sp.getFloat("y", 699f)
            setOnTouchListener(FabTouchListener(this@DebtActivity))
            setOnClickListener {
                val intent = Intent(this@DebtActivity, DebtNewActivity::class.java)
                intent.putExtra("id", this@DebtActivity.id)
                intent.putExtra("name", name)
                startActivityForResult(intent, 0)
            }
        }

        loadNormalDebt(id)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Constant.log(resultCode.toString())
        if (resultCode and Constant.OP_REFRESH != 0) {
            loadNormalDebt(id)
        }
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

    private fun loadNormalDebt(id: Int) {
        val request = GsonRequest(
            DebtCalResponse::class.java,
            Request.Method.POST,
            Constant.BASE_URL + "/api/debt/cal",
            DebtCalRequest(id),
            Response.Listener {
                if (it is DebtCalResponse) {
                    textViewTotal.text = parseAmount(it.total)
                    textViewDaily.text = parseAmount(it.daily)
                    textViewDailyAverage.text = parseAmount(it.daily / it.dates.size)
                    val t = getLatest(it.dates)
                    textViewTotalToday.text = parseAmount(t?.amount ?: 0)
                    textViewDailyToday.text = parseAmount(t?.daily ?: 0)
                }
            }
        )
        requestQueue.add(request)
    }

    fun parseAmount(amount: Int) = String.format("%d.%d", amount / 100, amount % 100)
    fun <T> getLatest(list: List<T>): T? {
        return if (list.isEmpty()) null else list[list.size - 1]
    }
}
