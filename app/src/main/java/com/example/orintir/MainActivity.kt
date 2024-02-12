package com.example.orintir

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Database
import androidx.room.Room
import com.example.orintir.Database.ManDatabase
import com.example.orintir.Database.ManModel
import com.example.orintir.databinding.ActivityMainBinding
import com.example.orintir.mvvm.MyViewModel


class MainActivity : AppCompatActivity() {

    //создание объекта бд
    companion object{
         lateinit var db: ManDatabase
    }

    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {

        //инициализация бд
         db = Room.databaseBuilder(
            applicationContext,
            ManDatabase::class.java, "people"
        ).fallbackToDestructiveMigration().build()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        MAIN = this


    }
}