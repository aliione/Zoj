package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.NotInterested
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.GameHistoryEntity
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
import com.example.ui.viewmodel.GameViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
  viewModel: GameViewModel,
  modifier: Modifier = Modifier
) {
  val historyList by viewModel.allHistory.collectAsState()
  val uiState by viewModel.uiState.collectAsState()
  var showClearConfirm by remember { mutableStateOf(false) }

  val dateFormat = remember { SimpleDateFormat("yyyy/MM/dd - HH:mm", Locale.getDefault()) }

  Scaffold(
    modifier = modifier,
    containerColor = ArtisticBackground
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(16.dp)
    ) {
      // Header Card with Scores
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = ArtisticSurfaceHeader
      ) {
        Column(
          modifier = Modifier.padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = ArtisticGold,
                modifier = Modifier.size(28.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = "تاریخچه چالش‌ها و امتیازات",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
            if (historyList.isNotEmpty()) {
              IconButton(onClick = { showClearConfirm = true }) {
                Icon(
                  imageVector = Icons.Default.Delete,
                  contentDescription = "پاکسازی",
                  tint = ArtisticRose
                )
              }
            }
          }

          // Player Score Cards
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Card(
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = ArtisticSurfaceCard)
            ) {
              Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(uiState.player1Name, fontSize = 13.sp, color = ArtisticOutline)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = ArtisticGold, modifier = Modifier.size(18.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("${uiState.player1Score} امتیاز", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ArtisticGold)
                }
              }
            }

            Card(
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = ArtisticSurfaceCard)
            ) {
              Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(uiState.player2Name, fontSize = 13.sp, color = ArtisticOutline)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = ArtisticRose, modifier = Modifier.size(18.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("${uiState.player2Score} امتیاز", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ArtisticRose)
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // List of History
      if (historyList.isEmpty()) {
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "هنوز چالش یا کارت اجرا شده‌ای وجود ندارد.",
            color = ArtisticOutline,
            fontSize = 14.sp
          )
        }
      } else {
        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(10.dp),
          modifier = Modifier.fillMaxSize()
        ) {
          items(historyList, key = { it.id }) { item ->
            HistoryItemCard(item = item, dateFormat = dateFormat)
          }
        }
      }
    }
  }

  if (showClearConfirm) {
    AlertDialog(
      onDismissRequest = { showClearConfirm = false },
      containerColor = ArtisticSurfaceCard,
      title = { Text("پاکسازی تاریخچه", color = Color.White, fontWeight = FontWeight.Bold) },
      text = { Text("آیا می‌خواهید تمام تاریخچه بازی‌ها پاک شود؟", color = ArtisticOnSurface) },
      confirmButton = {
        Button(
          onClick = {
            viewModel.clearHistory()
            showClearConfirm = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = ArtisticRose)
        ) {
          Text("پاکسازی", color = Color.White, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showClearConfirm = false }) {
          Text("انصراف", color = ArtisticOutline)
        }
      }
    )
  }
}

@Composable
fun HistoryItemCard(
  item: GameHistoryEntity,
  dateFormat: SimpleDateFormat
) {
  val isCompleted = item.status == "انجام شد" || item.status == "گردونه شانس"

  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = ArtisticSurfaceCard)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Surface(
        shape = CircleShape,
        color = if (isCompleted) ArtisticPrimary.copy(alpha = 0.2f) else ArtisticRose.copy(alpha = 0.2f),
        modifier = Modifier.size(44.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.NotInterested,
            contentDescription = null,
            tint = if (isCompleted) ArtisticGold else ArtisticRose,
            modifier = Modifier.size(24.dp)
          )
        }
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = item.cardTitle,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(text = "بازیکن: ${item.playerName}", fontSize = 11.sp, color = ArtisticOutline)
          Text(text = "•", fontSize = 11.sp, color = ArtisticOutline)
          Text(text = dateFormat.format(Date(item.timestamp)), fontSize = 11.sp, color = ArtisticOutline)
        }
      }

      Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isCompleted) ArtisticGold.copy(alpha = 0.2f) else ArtisticSecondaryContainer
      ) {
        Text(
          text = if (isCompleted) "+${item.points} امتیاز" else "رد شد",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = if (isCompleted) ArtisticGold else ArtisticOutline,
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
      }
    }
  }
}
