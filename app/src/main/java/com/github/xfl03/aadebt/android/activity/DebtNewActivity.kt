package com.github.xfl03.aadebt.android.activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.github.xfl03.aadebt.R
import com.github.xfl03.aadebt.android.util.Constant
import com.github.xfl03.aadebt.android.util.GsonRequest
import com.github.xfl03.aadebt.json.CommonResponse
import com.github.xfl03.aadebt.json.debt.DebtAddRequest
import kotlinx.android.synthetic.main.activity_debt_new.*
import java.sql.Date
import kotlin.math.roundToInt

class DebtNewActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue
    private lateinit var sp: SharedPreferences

    var id = 0
    var name = ""
    var selected = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debt_new)

        requestQueue = Volley.newRequestQueue(this)
        sp = getSharedPreferences("debt", Context.MODE_PRIVATE)

        if (intent != null) {
            id = intent.getIntExtra("id", 0)
            if (intent.hasExtra("name")) {
                name = intent.getStringExtra("name")
            }
        }
        if (id == 0) {
            id = sp.getInt("id", 0)
            name = sp.getString("name", "")!!
        }
        Constant.log(id.toString())

        title = name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val apt = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            Constant.TYPES
        )
        spinnerDebtType.adapter = apt
        spinnerDebtType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //Constant.log(Constant.TYPES[position])
                if (selected != -1 && editTextDebtName.text.isNullOrBlank()) {
                    editTextDebtName.setText(Constant.TYPES[position])
                }
                selected = position
            }
        }

        buttonDebtAdd.setOnClickListener {
            if (check(
                    editTextDebtName.text.toString(),
                    selected,
                    editTextDebtAmount.text.toString(),
                    editTextDebtDate.text.toString()
                )
            ) {
                sp.edit().putInt("id", id).putString("name", name).apply()
                addDebt(
                    editTextDebtName.text.toString(),
                    selected,
                    editTextDebtAmount.text.toString().toDouble(),
                    //Constant.DATE_FORMAT.parse(editTextDebtDate.text.toString()),
                    Date.valueOf(editTextDebtDate.text.toString()),
                    switchDebtSpecial.isChecked
                )
            } else {
                Constant.toast(this, "请填写内容")
            }
        }

        editTextDebtDate.setText(Constant.DATE_FORMAT.format(Date(System.currentTimeMillis())))
    }

    private fun addDebt(name: String, type: Int, amount: Double, date: Date, special: Boolean) {
        //Constant.log(id.toString())
        val request = GsonRequest(
            CommonResponse::class.java,
            Request.Method.POST,
            Constant.BASE_URL + "/api/debt/add",
            DebtAddRequest(id, name, parseAmount(amount), type, date, special),
            Response.Listener {
                if (it is CommonResponse) {
                    setResult(Constant.OP_REFRESH)
                    finish()
                }
            }
        )
        requestQueue.add(request)
    }

    fun parseAmount(amount: Double): Int = (amount * 100).roundToInt()

    private fun check(name: String?, type: Int, amount: String?, date: String?): Boolean {
        if (name.isNullOrBlank() || amount.isNullOrBlank() || date.isNullOrBlank()) {
            return false
        }
        if (type < 0 || type >= Constant.TYPES.size) {
            return false
        }

        try {
            val t = amount.toDouble()
            if (t <= 0) {
                return false
            }
            Date.valueOf(editTextDebtDate.text.toString()) ?: return false
        } catch (e: Exception) {
            return false
        }
        return true

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
