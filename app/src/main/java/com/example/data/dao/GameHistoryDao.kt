package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.GameHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameHistoryDao {
  @Query("SELECT * FROM game_history ORDER BY id DESC")
  fun getAllHistory(): Flow<List<GameHistoryEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertHistory(history: GameHistoryEntity)

  @Query("DELETE FROM game_history")
  suspend fun clearHistory()
}
