package com.example.orintir.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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