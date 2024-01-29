package com.example.orintir.screens

import android.provider.BaseColumns
import android.os.Bundle
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

class ListFragment : Fragment() {

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

        menAdapter = MenAdapter(dataList, object : MenAdapter.OnDeleteClickListener {
            override fun onDeleteClick(manModel: ManModel) {
                deleteManFromDatabase(manModel)
            }
        })

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
}
