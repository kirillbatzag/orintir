package com.example.orintir.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface ManDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMan(manModel: ManModel)

    @Query("Select* from people")
    fun getAllPeople(): LiveData<List<ManModel>>
}