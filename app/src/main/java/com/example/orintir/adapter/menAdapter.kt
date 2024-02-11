package com.example.orintir.adapter

import android.R.attr.height
import android.R.attr.width
import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orintir.Database.ManModel
import com.example.orintir.MainActivity
import com.example.orintir.R
import java.io.ByteArrayOutputStream


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
            statusView.setTextColor(Color.parseColor("#FF0000")) }

            val bitmap = BitmapFactory.decodeByteArray(currentItem.imageData, 0, currentItem.imageData.size)
            photoImageView.setImageBitmap(bitmap)

            trashView.setOnClickListener{
                onDeleteClickListener.onDeleteClick(currentItem)
            }

            statusView.setOnClickListener{
                showStatusDialog(currentItem, itemView.context,photoImageView)

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



    private fun showStatusDialog(manModel: ManModel, context: Context, view: View){
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

            manModel.imageData = savePhotoAndShowStatus(manModel.imageData, statusText)
            Thread{
                MainActivity.db.ManDao.insertMan(manModel)
            }.start()
            onPersonStatusChangeListener.onPersonStatusChange(manModel, isFound)
        }

        builder.show()
    }
}
    public fun savePhotoAndShowStatus(imageData: ByteArray, statusText: String):ByteArray {



        val b = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
        var bitmap = b.copy((Bitmap.Config.ARGB_8888) , true)
        val canvas = Canvas(bitmap)
        val paint = android.graphics.Paint()
        paint.color = Color.RED
        paint.textSize = 23f
        val x = bitmap.width/2f
        val y = bitmap.height/2f
        canvas.drawText(statusText, x ,y, paint)
        val stream = ByteArrayOutputStream()
        bitmap = subColor(bitmap)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

        return stream.toByteArray()
    }

fun subColor(src:Bitmap): Bitmap? {
    val output = Bitmap.createScaledBitmap(src, src.width,src.height, true )
    for (x in 0 until output.width) for (y in 0 until output.height) {
        val pixel = output.getPixel(x, y)

        val r: Int = pixel shr 16 and 0xff
        val g: Int = pixel shr 8 and 0xff
        val b: Int = pixel shr 0 and 0xff
        val Y = 0.2126*r + 0.7152*g + 0.0722*b

        if (Y < 128) {
            output.setPixel(x, y, Color.BLACK)
        }else{
            output.setPixel(x, y, Color.WHITE)
        }

    }
    return output
}
