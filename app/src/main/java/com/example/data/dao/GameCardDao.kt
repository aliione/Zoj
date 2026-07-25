package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.GameCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameCardDao {
  @Query("SELECT * FROM game_cards ORDER BY id DESC")
  fun getAllCards(): Flow<List<GameCardEntity>>

  @Query("SELECT * FROM game_cards WHERE excitementLevel <= :maxExcitementLevel")
  suspend fun getCardsForExcitement(maxExcitementLevel: Int): List<GameCardEntity>

  @Query("SELECT * FROM game_cards WHERE isPlayed = 0 AND excitementLevel <= :maxExcitementLevel")
  suspend fun getUnplayedCardsForExcitement(maxExcitementLevel: Int): List<GameCardEntity>

  @Query("SELECT * FROM game_cards WHERE isPlayed = 0")
  suspend fun getAllUnplayedCards(): List<GameCardEntity>

  @Query("UPDATE game_cards SET isPlayed = 1 WHERE id = :cardId")
  suspend fun markAsPlayed(cardId: Int)

  @Query("UPDATE game_cards SET isPlayed = 0")
  suspend fun resetAllPlayed()

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCards(cards: List<GameCardEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCard(card: GameCardEntity): Long

  @Update
  suspend fun updateCard(card: GameCardEntity)

  @Delete
  suspend fun deleteCard(card: GameCardEntity)

  @Query("DELETE FROM game_cards WHERE id = :cardId")
  suspend fun deleteCardById(cardId: Int)

  @Query("DELETE FROM game_cards")
  suspend fun deleteAllCards()

  @Query("SELECT COUNT(*) FROM game_cards")
  suspend fun getCardCount(): Int
}
