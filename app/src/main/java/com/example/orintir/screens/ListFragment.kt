package com.example.orintir.screens

import android.provider.BaseColumns
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.asLiveData
import com.example.orintir.Database.ManModel
import com.example.orintir.MainActivity
import com.example.orintir.R
class ListFragment : Fragment() {


    //Здесь будет список созданых ориентировок, сделаем как базу даннных , что бы можно было при нажатии закрыть ориентировку
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

}