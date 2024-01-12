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
        }
    }


    private fun applyText(){
        val text = binding.editTextText.text.toString()

        // Загрузка изображения с помощью Picasso
        Picasso.get()
            .load(R.drawable.fewf)
            .into(imageView, object : com.squareup.picasso.Callback{
                override fun onSuccess() {
                    val bitmap = createBitmapFromView(imageView, text)

                    saveImageToGallery(bitmap)
                }

                override fun onError(e: Exception?) {
                    // Обработка ошибок при загрузке изображения
                }
            })
    }
    //Рисовка
    private fun createBitmapFromView(view: View, text: String):Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        val paint = android.graphics.Paint()
        paint.color = Color.BLACK
        paint.textSize = 40f

        val x = 5f
        val y = (view.height / 2).toFloat()

        canvas.drawText(text, x ,y, paint)

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