package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.entity.GameCardEntity
import com.example.data.entity.GameHistoryEntity
import com.example.data.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class GameUiState(
  val player1Name: String = "علیرضا",
  val player2Name: String = "فاطمه",
  val player1Score: Int = 0,
  val player2Score: Int = 0,
  val activePlayerIndex: Int = 0, // 0 for player 1, 1 for player 2
  val excitementLevel: Int = 2, // 1, 2, or 3
  val isGameStarted: Boolean = false,
  val currentCard: GameCardEntity? = null,
  val isCardRevealed: Boolean = false,
  val completedCount: Int = 0,
  val skippedCount: Int = 0,
  val isHapticEnabled: Boolean = true,
  val isHideCardByDefault: Boolean = true,
  val pinLockCode: String = "",
  val isAppLocked: Boolean = false
)

class GameViewModel(application: Application) : AndroidViewModel(application) {
  private val repository = GameRepository(application)

  private val _uiState = MutableStateFlow(GameUiState())
  val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

  val allCards: StateFlow<List<GameCardEntity>> = repository.getAllCards()
    .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

  val allHistory: StateFlow<List<GameHistoryEntity>> = repository.getAllHistory()
    .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

  init {
    viewModelScope.launch {
      repository.initializeDatabaseIfEmpty()
    }
  }

  fun updatePlayerNames(p1: String, p2: String) {
    _uiState.value = _uiState.value.copy(
      player1Name = p1.ifBlank { "علیرضا" },
      player2Name = p2.ifBlank { "فاطمه" }
    )
  }

  fun updateExcitementLevel(level: Int) {
    _uiState.value = _uiState.value.copy(excitementLevel = level.coerceIn(1, 3))
  }

  fun setHapticEnabled(enabled: Boolean) {
    _uiState.value = _uiState.value.copy(isHapticEnabled = enabled)
  }

  fun setHideCardByDefault(enabled: Boolean) {
    _uiState.value = _uiState.value.copy(isHideCardByDefault = enabled)
  }

  fun setPinLockCode(code: String) {
    _uiState.value = _uiState.value.copy(pinLockCode = code)
  }

  fun unlockApp() {
    _uiState.value = _uiState.value.copy(isAppLocked = false)
  }

  fun lockApp() {
    if (_uiState.value.pinLockCode.isNotEmpty()) {
      _uiState.value = _uiState.value.copy(isAppLocked = true)
    }
  }

  fun startGame() {
    _uiState.value = _uiState.value.copy(
      isGameStarted = true,
      completedCount = 0,
      skippedCount = 0,
      player1Score = 0,
      player2Score = 0,
      activePlayerIndex = 0
    )
    nextTurn()
  }

  fun stopGame() {
    _uiState.value = _uiState.value.copy(isGameStarted = false)
  }

  fun revealCard() {
    _uiState.value = _uiState.value.copy(isCardRevealed = true)
  }

  fun completeCard() {
    val card = _uiState.value.currentCard
    val activePlayer = activePlayerName
    val pointsEarned = (card?.excitementLevel ?: 1) * 10

    viewModelScope.launch {
      if (card != null) {
        repository.markCardAsPlayed(card.id)
        repository.addHistoryEntry(
          cardTitle = card.title,
          category = card.category,
          status = "انجام شد",
          points = pointsEarned,
          playerName = activePlayer
        )
      }

      val newP1Score = if (_uiState.value.activePlayerIndex == 0) _uiState.value.player1Score + pointsEarned else _uiState.value.player1Score
      val newP2Score = if (_uiState.value.activePlayerIndex == 1) _uiState.value.player2Score + pointsEarned else _uiState.value.player2Score

      _uiState.value = _uiState.value.copy(
        completedCount = _uiState.value.completedCount + 1,
        player1Score = newP1Score,
        player2Score = newP2Score,
        activePlayerIndex = 1 - _uiState.value.activePlayerIndex
      )
      nextTurn()
    }
  }

  fun skipCard() {
    val card = _uiState.value.currentCard
    val activePlayer = activePlayerName

    viewModelScope.launch {
      if (card != null) {
        repository.markCardAsPlayed(card.id)
        repository.addHistoryEntry(
          cardTitle = card.title,
          category = card.category,
          status = "رد شد",
          points = 0,
          playerName = activePlayer
        )
      }

      _uiState.value = _uiState.value.copy(
        skippedCount = _uiState.value.skippedCount + 1,
        activePlayerIndex = 1 - _uiState.value.activePlayerIndex
      )
      nextTurn()
    }
  }

  fun nextTurn() {
    viewModelScope.launch {
      val card = repository.getRandomWeightedCard(_uiState.value.excitementLevel)
      _uiState.value = _uiState.value.copy(
        currentCard = card,
        isCardRevealed = !_uiState.value.isHideCardByDefault
      )
    }
  }

  fun spinWheelDrawCard(onResult: (GameCardEntity?) -> Unit) {
    viewModelScope.launch {
      val card = repository.getRandomWeightedCard(_uiState.value.excitementLevel)
      if (card != null) {
        repository.markCardAsPlayed(card.id)
        repository.addHistoryEntry(
          cardTitle = card.title,
          category = card.category,
          status = "گردونه شانس",
          points = 10,
          playerName = activePlayerName
        )
      }
      onResult(card)
    }
  }

  fun addCustomCard(
    title: String,
    content: String,
    category: String,
    level: Int,
    timerSeconds: Int = 60,
    imageUri: String? = null
  ) {
    viewModelScope.launch {
      val newCard = GameCardEntity(
        title = title.ifBlank { "کارت جدید" },
        content = content,
        category = category.ifBlank { "پوزیشن و صمیمیت" },
        excitementLevel = level.coerceIn(1, 3),
        weight = 2,
        timerSeconds = timerSeconds.coerceAtLeast(10),
        isCustom = true,
        imageUri = imageUri
      )
      repository.addCustomCard(newCard)
    }
  }

  fun updateCard(card: GameCardEntity) {
    viewModelScope.launch {
      repository.updateCard(card)
    }
  }

  fun deleteCard(card: GameCardEntity) {
    viewModelScope.launch {
      repository.deleteCard(card)
    }
  }

  fun resetToDefaultData() {
    viewModelScope.launch {
      repository.resetToDefaultData()
    }
  }

  fun clearHistory() {
    viewModelScope.launch {
      repository.clearHistory()
    }
  }

  fun toggleFavorite(card: GameCardEntity) {
    viewModelScope.launch {
      repository.updateCard(card.copy(isFavorite = !card.isFavorite))
    }
  }

  val activePlayerName: String
    get() = if (_uiState.value.activePlayerIndex == 0) _uiState.value.player1Name else _uiState.value.player2Name
}
