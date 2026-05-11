package com.yash.iqtest.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ScoreEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scoreDao(): ScoreDao
}