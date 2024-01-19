package com.example.orintir.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "people")
data class ManModel(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val name: String,
    val city: String,
    val years: Int,
    val obst: String,
    val sings: String,
    val cloth: String,
    val imageData: ByteArray,

)

