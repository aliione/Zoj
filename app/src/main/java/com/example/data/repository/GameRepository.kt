package com.example.data.repository

import android.content.Context
import com.example.data.db.AppDatabase
import com.example.data.entity.GameCardEntity
import com.example.data.entity.GameHistoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import kotlin.random.Random

class GameRepository(private val context: Context) {
  private val db = AppDatabase.getDatabase(context)
  private val dao = db.gameCardDao()
  private val historyDao = db.gameHistoryDao()

  suspend fun initializeDatabaseIfEmpty() = withContext(Dispatchers.IO) {
    if (dao.getCardCount() == 0) {
      loadDefaultContent()
    }
  }

  suspend fun resetToDefaultData() = withContext(Dispatchers.IO) {
    dao.deleteAllCards()
    historyDao.clearHistory()
    loadDefaultContent()
  }

  private suspend fun loadDefaultContent() {
    val jsonString = context.assets.open("game_content.json").bufferedReader().use { it.readText() }
    val jsonArray = JSONArray(jsonString)
    val initialCards = mutableListOf<GameCardEntity>()

    for (i in 0 until jsonArray.length()) {
      val obj = jsonArray.getJSONObject(i)
      initialCards.add(
        GameCardEntity(
          title = obj.optString("title", "کارت بازی"),
          content = obj.optString("content", ""),
          category = obj.optString("category", "پوزیشن و صمیمیت"),
          excitementLevel = obj.optInt("excitementLevel", 1),
          weight = obj.optInt("weight", 1),
          timerSeconds = obj.optInt("timerSeconds", 45),
          isCustom = false,
          isFavorite = false,
          imageUri = obj.optString("imageUri", null),
          isPlayed = false
        )
      )
    }
    dao.insertCards(initialCards)
  }

  fun getAllCards(): Flow<List<GameCardEntity>> = dao.getAllCards()
  fun getAllHistory(): Flow<List<GameHistoryEntity>> = historyDao.getAllHistory()

  suspend fun getRandomWeightedCard(maxExcitementLevel: Int): GameCardEntity? = withContext(Dispatchers.IO) {
    var unplayedCandidates = dao.getUnplayedCardsForExcitement(maxExcitementLevel)
    
    // If all candidates in this level have been played, reset played status and re-fetch!
    if (unplayedCandidates.isEmpty()) {
      dao.resetAllPlayed()
      unplayedCandidates = dao.getUnplayedCardsForExcitement(maxExcitementLevel)
    }
    
    if (unplayedCandidates.isEmpty()) {
      val allCards = dao.getCardsForExcitement(maxExcitementLevel)
      if (allCards.isEmpty()) return@withContext null
      return@withContext allCards.random()
    }

    val totalWeight = unplayedCandidates.sumOf { it.weight }
    if (totalWeight <= 0) return@withContext unplayedCandidates.random()

    var randomVal = Random.nextInt(totalWeight)
    for (card in unplayedCandidates) {
      if (randomVal < card.weight) {
        return@withContext card
      }
      randomVal -= card.weight
    }
    unplayedCandidates.random()
  }

  suspend fun markCardAsPlayed(cardId: Int) = withContext(Dispatchers.IO) {
    dao.markAsPlayed(cardId)
  }

  suspend fun addHistoryEntry(cardTitle: String, category: String, status: String, points: Int, playerName: String) = withContext(Dispatchers.IO) {
    historyDao.insertHistory(
      GameHistoryEntity(
        cardTitle = cardTitle,
        category = category,
        status = status,
        points = points,
        playerName = playerName
      )
    )
  }

  suspend fun clearHistory() = withContext(Dispatchers.IO) {
    historyDao.clearHistory()
  }

  suspend fun addCustomCard(card: GameCardEntity) = withContext(Dispatchers.IO) {
    dao.insertCard(card)
  }

  suspend fun updateCard(card: GameCardEntity) = withContext(Dispatchers.IO) {
    dao.updateCard(card)
  }

  suspend fun deleteCard(card: GameCardEntity) = withContext(Dispatchers.IO) {
    dao.deleteCard(card)
  }

  suspend fun deleteCardById(cardId: Int) = withContext(Dispatchers.IO) {
    dao.deleteCardById(cardId)
  }
}
