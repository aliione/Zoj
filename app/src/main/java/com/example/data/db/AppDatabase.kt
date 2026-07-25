package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.GameCardDao
import com.example.data.dao.GameHistoryDao
import com.example.data.entity.GameCardEntity
import com.example.data.entity.GameHistoryEntity

@Database(entities = [GameCardEntity::class, GameHistoryEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract fun gameCardDao(): GameCardDao
  abstract fun gameHistoryDao(): GameHistoryDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "couples_game_db"
        ).fallbackToDestructiveMigration(true).build()
        INSTANCE = instance
        instance
      }
    }
  }
}
