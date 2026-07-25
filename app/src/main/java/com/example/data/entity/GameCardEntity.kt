package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_cards")
data class GameCardEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
  val title: String,
  val content: String,
  val category: String,
  val excitementLevel: Int, // 1, 2, or 3
  val weight: Int = 1,
  val timerSeconds: Int = 45,
  val isCustom: Boolean = false,
  val isFavorite: Boolean = false,
  val imageUri: String? = null,
  val isPlayed: Boolean = false
)
