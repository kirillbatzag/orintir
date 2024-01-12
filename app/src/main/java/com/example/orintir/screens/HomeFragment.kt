package com.example.orintir.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.orintir.MAIN
import com.example.orintir.R
import com.example.orintir.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    //Основное меню
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createBt.setOnClickListener {
            MAIN.navController.navigate(R.id.action_homeFragment_to_createFragment)
        }

        binding.listBt.setOnClickListener {
            MAIN.navController.navigate(R.id.action_homeFragment_to_listFragment)
        }
    }

}