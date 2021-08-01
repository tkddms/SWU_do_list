package com.example.swudolistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // 전체적인 게시판+미션 친구들 보이는 곳

        if (intent.hasExtra("subject_name")) {
            textView.text = intent.getStringExtra("subject_name")



        } else {
            Toast.makeText(applicationContext, "해당 과목 data가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
        
    }
}