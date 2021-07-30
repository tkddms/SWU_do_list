package com.example.swudolistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView

class SelectSubjectActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_subject)


        //getSubjectList()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_my_change_subject -> Log.e("a", "change subject") // 과목 변경
            R.id.menu_my_logout -> Log.e("b", "logout") // 로그아웃
        }
        return super.onOptionsItemSelected(item)
    }

    // 선택한 과목 List 생성
    fun getSubjectList(){
        val subjectList = intent.getStringArrayListExtra("selectSubject")

        if(intent.hasExtra("selectSubject")){
            Log.e("SelectSubjectActivity", "첫 번째 데이터 :" + subjectList.get(0))
        }else{
            Log.e("SelectSubjectActivity", "가져온 데이터 없음!")
        }

//        for (sub in subjectList){
//            //val tv_subject: TextView = TextView(this)
//
//        }

    }
}