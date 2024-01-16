package com.example.orintir.screens

import android.content.ContentProviderOperation
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.createBitmap
import com.example.orintir.R
import com.example.orintir.databinding.FragmentCreateBinding
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.Exception

//
//Фрагмент для ввода данных т.е создание ориентировки
class CreateFragment : Fragment() {

    lateinit var binding: FragmentCreateBinding
    lateinit var imageView: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateBinding.inflate(layoutInflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageView = view.findViewById(R.id.imageView2)

        binding.button.setOnClickListener{
            applyText()
            Toast.makeText(getActivity(), "Изображение сохранени", Toast.LENGTH_SHORT).show()
        }
    }


    private fun applyText(){
        val text = binding.editTextText.text.toString()
        val text2 = binding.editTextText2.text.toString()
        val text3 = binding.editTextDate.text.toString()
        val text4 = binding.editTextText5.text.toString()
        val text5 = binding.editTextText6.text.toString()
        val text6 = binding.editTextText7.text.toString()

        //Загрузка изображения с помощью Picasso
        Picasso.get()
            .load(R.drawable.fewf)
            .into(imageView, object : com.squareup.picasso.Callback{
                override fun onSuccess() {
                    val bitmap = createBitmapFromView(imageView, text, text2, text3, text4, text5, text6)

                    saveImageToGallery(bitmap)
                }

                override fun onError(e: Exception?) {
                    // Обработка ошибок при загрузке изображения
                }
            })
    }

    //Рисовка
    private fun createBitmapFromView(view: View,
                                     text: String,
                                     text2: String,
                                     text3: String,
                                     text4: String,
                                     text5: String,
                                     text6: String
    ):Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        // Фио
        val paint = android.graphics.Paint()
        paint.color = Color.RED
        paint.textSize = 15f
        val x = 50f
        val y = 40f
        canvas.drawText(text, x ,y, paint)

        //Город
        val paint2 = android.graphics.Paint()
        paint2.color = Color.BLACK
        paint2.textSize = 10f
        val x2 = 50f
        val y2 = 60f
        canvas.drawText(text2, x2,  y2, paint2)

        // Дата рождения
        val paint3 = android.graphics.Paint()
        paint3.color = Color.BLACK
        paint3.textSize = 10f
        val x3 = 120f
        val y3 = 60f
        canvas.drawText("$text3 г.р.", x3,  y3, paint3)

        // При каких осбст.
        val paintObs = android.graphics.Paint()
        paintObs.color = Color.RED
        paintObs.textSize = 10f
        val xO = 50f
        val yO = 80f
        canvas.drawText("При каких обстоятельствах пропал(а): ", xO,  yO, paintObs)

        // Обстоятельства
        val paint4 = android.graphics.Paint()
        paint4.color = Color.BLACK
        paint4.textSize = 10f
        val x4 = 50f
        val y4 = 100f
        canvas.drawText(text4, x4,  y4, paint4)

        //Приметы
        val paint5 = android.graphics.Paint()
        paint4.color = Color.BLACK
        paint4.textSize = 10f
        val x5 = 50f
        val y5 = 100f
        canvas.drawText(text5, x5,  y5, paint5)

        //Одежда
        val paint6 = android.graphics.Paint()
        paint4.color = Color.BLACK
        paint4.textSize = 10f
        val x6 = 50f
        val y6 = 120f
        canvas.drawText(text6, x6,  y6, paint6)

        return bitmap
    }

    //Сохранения изображения
    private fun saveImageToGallery(bitmap: Bitmap) {
        val filename = "orintir.jpg"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, filename)
            put(MediaStore.Images.Media.DESCRIPTION, "Image with text")
            put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures") // Опционально: указание подпапки
        }

        val resolver = requireActivity().contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        try {
            if (uri != null) {
                val stream: OutputStream? = resolver.openOutputStream(uri)
                stream.use {
                    if (it != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}