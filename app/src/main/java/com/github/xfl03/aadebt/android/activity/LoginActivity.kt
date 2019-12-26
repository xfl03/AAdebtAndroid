package com.github.xfl03.aadebt.android.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.github.xfl03.aadebt.R
import com.github.xfl03.aadebt.android.util.Constant
import com.github.xfl03.aadebt.android.util.GsonRequest
import com.github.xfl03.aadebt.json.CommonResponse
import com.github.xfl03.aadebt.json.auth.AuthLoginRequest
import com.github.xfl03.aadebt.json.auth.AuthLoginResponse
import com.github.xfl03.aadebt.json.auth.AuthRegRequest
import com.github.xfl03.aadebt.json.auth.AuthRegResponse
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue
    private lateinit var sp: SharedPreferences

    private var login = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        requestQueue = Volley.newRequestQueue(this)
        sp = getSharedPreferences("login", Context.MODE_PRIVATE)

        buttonLogin.setOnClickListener {
            if (login) {
                if (check(
                        "LOGIN",
                        editTextEmail.text.toString(),
                        editTextPassword.text.toString()
                    )
                ) {
                    save(
                        editTextUsername.text.toString(),
                        editTextEmail.text.toString(),
                        editTextPassword.text.toString()
                    )
                    login(
                        editTextEmail.text.toString(),
                        editTextPassword.text.toString()
                    )
                } else {
                    Constant.toast(this, "请填写内容")
                }
            } else {
                login = !login;
                editTextUsername.visibility = View.INVISIBLE
            }
        }
        buttonRegister.setOnClickListener {
            if (!login) {
                if (check(
                        editTextUsername.text.toString(),
                        editTextEmail.text.toString(),
                        editTextPassword.text.toString()
                    )
                ) {
                    save(
                        editTextUsername.text.toString(),
                        editTextEmail.text.toString(),
                        editTextPassword.text.toString()
                    )
                    register(
                        editTextUsername.text.toString(),
                        editTextEmail.text.toString(),
                        editTextPassword.text.toString()
                    )
                } else {
                    Constant.toast(this, "请填写内容")
                }
            } else {
                login = !login;
                editTextUsername.visibility = View.VISIBLE
            }
        }
        load()
    }

    fun check(name: String?, email: String?, password: String?): Boolean {
        if (name.isNullOrBlank()) {
            Constant.toast(this, "请输入昵称")
            return false
        }
        if (email.isNullOrBlank()) {
            Constant.toast(this, "请输入邮箱")
            return false
        }
        if (password.isNullOrBlank()) {
            Constant.toast(this, "请输入密码")
            return false
        }
        return true
    }

    fun save(name: String?, email: String?, password: String?) {
        sp.edit()
            .putString("name", name)
            .putString("email", email)
            .putString("password", password)
            .apply()
    }

    fun load() {
        editTextUsername.setText(sp.getString("name", ""))
        editTextEmail.setText(sp.getString("email", ""))
        editTextPassword.setText(sp.getString("password", ""))
    }

    private fun login(email: String, password: String) {
        val request = GsonRequest(
            AuthLoginResponse::class.java,
            Request.Method.POST,
            Constant.BASE_URL + "/api/auth/login",
            AuthLoginRequest(email, password),
            Response.Listener {
                if (it is AuthLoginResponse) {
                    onSuccess(it.token)
                }
            },
            Response.ErrorListener {
                Constant.log("FAILED: $it")
                Constant.toast(this, "登录失败")
            }
        )
        requestQueue.add(request)
    }

    private fun register(name: String, email: String, password: String) {
        val request = GsonRequest(
            AuthRegResponse::class.java,
            Request.Method.POST,
            Constant.BASE_URL + "/api/auth/reg",
            AuthRegRequest(name, email, password),
            Response.Listener {
                if (it is AuthRegResponse) {
                    onSuccess(it.token)
                } else if (it is CommonResponse) {
                    Constant.log("FAILED: " + it.msg)
                    Constant.toast(this, "邮箱已被使用")
                }
            }
        )
        requestQueue.add(request)
    }

    private fun onSuccess(token: String) {
        //Constant.log("Success: $token")
        val intent = Intent()
        intent.putExtra("token", token)
        setResult(Constant.OP_REFRESH or Constant.OP_REFRESH_TOKEN, intent)
        finish()
    }
}
