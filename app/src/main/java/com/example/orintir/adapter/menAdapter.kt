package com.example.orintir.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.renderscript.RenderScript
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orintir.Database.ManModel
import com.example.orintir.MainActivity
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
            if (currentItem.status){statusView.text="Закрыта"
            statusView.setTextColor(Color.parseColor("#FF0000"))}

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

    // Обновление статуса (Его пока нет)

    private fun showStatusDialog(manModel: ManModel, context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Выберите статус")

        val options = arrayOf("Найден жив","Найден мертв")

        builder.setItems(options) { _, which ->
            val isFound = which == 0

            val statusText = if (isFound) {
                "Найден жив"
            } else {
                "Найден мертв"
            }

            manModel.status = true

            savePhotoAndShowStatus(manModel.imageData, statusText)
            Thread{
                MainActivity.db.ManDao.insertMan(manModel)
            }.start()
            onPersonStatusChangeListener.onPersonStatusChange(manModel, isFound)
        }

        builder.show()
    }
}
    private fun savePhotoAndShowStatus(imageData: ByteArray, statusText: String) {


        // Здесь вы должны реализовать логику сохранения фото и отображения статуса
        // Например, сохранить фото в базе данных и обновить статус в RecyclerView
        // Также, вы можете использовать onPersonStatusChangeListener.onPersonStatusChange,
        // чтобы уведомить остальные части кода о изменении статуса.
    }
