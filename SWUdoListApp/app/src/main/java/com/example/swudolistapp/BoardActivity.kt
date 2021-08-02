package com.example.swudolistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.board_rv_item.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 게시판 액티비티
class BoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        var retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var addBoardService: MyAPI = retrofit.create(MyAPI::class.java)

        btn_board_edit
    }
}