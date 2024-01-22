package com.example.orintir.Database

import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room

@Database(
    entities = [ManModel::class],
    version = 1,
    exportSchema = false
)

abstract class ManDatabase : RoomDatabase() {

    abstract val ManDao: ManDao

}