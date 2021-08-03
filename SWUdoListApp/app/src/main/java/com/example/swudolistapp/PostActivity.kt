package com.example.swudolistapp

import android.content.DialogInterface
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.comment_rv_item.*
import org.w3c.dom.Comment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostActivity : AppCompatActivity() {

    lateinit var postItem: BoardData

    var subjectArr = arrayOf("JAVA프로그래밍기초", "C++프로그래밍기초", "자료구조")
    var subjectCode = arrayOf("MT01044", "MT01043", "MT01019")

    // sharedPreference
    private val sharedManager: SharedManager by lazy { SharedManager(this) }

    companion object{
        var commentList = arrayListOf<CommentData>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        var retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var addCommentService: MyAPI = retrofit.create(MyAPI::class.java)
        var deletePostService: MyAPI = retrofit.create(MyAPI::class.java)
        var getCommentService: MyAPI = retrofit.create(MyAPI::class.java)
        var deleteCommentService: MyAPI = retrofit.create(MyAPI::class.java)

        if(intent.hasExtra("post")){
            postItem = intent.getParcelableExtra<BoardData>("post")

            tv_post_title.text = postItem.title
            tv_post_context.text = postItem.context
            tv_post_date.text = postItem.created

            if(postItem.author.equals(sharedManager.getCurrentUser().id)){
                btn_delete_post.visibility = View.VISIBLE
            }else{
                btn_delete_post.visibility = View.GONE
            }
        }

        val mAdapter = CommentAdapter(this, commentList)
        mAdapter.setItemClickListener(object : CommentAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                val item = commentList[position]
                if(sharedManager.getCurrentUser().id.equals(item.author)) {
                    val builder = AlertDialog.Builder(this@PostActivity)
                    builder.setMessage("정말 삭제하시겠습니까?")

                    builder
                        .setPositiveButton("확인") { dialogInterface: DialogInterface?, i: Int ->
                        deletePostService.deleteComment(
                            item.author,
                            postItem.title,
                            postItem.subject,
                            item.context,
                            item.created
                        ).enqueue(object : Callback<PostItem> {
                            override fun onResponse(
                                call: Call<PostItem>,
                                response: Response<PostItem>
                            ) {
                                var res = response.body()
                                Log.e("code", res?.code)
                                Log.e("msg", res?.msg)

                                commentList.remove(CommentData(item.author, item.context, item.created))

                                for(list in commentList){
                                    Log.e("commentList", list.toString())
                                }

                                setCommentListView(mAdapter)
                            }

                            override fun onFailure(call: Call<PostItem>, t: Throwable) {
                                t.printStackTrace()
                            }
                        })
                    }
                        .setNegativeButton("취소") { dialogInterface: DialogInterface?, i: Int ->
                        // 수행하는 동작 없음
                    }
                    builder.show()
                }

            }
        })
        rv_comment.adapter = mAdapter


        // 댓글 표시
        getCommentService.getComment(sharedManager.getCurrentUser().id.toString(), postItem.title, postItem.subject).enqueue(object : Callback<List<CommentData>>{
            override fun onResponse(
                call: Call<List<CommentData>>,
                response: Response<List<CommentData>>
            ) {
                var datas = response.body()
                Log.e("datas", datas.toString())
                if (datas != null) {
                    commentList.clear()
                    for (data in datas){
                        if(!commentList.contains(data)) {
                            commentList.add(CommentData(data.author, data.context, data.created))
                        }
                    }
                    for (list in commentList){
                        Log.e("commentList", list.toString())
                    }
                }
                setCommentListView(mAdapter)
            }

            override fun onFailure(call: Call<List<CommentData>>, t: Throwable) {
                t.printStackTrace()
            }

        })

        // 게시글 삭제
        btn_delete_post.setOnClickListener {
            // 팝업 메세지 - ok 시 삭제

            val builder = AlertDialog.Builder(this)
            builder.setMessage("정말 삭제하시겠습니까?")

            builder.setPositiveButton("확인"){ dialogInterface: DialogInterface?, i: Int ->
                deletePostService.deletePost(postItem.author, postItem.title, postItem.subject).enqueue(object : Callback<PostItem>{
                    override fun onResponse(call: Call<PostItem>, response: Response<PostItem>) {
                        // 이전 화면으로 돌아감
                        val intent=Intent(this@PostActivity, BoardActivity::class.java)
                        intent.putExtra("subject", subjectArr.get(subjectCode.indexOf(postItem.subject)))
                        intent.putExtra("post", postItem)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        }

                    override fun onFailure(call: Call<PostItem>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }
            builder.setNegativeButton("취소"){ dialogInterface: DialogInterface?, i: Int ->
                // 수행하는 동작 없음
            }
            builder.show()

        }

        // 댓글 추가
        btn_comment_add.setOnClickListener {
            var author = sharedManager.getCurrentUser().id.toString()
            var comment_context = et_comment.text.toString()
            addCommentService.addComment(author, comment_context, postItem.title, postItem.subject ).enqueue(object : Callback<EditItem>{
                override fun onResponse(call: Call<EditItem>, response: Response<EditItem>) {
                    commentList.add(CommentData(author, comment_context, response.body()?.created.toString()))
                    setCommentListView(mAdapter)
                }

                override fun onFailure(call: Call<EditItem>, t: Throwable) {
                    t.printStackTrace()
                }

            })
        }


    }

    fun setCommentListView(mAdapter: CommentAdapter){
        rv_comment.adapter = mAdapter

        val layout = LinearLayoutManager(this)
        rv_comment.layoutManager = layout
        rv_comment.setHasFixedSize(true)


    }
}