package com.example.orintir.screens

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.BaseColumns
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orintir.Database.ManDatabase
import com.example.orintir.Database.ManModel
import com.example.orintir.MainActivity
import com.example.orintir.R
import com.example.orintir.adapter.MenAdapter
import com.example.orintir.databinding.FragmentListBinding
import java.io.IOException
import java.io.OutputStream

class ListFragment : Fragment(), MenAdapter.OnPersonStatusChangeListener {

    private lateinit var binding: FragmentListBinding
    private lateinit var menAdapter: MenAdapter
    private var dataList: List<ManModel> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menAdapter = MenAdapter(
            dataList,
            object : MenAdapter.OnDeleteClickListener {
                override fun onDeleteClick(manModel: ManModel) {
                    deleteManFromDatabase(manModel)
                }
            },
            this // передайте текущий фрагмент в качестве OnPersonStatusChangeListener
        )

        binding.rvListMen.adapter = menAdapter
        binding.rvListMen.layoutManager = LinearLayoutManager(requireContext())

        // Получите LiveData из базы данных и наблюдайте за изменениями
        MainActivity.db.ManDao.getAllPeople().observe(viewLifecycleOwner, { people ->
            dataList = people
            menAdapter.setData(dataList)
        })
    }

    private fun deleteManFromDatabase(manModel: ManModel){
        Thread {
            MainActivity.db.ManDao.deleteMan(manModel)
        }.start()
    }

    override fun onPersonStatusChange(manModel: ManModel, isFound: Boolean) {
        // Обработка изменения статуса человека
        if (isFound) {
            Toast.makeText(requireContext(), "Человек найден", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(requireContext(), "Человек найден", Toast.LENGTH_SHORT).show()
        }
        saveImageToGallery(BitmapFactory.decodeByteArray(manModel.imageData, 0, manModel.imageData.size))
    }
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


