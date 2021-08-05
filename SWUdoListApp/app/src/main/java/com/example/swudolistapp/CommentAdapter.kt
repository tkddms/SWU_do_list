package com.example.swudolistapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

// CommentAdapter - 댓글
class CommentAdapter(private val context: Context, private val dataList: ArrayList<CommentData>):
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    private val sharedManager: SharedManager by lazy { SharedManager(context) }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val tv_context = view.findViewById<TextView>(R.id.tv_comment_context)
        private val tv_created = view.findViewById<TextView>(R.id.tv_comment_date)
        private val tv_author = view.findViewById<TextView>(R.id.comment_nickname)

        fun bind(commentData: CommentData){
            tv_context.text = commentData.context
            tv_created.text = commentData.created
            if(commentData.author.equals(sharedManager.getCurrentUser().id)){
                tv_author.setTextColor(ContextCompat.getColor(context!!, R.color.coral_500))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.comment_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.apply {
            bind(item)
        }
    }

    // ClickListener
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}