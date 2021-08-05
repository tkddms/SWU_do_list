package com.example.swudolistapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_board.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_to_do_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    var subjectName: String = ""
    var subjectCode: String = ""
    var subjectArr = arrayOf("JAVA프로그래밍기초", "C++프로그래밍기초", "자료구조")
    var subjectCodeArr = arrayOf("MT01044", "MT01043", "MT01019")

    var retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var addTDLService: MyAPI = retrofit.create(MyAPI::class.java)
    var addBoardService: MyAPI = retrofit.create(MyAPI::class.java)

    companion object{
        var todoList = ArrayList<ToDoListData>()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // 전체적인 게시판+미션 친구들 보이는 곳

        if (intent.hasExtra("subject_name")) {
            // 해당 게시글 과목명 얻기
            subjectName = intent.getStringExtra("subject_name")
            tv_main_subject_name.text = subjectName
            subjectCode = subjectCodeArr.get(subjectArr.indexOf(subjectName))
        } else {
            Toast.makeText(applicationContext, "해당 과목 data가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
        }

        // 미션 얻기
        addTDLService.getTodolist(subjectCode).enqueue(object : Callback<List<ToDoListData>> {
            override fun onResponse(
                call: Call<List<ToDoListData>>,
                response: Response<List<ToDoListData>>
            ) {
                var datas = response.body()
                ToDoListActivity.todoList.clear()
                if (datas != null) {
                    for (data in datas){
                        Log.e("post data", data.toString())
                        ToDoListActivity.todoList.add(ToDoListData(subjectCode, data.context, false))
                    }
                    setToDoListView()
                }
            }

            override fun onFailure(call: Call<List<ToDoListData>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        // 게시글 얻기
        addBoardService.getPost(subjectCode).enqueue(object : Callback<List<BoardData>>{
            override fun onResponse(
                call: Call<List<BoardData>>,
                response: Response<List<BoardData>>
            ) {
                var datas = response.body()
                BoardActivity.boardDataList.clear()
                if (datas != null) {
                    for (data in datas){
                        BoardActivity.boardDataList.add(0, BoardData(data.author, data.subject, data.title, data.context, data.created))
                    }
                    setBoardListView()
                    for (d in BoardActivity.boardDataList){
                        Log.e("boardData", d.toString())
                    }
                }

            }

            override fun onFailure(call: Call<List<BoardData>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        // to-do-list 더보기
        btn_more_tdl.setOnClickListener {
            val intent = Intent(this@MainActivity, ToDoListActivity::class.java)
            intent.putExtra("subject", subjectName)
            startActivity(intent)
        }

        // 게시판 더보기
        btn_more_post.setOnClickListener {
            val intent = Intent(this@MainActivity, BoardActivity::class.java)
            intent.putExtra("subject", subjectName)
            startActivity(intent)
        }
        
    }

    // To-Do-List RV 구성
    fun setToDoListView(){
        val mAdapter = ToDoListAdapter(this, ToDoListActivity.todoList)
        Log.e("todolist-setTodolistView", ToDoListActivity.todoList.toString())
        rv_main_todo.adapter = mAdapter

        val layout = LinearLayoutManager(this)
        rv_main_todo.layoutManager = layout
        rv_main_todo.setHasFixedSize(true)
    }

    // 게시글 RV 구성
    fun setBoardListView(){
        val mAdapter = BoardListAdapter(this, BoardActivity.boardDataList)
        rv_main_board.adapter = mAdapter

        val layout = LinearLayoutManager(this)
        rv_main_board.layoutManager = layout
        rv_main_board.setHasFixedSize(true)
    }
}