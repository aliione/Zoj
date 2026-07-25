package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.io.File
import android.net.Uri
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.GameCardEntity
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

@Composable
fun GameHeader(
  activePlayerName: String,
  excitementLevel: Int,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)),
    color = ArtisticSurfaceHeader,
    shadowElevation = 8.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "نوبت بازیکن",
          fontSize = 12.sp,
          color = ArtisticPrimary,
          fontWeight = FontWeight.Medium,
          letterSpacing = 0.5.sp
        )
        Text(
          text = activePlayerName,
          fontSize = 22.sp,
          color = Color.White,
          fontWeight = FontWeight.Bold
        )
      }

      Surface(
        shape = RoundedCornerShape(16.dp),
        color = ArtisticSecondaryContainer
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = "سطح هیجان $excitementLevel",
            fontSize = 12.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium
          )
          Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            repeat(3) { index ->
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(
                    if (index < excitementLevel) ArtisticPrimary else ArtisticPrimary.copy(alpha = 0.3f)
                  )
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun CircularTimerRing(
  remainingSeconds: Int,
  totalSeconds: Int,
  modifier: Modifier = Modifier
) {
  val progress = if (totalSeconds > 0) remainingSeconds.toFloat() / totalSeconds.toFloat() else 0f
  val minutes = remainingSeconds / 60
  val seconds = remainingSeconds % 60
  val timeString = String.format("%02d:%02d", minutes, seconds)

  Box(
    modifier = modifier.size(170.dp),
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val strokeWidth = 8.dp.toPx()
      // Track ring
      drawCircle(
        color = ArtisticSecondaryContainer.copy(alpha = 0.4f),
        style = Stroke(width = strokeWidth)
      )
      // Progress ring
      drawArc(
        color = ArtisticPrimary,
        startAngle = -90f,
        sweepAngle = 360f * progress,
        useCenter = false,
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
      )
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = timeString,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontFamily = FontFamily.Monospace
      )
      Text(
        text = "زمان باقیمانده",
        fontSize = 11.sp,
        color = ArtisticPrimary
      )
    }
  }
}

@Composable
fun CardDisplayImage(
  imageUri: String?,
  category: String,
  modifier: Modifier = Modifier,
  shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(20.dp)
) {
  val context = LocalContext.current
  var isError by remember(imageUri) { mutableStateOf(false) }

  Box(
    modifier = modifier
      .clip(shape)
      .background(
        Brush.radialGradient(
          colors = listOf(
            ArtisticPrimary.copy(alpha = 0.35f),
            ArtisticSecondaryContainer.copy(alpha = 0.95f)
          )
        )
      )
      .border(2.5.dp, ArtisticGold.copy(alpha = 0.85f), shape),
    contentAlignment = Alignment.Center
  ) {
    if (!imageUri.isNullOrBlank() && !isError) {
      val modelData: Any = remember(imageUri) {
        when {
          imageUri.startsWith("/") -> File(imageUri)
          imageUri.startsWith("file:") -> Uri.parse(imageUri)
          imageUri.startsWith("content:") -> Uri.parse(imageUri)
          else -> imageUri
        }
      }
      val imageRequest = remember(modelData, context) {
        ImageRequest.Builder(context)
          .data(modelData)
          .crossfade(true)
          .build()
      }
      AsyncImage(
        model = imageRequest,
        contentDescription = "تصویر کارت",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        onError = { isError = true }
      )
    } else {
      val icon = when {
        category.contains("پوزیشن") -> Icons.Default.Favorite
        category.contains("ماساژ") -> Icons.Default.SelfImprovement
        category.contains("لمس") -> Icons.Default.TouchApp
        else -> Icons.Default.FavoriteBorder
      }
      Icon(
        imageVector = icon,
        contentDescription = "تصویر کارت",
        tint = ArtisticGold,
        modifier = Modifier.size(48.dp)
      )
    }
  }
}

@Composable
fun CardAvatarImage(
  imageUri: String?,
  category: String,
  modifier: Modifier = Modifier,
  sizeDp: Int = 100
) {
  CardDisplayImage(
    imageUri = imageUri,
    category = category,
    modifier = modifier.size(sizeDp.dp),
    shape = RoundedCornerShape(16.dp)
  )
}

@Composable
fun InteractiveGameCard(
  card: GameCardEntity?,
  isRevealed: Boolean,
  onRevealClick: () -> Unit,
  onFavoriteClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val backgroundBrush = Brush.linearGradient(
    colors = listOf(ArtisticSurfaceCard, ArtisticSurfaceHeader)
  )

  Card(
    modifier = modifier
      .fillMaxWidth(),
    shape = RoundedCornerShape(32.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(backgroundBrush)
        .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(32.dp))
        .padding(20.dp)
    ) {
      if (card == null) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "کارتی انتخاب نشده است",
            color = ArtisticOutline,
            fontSize = 16.sp
          )
        }
      } else {
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Top bar on card: Category & Favorite Button
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = ArtisticPrimary
            ) {
              Text(
                text = "دسته‌بندی: ${card.category}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ArtisticOnPrimary,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
              )
            }

            IconButton(onClick = onFavoriteClick) {
              Icon(
                imageVector = if (card.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "علاقه‌مندی",
                tint = if (card.isFavorite) ArtisticRose else ArtisticOutline
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Card Content
          if (isRevealed) {
            Column(
              modifier = Modifier.fillMaxWidth(),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              // 1. Image at the top (Large with border & 4 rounded corners)
              CardDisplayImage(
                imageUri = card.imageUri,
                category = card.category,
                modifier = Modifier
                  .fillMaxWidth()
                  .height(200.dp),
                shape = RoundedCornerShape(20.dp)
              )

              Spacer(modifier = Modifier.height(14.dp))

              // 2. Title underneath the image
              Text(
                text = card.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = ArtisticGold,
                textAlign = TextAlign.Center
              )

              Spacer(modifier = Modifier.height(8.dp))

              // 3. Description underneath the title
              Text(
                text = card.content,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = ArtisticOnSurface,
                textAlign = TextAlign.Center,
                lineHeight = 26.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
              )
            }
          } else {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center,
              modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(ArtisticSecondaryContainer.copy(alpha = 0.5f))
                .clickable { onRevealClick() }
                .padding(24.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = "نمایش کارت",
                tint = ArtisticPrimary,
                modifier = Modifier.size(40.dp)
              )
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = "برای لمس و مشاهده کارت کلیک کنید",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun GameActionButtons(
  onSkipClick: () -> Unit,
  onCompleteClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    OutlinedButton(
      onClick = onSkipClick,
      modifier = Modifier
        .weight(1f)
        .height(56.dp)
        .testTag("skip_button"),
      shape = RoundedCornerShape(18.dp),
      colors = ButtonDefaults.outlinedButtonColors(contentColor = ArtisticPrimary),
      border = androidx.compose.foundation.BorderStroke(1.dp, ArtisticSecondaryContainer)
    ) {
      Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(20.dp))
      Spacer(modifier = Modifier.width(6.dp))
      Text("رد کردن", fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }

    Button(
      onClick = onCompleteClick,
      modifier = Modifier
        .weight(1.8f)
        .height(56.dp)
        .testTag("complete_button"),
      shape = RoundedCornerShape(18.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = ArtisticPrimary,
        contentColor = ArtisticOnPrimary
      )
    ) {
      Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(22.dp))
      Spacer(modifier = Modifier.width(6.dp))
      Text("✓ انجام شد", fontSize = 16.sp, fontWeight = FontWeight.Black)
    }
  }
}
