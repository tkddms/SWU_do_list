package com.example.swudolistapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.activity_to_do_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// ToDoList 액티비티
class ToDoListActivity : AppCompatActivity() {

    var subjectArr = arrayOf("JAVA프로그래밍기초", "C++프로그래밍기초", "자료구조")
    var subjectCodeArr = arrayOf("MT01044", "MT01043", "MT01019")

    var subjectCode: String = ""
    lateinit var subjectPut: String

    companion object{
        var todoList = arrayListOf<ToDoListData>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_list)

        var retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var addTDLService: MyAPI = retrofit.create(MyAPI::class.java)
        var updateChecked: MyAPI = retrofit.create(MyAPI::class.java)


        if (intent.hasExtra("subject")) {
            // 해당 게시글 과목코드 얻기
            subjectPut = intent.getStringExtra("subject")
            subjectCode = subjectCodeArr.get(subjectArr.indexOf(subjectPut))
        } else {
            Toast.makeText(applicationContext, "error", Toast.LENGTH_SHORT).show()
        }

        // todolist 읽기
        addTDLService.getTodolist(subjectCode).enqueue(object : Callback<List<ToDoListData>>{
            override fun onResponse(
                call: Call<List<ToDoListData>>,
                response: Response<List<ToDoListData>>
            ) {
                var datas = response.body()
                todoList.clear()
                if (datas != null) {
                    for (data in datas){
                        todoList.add(ToDoListData(subjectCode, data.context, false))
                    }
                    setToDoListView()
                }
            }

            override fun onFailure(call: Call<List<ToDoListData>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        // todolist 추가
        btn_add_tdl.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.to_do_list_dialog_item, null)
            val dialogText = dialogView.findViewById<EditText>(R.id.tdl_dialog_context)

            builder.setView(dialogView)
                .setPositiveButton("등록"){ dialogInterface, i ->
                    addTDLService.addTodolist(subjectCode, dialogText.text.toString()).enqueue(object: Callback<PostItem>{
                        override fun onResponse(
                            call: Call<PostItem>,
                            response: Response<PostItem>
                        ) {
                            todoList.add(ToDoListData(subjectCode, dialogText.text.toString(), false))
                            setToDoListView()
                        }

                        override fun onFailure(call: Call<PostItem>, t: Throwable) {
                            t.printStackTrace()
                        }

                    })
                }
                .setNegativeButton("취소") {dialogInterface, i ->
                    // 아무 액션 없음
                }
                .show()
        }

    }

    // 뒤로 가기 눌렀을 때
    override fun onBackPressed() {
        todoList.clear()
        super.onBackPressed()
        val intent = Intent(this@ToDoListActivity, MainActivity::class.java )
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("subject_name",subjectPut)
        startActivity(intent)
        finish()
    }

    // List 만들기

    fun setToDoListView(){
        val mAdapter = ToDoListAdapter(this, todoList)
        rv_tdl.adapter = mAdapter

        val layout = LinearLayoutManager(this)
        rv_tdl.layoutManager = layout
        rv_tdl.setHasFixedSize(true)
    }
}