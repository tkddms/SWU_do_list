package com.example.swudolistapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

class ToDoListAdapter(private val context: Context, private val dataList: ArrayList<ToDoListData>):
    RecyclerView.Adapter<ToDoListAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(v: View, data: ToDoListData, pos: Int)
    }

    private var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val tdlCheckBox = view.findViewById<CheckBox>(R.id.tdl_cb_rv)

        fun bind(toDoListData: ToDoListData){
            tdlCheckBox.text = toDoListData.context

            val pos = adapterPosition
            if(pos!=RecyclerView.NO_POSITION){
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, toDoListData, pos)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.to_do_list_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

}
