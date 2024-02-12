package com.example.orintir.screens

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.orintir.Database.ManDao
import com.example.orintir.Database.ManDatabase
import com.example.orintir.Database.ManModel
import com.example.orintir.MainActivity
import com.example.orintir.R
import com.example.orintir.databinding.FragmentCreateBinding
import com.example.orintir.mvvm.MyViewModel
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
    lateinit var vm: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateBinding.inflate(layoutInflater, container, false)
        return (binding.root)

        vm = ViewModelProvider(this).get(MyViewModel::class.java)
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

        // Кнопка для автоматического ввода данных
        binding.buttonTest.setOnClickListener(){
            binding.editTextText.setText("Иванов Иван Иванович")
            binding.editTextText2.setText("Санкт-Петербург")
            binding.editTextDate.setText("1980")
            binding.editTextText5.setText("Он выпил алкоголя и отправился в лес за грибами, к тому же еще шёл дождь")
            binding.editTextText6.setText("Седой, но не совсем. Высокий, но не очень. Очень много ворчит и может пахнуть перегаром, есть шрам на лице")
            binding.editTextText7.setText("Старый пуховик, шапка ушанка, сапоги, портфель жёлтый, варешки, очки.")
        }

    }

    //процедура создает запись в бд
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
        //заполнение объекта и внесение его в бд
        val Man = ManModel(0, text, text2, text3, text4,text5, text6, image, false)
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
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        // Определяем максимальное количество символов в каждой строке
        val maxCharsPerLine = 42

        // Фио
        val paint = Paint().apply {
            color = Color.RED
            textSize = 23f
        }
        val x = 525f
        var y = 365f
        drawText(canvas, text, paint, x, y, maxCharsPerLine)

        // Город
        val paint2 = Paint().apply {
            color = Color.BLACK
            textSize = 20f
        }
        y += 30f // Увеличиваем y на высоту предыдущей строки плюс некоторый отступ
        drawText(canvas, "Г.$text2", paint2, x, y, maxCharsPerLine)

        // Дата рождения
        val paint3 = Paint().apply {
            color = Color.BLACK
            textSize = 20f
        }
        y += 30f // Увеличиваем y на высоту предыдущей строки плюс некоторый отступ
        drawText(canvas, "$text3 г.р.", paint3, x, y, maxCharsPerLine)

        // При каких обстоятельствах
        val paintObs = Paint().apply {
            color = Color.RED
            textSize = 20f
        }
        y += 70f // Увеличиваем y на высоту предыдущей строки плюс некоторый отступ
        drawText(canvas, "При каких обстоятельствах пропал(а): ", paintObs, x, y, maxCharsPerLine)

        // Обстоятельства
        val paint4 = Paint().apply {
            color = Color.BLACK
            textSize = 20f
        }
        y += 20f // Увеличиваем y на высоту предыдущей строки плюс некоторый отступ
        drawText(canvas, text4, paint4, x, y, maxCharsPerLine)

        // Приметы основные
        val paintPrim = Paint().apply {
            color = Color.RED
            textSize = 20f
        }
        y += 90f // Увеличиваем y на высоту предыдущей строки плюс некоторый отступ
        drawText(canvas, "Основные приметы: ", paintPrim, x, y, maxCharsPerLine)

        // Приметы
        val paint5 = Paint().apply {
            color = Color.BLACK
            textSize = 20f
        }
        y += 20f // Увеличиваем y на высоту предыдущей строки плюс некоторый отступ
        drawText(canvas, text5, paint5, x, y, maxCharsPerLine)

        // Одежда основная
        val paintCloth = Paint().apply {
            color = Color.RED
            textSize = 20f
        }
        y += 100f // Увеличиваем y на высоту предыдущей строки плюс некоторый отступ
        drawText(canvas, "Одежда: ", paintCloth, x, y, maxCharsPerLine)

        // Одежда
        val paint6 = Paint().apply {
            color = Color.BLACK
            textSize = 20f
        }
        y += 20f // Увеличиваем y на высоту предыдущей строки плюс некоторый отступ
        drawText(canvas, text6, paint6, x, y, maxCharsPerLine)

        // Фото
        userSelectedImage?.let {
            val scaledBitmap = Bitmap.createScaledBitmap(it, 415, 506, false)
            canvas.drawBitmap(scaledBitmap, 104f, 352f, null)
        }

        return bitmap
    }

    private fun drawText(canvas: Canvas, text: String, paint: Paint, x: Float, y: Float, maxCharsPerLine: Int) {
        var remainingText = text
        var currentY = y
        while (remainingText.isNotEmpty()) {
            val charsToDraw = if (remainingText.length > maxCharsPerLine) {
                val cutoff = remainingText.lastIndexOf(' ', maxCharsPerLine)
                if (cutoff != -1) {
                    remainingText.substring(0, cutoff)
                } else {
                    remainingText.substring(0, maxCharsPerLine)
                }
            } else {
                remainingText
            }
            canvas.drawText(charsToDraw, x, currentY, paint)
            remainingText = remainingText.substring(charsToDraw.length).trimStart()
            currentY += paint.textSize
        }
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