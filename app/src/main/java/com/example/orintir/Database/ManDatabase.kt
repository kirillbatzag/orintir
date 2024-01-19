package com.example.orintir.Database

import androidx.room.RoomDatabase
import androidx.room.Database

@Database(
    entities = [ManModel::class],
    version = 1,
    exportSchema = false
)

abstract class ManDatabase : RoomDatabase() {

    abstract val ManDao: ManDao
}