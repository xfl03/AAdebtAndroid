package com.github.xfl03.aadebt.android.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.xfl03.aadebt.R
import com.github.xfl03.aadebt.json.aa.DebtDetailInfo
import com.github.xfl03.aadebt.json.aa.GroupInfo

class BasicListAdapter<T>(private val listener: (T) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        fun <T> newDataList() = ArrayList<T>()
    }

    val data = newDataList<T>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.basic_list, parent, false)
        return BasicViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val d = data[position]
        if (d is GroupInfo) {
            holder.itemView
                .findViewById<TextView>(R.id.textViewName).text = d.name
            holder.itemView
                .findViewById<TextView>(R.id.textViewInfo).text = "普通记账"
        } else if (d is DebtDetailInfo) {
            holder.itemView
                .findViewById<TextView>(R.id.textViewName).text = d.name
            holder.itemView
                .findViewById<TextView>(R.id.textViewInfo).text = "${d.amount}"
        }
        holder.itemView.setOnClickListener {
            onClick(position)
        }
    }

    fun update(newData: List<T>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    fun onClick(pos: Int) {
        listener.invoke(data[pos])
    }
}