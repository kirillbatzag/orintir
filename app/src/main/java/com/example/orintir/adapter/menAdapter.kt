package com.example.orintir.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orintir.Database.ManModel
import com.example.orintir.R

class MenAdapter(
    private var dataList: List<ManModel>,
    private val onDeleteClickListener: OnDeleteClickListener,
    private val onPersonStatusChangeListener: OnPersonStatusChangeListener
) : RecyclerView.Adapter<MenAdapter.MenViewHolder>() {

    interface OnDeleteClickListener {
        fun onDeleteClick(manModel: ManModel)
    }

    interface OnPersonStatusChangeListener {
        fun onPersonStatusChange(manModel: ManModel, isFound: Boolean)
    }

    class MenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImageView: ImageView = itemView.findViewById(R.id.photo_men)
        val nameTextView: TextView = itemView.findViewById(R.id.name_text)
        val trashView: ImageView = itemView.findViewById(R.id.trash)
        val statusView: TextView = itemView.findViewById(R.id.status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.men_item, parent, false)
        return MenViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenViewHolder, position: Int) {
        val currentItem = dataList[position]
        with(holder) {
            nameTextView.text = currentItem.name

            val bitmap = BitmapFactory.decodeByteArray(currentItem.imageData, 0, currentItem.imageData.size)
            photoImageView.setImageBitmap(bitmap)

            trashView.setOnClickListener{
                onDeleteClickListener.onDeleteClick(currentItem)
            }

            statusView.setOnClickListener{
                showStatusDialog(currentItem, itemView.context)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(newData: List<ManModel>) {
        dataList = newData
        notifyDataSetChanged()
    }

    private fun showStatusDialog(manModel: ManModel, context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Выберите статус")

        val options = arrayOf("Найден жив","Найден мертв")

        builder.setItems(options){_, which ->
            val isFound = which == 0
            onPersonStatusChangeListener.onPersonStatusChange(manModel, isFound)
        }
        builder.show()
    }

}
