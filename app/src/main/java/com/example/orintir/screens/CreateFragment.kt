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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.orintir.Database.ManDao
import com.example.orintir.Database.ManDatabase
import com.example.orintir.Database.ManModel
import com.example.orintir.R
import com.example.orintir.databinding.FragmentCreateBinding
import com.example.orintir.mvvm.MyViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.Exception


//
//Фрагмент для ввода данных т.е создание ориентировки
class CreateFragment : Fragment() {

    lateinit var binding: FragmentCreateBinding
    lateinit var imageView: ImageView
    lateinit var selectedImageView: ImageView
    private lateinit var myViewModel: MyViewModel
    private lateinit var manDao: ManDao
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
        }

        selectImageButton.setOnClickListener{
            openGallery()
        }

        //mvvm
        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)


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
            val bitmap = myViewModel.createBitmapFromView(imageView, text, text2, text3, text4, text5, text6, userSelectedImage)
            saveImageToGallery(bitmap)
        } else{
            Toast.makeText(requireContext(), "Выберите изображение", Toast.LENGTH_SHORT).show()
        }




        //Загрузка изображения с помощью Picasso
        Picasso.get()
            .load(R.drawable.fewf2)
            .into(imageView, object : com.squareup.picasso.Callback{
                override fun onSuccess() {
                    val bitmap = myViewModel.createBitmapFromView(
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

    //Рисовка в mvvm

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