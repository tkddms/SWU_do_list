package com.example.swudolistapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// pw 값이랑 pw_confirm 값이랑 같은지 확인하고 버튼 활성화시키기

class RegisterActivity : AppCompatActivity() {

    var isOk: Boolean = true
    var register: Register? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        var registerService: RegisterService = retrofit.create(RegisterService::class.java)

        var subjectArr = arrayOf("JAVA프로그래밍기초", "C++프로그래밍기초", "자료구조")
        var subjectStr = ""

        // 과목 선택 팝업 창
        btn_select_subject.setOnClickListener {
            var selectItems = ArrayList<Int>()
            var builder = AlertDialog.Builder(this)
            builder.setTitle("과목 선택")
            builder.setMultiChoiceItems(
                subjectArr,
                null,
                object: DialogInterface.OnMultiChoiceClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int, isChecked: Boolean) {
                        if(isChecked){
                            selectItems.contains(which)
                        }
                        else if(selectItems.contains(which)){
                            selectItems.remove(which)
                        }
                    }
                }
            )

//             여기 부분 이상하다 - 반응이 없어..
            var listener = object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    for(item in selectItems){
                        print(item)
                        subjectStr += subjectArr[item] + " "
                    }
                    Log.d("subject List", subjectStr)
                }
            }

            builder.setPositiveButton("확인", listener)
            builder.setNegativeButton("취소", null)
            builder.show()
        }

        btn_register.setOnClickListener{
            // 넘어갈 수 있도록 조건 검사
            canSubmitForm()

            var reg_id = et_register_id.text.toString()
            var reg_pw = et_register_pw.text.toString()
            var reg_email = et_register_email.text.toString()

            if(isOk){
                // 회원가입 가능 조건 충족 시 - 회원가입
                registerService.requestRegister(reg_id, reg_pw, reg_email, subjectStr).enqueue(object: Callback<Register>{
                    override fun onFailure(call: Call<Register>, t: Throwable) {
                        Log.e("SIGNIN" , t.message)
                        var dialog = AlertDialog.Builder(this@RegisterActivity)
                        dialog.setTitle("에러")
                        dialog.setMessage("호출실패했습니다.")
                        dialog.show()
                    }

                    override fun onResponse(call: Call<Register>, response: Response<Register>) {
                        register = response.body()
                        Log.d("SIGNIN", "msg: " + register?.msg)
                        Log.d("SIGNIN", "msg: " + register?.code)
                        var dialog = AlertDialog.Builder(this@RegisterActivity)
                        dialog.setTitle(register?.msg)
                        dialog.setMessage(register?.code)
                        dialog.show()
                    }
                })

                // 메인 화면으로 이동 - 이후 선택 화면으로 이동? 아니면 팝업으로 선택한 후에 이동하도록 할까?
                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun canSubmitForm(){
        if (et_register_id.text.toString().equals("")){
            // id 입력란이 비어 있을 때
            et_register_id.setError("아이디 입력 란은 필수로 입력하셔야 합니다.")
            isOk = false
        }
        if (et_register_pw.text.toString().equals("")){
            // pw 입력란이 비어 있을 때
            et_register_pw.setError("비밀번호 입력 란은 필수로 입력하셔야 합니다.")
            isOk = false
        }
        if (et_register_pw_confirm.text.toString().equals("")){
            // pw_confirm 입력란이 비어 있을 때
            et_register_pw_confirm.setError("비밀번호 확인 란은 필수로 입력하셔야 합니다.")
            isOk = false
        }
        if (et_register_email.text.toString().equals("")){
            // email 입력란이 비어 있을 때
            et_register_email.setError("이메일 입력 란은 필수로 입력하셔야 합니다.")
            isOk = false
        }

        if (!(et_register_pw.text.toString().equals(et_register_pw_confirm.text.toString()))){
            // pw != pw_confirm
            et_register_pw_confirm.setError("입력한 비밀번호와 비밀번호 확인이 일치하지 않습니다.")
            isOk = false
        }
    }
}