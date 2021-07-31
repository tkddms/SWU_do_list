package com.example.swudolistapp

import android.content.Intent
import android.content.SharedPreferences
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

    var login: PostItem? = null
    private val sharedManager: SharedManager by lazy { SharedManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        retrofit 객체 build - 10.0.2.2는 에뮬레이터에서 서버와 접속 가능하도록 함.
        var retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        var loginService: MyAPI = retrofit.create(MyAPI::class.java)

        // 로그인 버튼 클릭 - 로그인
        btn_login.setOnClickListener {

            var t_id = et_id.text.toString()
            var t_pw = et_pw.text.toString()

            loginService.requestLogin(t_id, t_pw).enqueue(object : Callback<PostItem> {
                override fun onFailure(call: Call<PostItem>, t: Throwable) {
//                로그인 실패
                    Log.e("Login", t.message)
                }

                override fun onResponse(call: Call<PostItem>, response: Response<PostItem>) {
//                로그인 성공
                    login = response.body()
                    Log.d("Login", "msg: " + login?.msg)
                    Log.d("Login", "code: " + login?.code)

                    if(login?.code.equals("0000")){

                        // SharedPreference - 로그인 정보 및 subject 내용까지 얻어와야 함. - GET 필요

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