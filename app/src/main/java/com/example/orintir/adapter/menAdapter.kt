package com.example.orintir.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orintir.Database.ManModel
import com.example.orintir.R

class MenAdapter(private var dataList: List<ManModel>) :
    RecyclerView.Adapter<MenAdapter.MenViewHolder>() {

    class MenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImageView: ImageView = itemView.findViewById(R.id.photo_men)
        val nameTextView: TextView = itemView.findViewById(R.id.name_text)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.men_item, parent, false)
        return MenViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenViewHolder, position: Int) {
        val currentItem = dataList[position]
        with(holder) {

            nameTextView.text = currentItem.name

        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(newData: List<ManModel>) {
        dataList = newData
        notifyDataSetChanged()
    }
}