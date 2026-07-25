package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.InteractiveGameCard
import com.example.ui.theme.ArtisticBackground
import com.example.ui.theme.ArtisticGold
import com.example.ui.theme.ArtisticOnPrimary
import com.example.ui.theme.ArtisticOnSurface
import com.example.ui.theme.ArtisticOutline
import com.example.ui.theme.ArtisticPrimary
import com.example.ui.theme.ArtisticRose
import com.example.ui.theme.ArtisticSecondaryContainer
import com.example.ui.theme.ArtisticSurfaceCard
import com.example.ui.theme.ArtisticSurfaceHeader
import com.example.ui.viewmodel.GameUiState
import com.example.ui.viewmodel.GameViewModel

@Composable
fun HomeScreen(
  viewModel: GameViewModel,
  uiState: GameUiState,
  modifier: Modifier = Modifier
) {
  if (!uiState.isGameStarted) {
    GameSetupScreen(
      uiState = uiState,
      onPlayerNamesChanged = { p1, p2 -> viewModel.updatePlayerNames(p1, p2) },
      onExcitementSelected = { level -> viewModel.updateExcitementLevel(level) },
      onStartGame = { viewModel.startGame() },
      modifier = modifier
    )
  } else {
    ActiveGameScreen(
      viewModel = viewModel,
      uiState = uiState,
      modifier = modifier
    )
  }
}

@Composable
fun GameSetupScreen(
  uiState: GameUiState,
  onPlayerNamesChanged: (String, String) -> Unit,
  onExcitementSelected: (Int) -> Unit,
  onStartGame: () -> Unit,
  modifier: Modifier = Modifier
) {
  var p1 by remember { mutableStateOf(uiState.player1Name) }
  var p2 by remember { mutableStateOf(uiState.player2Name) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ArtisticBackground)
      .padding(20.dp)
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    // Header Title
    Text(
      text = "بازی دو نفره زوجین",
      fontSize = 26.sp,
      fontWeight = FontWeight.Bold,
      color = Color.White
    )
    Text(
      text = "نام بازیکنان و سطح هیجان را تنظیم کنید",
      fontSize = 13.sp,
      color = ArtisticOutline,
      modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
    )

    // Player Names Section
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = ArtisticSurfaceCard)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Text(
          text = "نام بازیکنان",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = ArtisticPrimary
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = p1,
          onValueChange = {
            p1 = it
            onPlayerNamesChanged(p1, p2)
          },
          label = { Text("نام نفر اول") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("player_1_input"),
          singleLine = true,
          shape = RoundedCornerShape(16.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ArtisticPrimary,
            unfocusedBorderColor = ArtisticOutline,
            focusedLabelColor = ArtisticPrimary,
            unfocusedLabelColor = ArtisticOutline,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
          )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = p2,
          onValueChange = {
            p2 = it
            onPlayerNamesChanged(p1, p2)
          },
          label = { Text("نام نفر دوم") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("player_2_input"),
          singleLine = true,
          shape = RoundedCornerShape(16.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ArtisticPrimary,
            unfocusedBorderColor = ArtisticOutline,
            focusedLabelColor = ArtisticPrimary,
            unfocusedLabelColor = ArtisticOutline,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Excitement Level Selector
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = ArtisticSurfaceCard)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Text(
          text = "سطح هیجان پوزیشن‌ها",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = ArtisticPrimary
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf(1, 2, 3).forEach { level ->
            val isSelected = uiState.excitementLevel == level
            Surface(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .clickable { onExcitementSelected(level) },
              color = if (isSelected) ArtisticSecondaryContainer else Color.White.copy(alpha = 0.05f)
            ) {
              Column(
                modifier = Modifier.padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                  repeat(level) {
                    Icon(
                      imageVector = Icons.Default.Star,
                      contentDescription = null,
                      tint = ArtisticGold,
                      modifier = Modifier.size(16.dp)
                    )
                  }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "سطح $level",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(28.dp))

    // Start Game Button
    Button(
      onClick = onStartGame,
      modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .testTag("start_game_button"),
      shape = RoundedCornerShape(20.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = ArtisticPrimary,
        contentColor = ArtisticOnPrimary
      )
    ) {
      Icon(
        imageVector = Icons.Default.PlayArrow,
        contentDescription = null,
        modifier = Modifier.size(28.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "شروع چالش‌ها",
        fontSize = 18.sp,
        fontWeight = FontWeight.Black
      )
    }

    Spacer(modifier = Modifier.height(20.dp))
  }
}

@Composable
fun ActiveGameScreen(
  viewModel: GameViewModel,
  uiState: GameUiState,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ArtisticBackground)
      .padding(16.dp)
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Score Bar
    Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      color = ArtisticSurfaceHeader
    ) {
      Row(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text("نوبت انجام:", fontSize = 11.sp, color = ArtisticOutline)
          Text(viewModel.activePlayerName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ArtisticGold)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
          Column(horizontalAlignment = Alignment.End) {
            Text(uiState.player1Name, fontSize = 11.sp, color = ArtisticOutline)
            Text("${uiState.player1Score} امتیاز", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
          }
          Column(horizontalAlignment = Alignment.End) {
            Text(uiState.player2Name, fontSize = 11.sp, color = ArtisticOutline)
            Text("${uiState.player2Score} امتیاز", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Interactive Game Card
    InteractiveGameCard(
      card = uiState.currentCard,
      isRevealed = uiState.isCardRevealed,
      onRevealClick = { viewModel.revealCard() },
      onFavoriteClick = {
        uiState.currentCard?.let { viewModel.toggleFavorite(it) }
      },
      modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Action Buttons
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Button(
        onClick = { viewModel.skipCard() },
        modifier = Modifier
          .weight(1f)
          .height(56.dp)
          .testTag("skip_card_button"),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ArtisticSurfaceCard)
      ) {
        Icon(Icons.Default.Close, contentDescription = null, tint = ArtisticRose)
        Spacer(modifier = Modifier.width(6.dp))
        Text("رد کردن چالش", fontSize = 14.sp, color = ArtisticRose, fontWeight = FontWeight.Bold)
      }

      Button(
        onClick = { viewModel.completeCard() },
        modifier = Modifier
          .weight(1f)
          .height(56.dp)
          .testTag("complete_card_button"),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ArtisticPrimary)
      ) {
        Icon(Icons.Default.Check, contentDescription = null, tint = ArtisticOnPrimary)
        Spacer(modifier = Modifier.width(6.dp))
        Text("انجام شد (+امتیاز)", fontSize = 14.sp, color = ArtisticOnPrimary, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Stop Game Button
    Button(
      onClick = { viewModel.stopGame() },
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp),
      shape = RoundedCornerShape(16.dp),
      colors = ButtonDefaults.buttonColors(containerColor = ArtisticSurfaceCard)
    ) {
      Icon(Icons.Default.Stop, contentDescription = null, tint = ArtisticRose, modifier = Modifier.size(18.dp))
      Spacer(modifier = Modifier.width(6.dp))
      Text("پایان چالش‌ها و بازگشت", fontSize = 13.sp, color = ArtisticRose)
    }
  }
}

