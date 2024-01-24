package com.example.orintir.screens

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.orintir.Database.ManDao
import com.example.orintir.Database.ManDatabase
import com.example.orintir.Database.ManModel
import com.example.orintir.MainActivity
import com.example.orintir.R
import com.example.orintir.databinding.FragmentCreateBinding
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream


//
//Фрагмент для ввода данных т.е создание ориентировки
class CreateFragment : Fragment() {

    lateinit var binding: FragmentCreateBinding
    lateinit var imageView: ImageView
    lateinit var selectedImageView: ImageView

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
        val selectImageButton = binding.addBt
        selectedImageView = binding.imageView

        binding.button.setOnClickListener{
            applyText()

            Toast.makeText(getActivity(), "Изображение сохранени", Toast.LENGTH_SHORT).show()
            abbDB()
        }

        selectImageButton.setOnClickListener{
            openGallery()
        }

    }

    private fun abbDB(){

        val text = binding.editTextText.text.toString()
        val text2 = binding.editTextText2.text.toString()
        val text3 = binding.editTextDate.text.toString()
        val text4 = binding.editTextText5.text.toString()
        val text5 = binding.editTextText6.text.toString()
        val text6 = binding.editTextText7.text.toString()

        val userSelectedImage: Bitmap? = (selectedImageView.drawable as? BitmapDrawable)?.bitmap
        val bitmap = createBitmapFromView(
            imageView,
            text,
            text2,
            text3,
            text4,
            text5,
            text6,
            userSelectedImage
        )

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()

        val Man = ManModel(0, text, text2, text3, text4,text5, text6, image)
        Thread{
            MainActivity.db.ManDao.insertMan(Man)
        }.start()

    }

    //Добавления фото
    private fun openGallery(){
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }
    companion object{
        const val PICK_IMAGE_REQUEST = 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data:Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == -1){
            val imageUri: Uri? = data?.data
            if (imageUri != null){
                Picasso.get().load(imageUri).into(selectedImageView)
            }
        }
    }




    private fun applyText(){
        val text = binding.editTextText.text.toString()
        val text2 = binding.editTextText2.text.toString()
        val text3 = binding.editTextDate.text.toString()
        val text4 = binding.editTextText5.text.toString()
        val text5 = binding.editTextText6.text.toString()
        val text6 = binding.editTextText7.text.toString()

        //Фото
        val userSelectedImage: Bitmap? = (selectedImageView.drawable as? BitmapDrawable)?.bitmap
        if (userSelectedImage != null){
            val bitmap = createBitmapFromView(imageView, text, text2, text3, text4, text5, text6, userSelectedImage)
            saveImageToGallery(bitmap)
        } else{
            Toast.makeText(requireContext(), "Выберите изображение", Toast.LENGTH_SHORT).show()
        }
        //Загрузка изображения с помощью Picasso
        Picasso.get()
            .load(R.drawable.fewf2)
            .into(imageView, object : com.squareup.picasso.Callback{
                override fun onSuccess() {
                    val bitmap = createBitmapFromView(
                        imageView,
                        text,
                        text2,
                        text3,
                        text4,
                        text5,
                        text6,
                        userSelectedImage
                    )

                    saveImageToGallery(bitmap)
                }

                override fun onError(e: Exception?) {
                    // Обработка ошибок при загрузке изображения
                }
            })
    }

    //Рисовка
    private fun createBitmapFromView(
        view: View,
        text: String,
        text2: String,
        text3: String,
        text4: String,
        text5: String,
        text6: String,
        userSelectedImage: Bitmap?
    ):Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        // Фио
        val paint = android.graphics.Paint()
        paint.color = Color.RED
        paint.textSize = 23f
        val x = 155f
        val y = 60f
        canvas.drawText(text, x ,y, paint)

        //Город
        val paint2 = android.graphics.Paint()
        paint2.color = Color.BLACK
        paint2.textSize = 20f
        val x2 = 155f
        val y2 = 90f
        canvas.drawText("$text2,", x2,  y2, paint2)

        // Дата рождения
        val paint3 = android.graphics.Paint()
        paint3.color = Color.BLACK
        paint3.textSize = 20f
        val x3 = 255f
        val y3 = 90f
        canvas.drawText("$text3 г.р.", x3,  y3, paint3)

        // При каких осбст.
        val paintObs = android.graphics.Paint()
        paintObs.color = Color.RED
        paintObs.textSize = 15f
        val xO = 50f
        val yO = 200f
        canvas.drawText("При каких обстоятельствах пропал(а): ", xO,  yO, paintObs)

        // Обстоятельства
        val paint4 = android.graphics.Paint()
        paint4.color = Color.BLACK
        paint4.textSize = 10f
        val x4 = 50f
        val y4 = 215f
        canvas.drawText(text4, x4,  y4, paint4)

        //Приметы осн.
        val paintPrim = android.graphics.Paint()
        paintPrim.color = Color.RED
        paintPrim.textSize = 15f
        val xP = 50f
        val yP = 240f
        canvas.drawText("Основные приметы: ", xP, yP, paintPrim)

        //Приметы
        val paint5 = android.graphics.Paint()
        paint4.color = Color.BLACK
        paint4.textSize = 10f
        val x5 = 50f
        val y5 = 255f
        canvas.drawText(text5, x5,  y5, paint5)

        //Одежда осн.
        val paintCloth = android.graphics.Paint()
        paintCloth.color = Color.RED
        paintCloth.textSize = 15f
        val xC = 50f
        val yC = 280f
        canvas.drawText("Одежда: ", xC, yC, paintCloth)

        //Одежда
        val paint6 = android.graphics.Paint()
        paint4.color = Color.BLACK
        paint4.textSize = 10f
        val x6 = 50f
        val y6 = 295f
        canvas.drawText(text6, x6,  y6, paint6)

        //Текст
        val paintText = android.graphics.Paint()
        paintText.color = Color.BLACK
        paintText.textSize = 20f
        val xT = 27f
        val yT = 350f
        canvas.drawText("Просим сообщить любую информацию по тел: ", xT, yT, paintText)

        //Номера
        val paintNumber = android.graphics.Paint()
        paintNumber.color = Color.BLACK
        paintNumber.textSize = 20f
        val xN = 130f
        val yN = 375f
        canvas.drawText("8(924)991-11-62 или 112", xN, yN, paintNumber)

        //Фото
        userSelectedImage?.let {
            val scaledBitmap = Bitmap.createScaledBitmap(it, 127, 147, false)
            canvas.drawBitmap(scaledBitmap, 20f, 19f, null)
        }

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
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}