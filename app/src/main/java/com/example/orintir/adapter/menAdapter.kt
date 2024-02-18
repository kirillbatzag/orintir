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
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orintir.Database.ManModel
import com.example.orintir.MainActivity
import com.example.orintir.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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



    private fun showStatusDialog(manModel: ManModel, context: Context, view: View) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Выберите статус")

        val options = arrayOf("Найден жив", "Найден мертв")

        builder.setItems(options) { _, which ->
            val isFound = which == 0

            val statusText = if (isFound) {
                "Найден жив"
            } else {
                "Найден погиб"
            }

            CoroutineScope(Dispatchers.IO).launch {
                val processedImageData = savePhotoAndShowStatus(manModel.imageData, statusText)
                withContext(Dispatchers.Main) {
                    manModel.status = true
                    manModel.imageData = processedImageData
                    notifyDataSetChanged()
                    onPersonStatusChangeListener.onPersonStatusChange(manModel, isFound)
                    Thread{
                        MainActivity.db.ManDao.insertMan(manModel)
                    }.start()
                }
            }
        }

        builder.show()
    }

}
    fun savePhotoAndShowStatus(imageData: ByteArray, statusText: String):ByteArray {

        val b = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
        var bitmap = b.copy((Bitmap.Config.ARGB_8888) , true)
        bitmap = applyContrastAndGrayscale(bitmap)
        val canvas = Canvas(bitmap)
        val paint = android.graphics.Paint()

        // текст найден
        paint.color = Color.RED
        paint.textSize = 80f
        val x = 270f
        val y = 950f
        canvas.drawText(statusText, x ,y, paint)

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

        return stream.toByteArray()
    }


    // Контраст
    fun applyContrastAndGrayscale(src: Bitmap): Bitmap {
        val width = src.width
        val height = src.height
        val bmOut = Bitmap.createBitmap(width, height, src.config)

        val pixels = IntArray(width * height)
        src.getPixels(pixels, 0, width, 0, 0, width, height)

        val contrastFactor = 1.5f // Попробуйте разные значения для контраста
        val intercept = 128 * (1 - contrastFactor)

        for (i in pixels.indices) {
            val color = pixels[i]
            val r = Color.red(color)
            val g = Color.green(color)
            val b = Color.blue(color)

            val red = applyContrast(r, contrastFactor, intercept)
            val green = applyContrast(g, contrastFactor, intercept)
            val blue = applyContrast(b, contrastFactor, intercept)

            val gray = (0.299 * red + 0.587 * green + 0.114 * blue).toInt()
            pixels[i] = Color.rgb(gray, gray, gray)
        }

        bmOut.setPixels(pixels, 0, width, 0, 0, width, height)

        return bmOut
    }


    private fun applyContrast(colorComponent: Int, contrastFactor: Float, intercept: Float): Int {
        var color = (colorComponent * contrastFactor + intercept).toInt()
        if (color < 0) {
            color = 0
        } else if (color > 255) {
            color = 255
        }
        return color
    }


