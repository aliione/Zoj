package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_history")
data class GameHistoryEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val cardTitle: String,
  val category: String,
  val status: String, // "انجام شد" or "رد شد"
  val points: Int,
  val timestamp: Long = System.currentTimeMillis(),
  val playerName: String = "زوجین"
)
