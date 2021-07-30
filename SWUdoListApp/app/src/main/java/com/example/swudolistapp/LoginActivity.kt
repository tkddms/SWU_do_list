package com.example.swudolistapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    var login: Login? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        retrofit 객체 build - 10.0.2.2는 에뮬레이터에서 서버와 접속 가능하도록 함.
        var retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        var loginService: LoginService = retrofit.create(LoginService::class.java)

        // 로그인 버튼 클릭 - 로그인
        btn_login.setOnClickListener {

            var t_id = et_id.text.toString()
            var t_pw = et_pw.text.toString()

            loginService.requestLogin(t_id, t_pw).enqueue(object : Callback<Login> {
                override fun onFailure(call: Call<Login>, t: Throwable) {
//                로그인 실패
                    Log.e("Login", t.message)
                    var dialog = AlertDialog.Builder(this@LoginActivity)
                    dialog.setTitle("Error")
                    dialog.setMessage("Response Fail")
                    dialog.show()

                }

                override fun onResponse(call: Call<Login>, response: Response<Login>) {
//                로그인 성공
                    login = response.body()
                    Log.d("Login", "msg: " + login?.msg)
                    Log.d("Login", "code: " + login?.code)
                    var dialog = AlertDialog.Builder(this@LoginActivity)
                    dialog.setTitle(login?.msg)
                    dialog.setMessage(login?.code)
                    dialog.show()

                    if(login?.code.equals("0000")){
                        val intent = Intent(this@LoginActivity, SelectSubjectActivity::class.java)
                        startActivity(intent)
                    }

                    if (login?.code.equals("1001")) {
                        Toast.makeText(applicationContext, "아이디 혹은 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show()
                    }

                }
            })
        }

        // 회원가입 tv 클릭 - 회원가입 화면 전환
        tv_register.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}