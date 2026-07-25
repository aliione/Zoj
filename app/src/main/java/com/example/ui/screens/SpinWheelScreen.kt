package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
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
import com.example.ui.viewmodel.GameViewModel
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

import com.example.ui.components.CardDisplayImage

@Composable
fun SpinWheelScreen(
  viewModel: GameViewModel,
  modifier: Modifier = Modifier
) {
  var rotationAngle by remember { mutableFloatStateOf(0f) }
  var isSpinning by remember { mutableStateOf(false) }
  var resultCard by remember { mutableStateOf<GameCardEntity?>(null) }
  var showResultDialog by remember { mutableStateOf(false) }

  val animatedRotation by animateFloatAsState(
    targetValue = rotationAngle,
    animationSpec = tween(durationMillis = 3500, easing = FastOutSlowInEasing),
    finishedListener = {
      isSpinning = false
      showResultDialog = true
    },
    label = "wheelRotation"
  )

  fun spinWheel() {
    if (isSpinning) return
    isSpinning = true
    showResultDialog = false

    viewModel.spinWheelDrawCard { card ->
      resultCard = card
      val extraTurns = (5..8).random() * 360f
      val randomOffset = Random.nextFloat() * 360f
      rotationAngle += extraTurns + randomOffset
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ArtisticBackground)
      .padding(20.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    // Header
    Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(24.dp),
      color = ArtisticSurfaceHeader
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Casino,
          contentDescription = null,
          tint = ArtisticGold,
          modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "گردونه شانس",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "گردونه را بچرخانید تا یک چالش تصادفی انتخاب شود!",
            fontSize = 11.sp,
            color = ArtisticOutline
          )
        }
      }
    }

    // Wheel Canvas Component
    Box(
      modifier = Modifier
        .size(280.dp)
        .padding(10.dp),
      contentAlignment = Alignment.Center
    ) {
      // Top Pointer Arrow
      Canvas(
        modifier = Modifier
          .size(28.dp)
          .align(Alignment.TopCenter)
      ) {
        val path = androidx.compose.ui.graphics.Path().apply {
          moveTo(size.width / 2, size.height)
          lineTo(0f, 0f)
          lineTo(size.width, 0f)
          close()
        }
        drawPath(path = path, color = ArtisticRose)
      }

      // Rotating Wheel
      Canvas(
        modifier = Modifier
          .fillMaxSize()
          .rotate(animatedRotation)
      ) {
        val sliceCount = 8
        val sweepAngle = 360f / sliceCount
        val colors = listOf(
          ArtisticSecondaryContainer,
          ArtisticSurfaceCard,
          ArtisticPrimary.copy(alpha = 0.8f),
          ArtisticSurfaceHeader,
          ArtisticRose.copy(alpha = 0.7f),
          ArtisticSecondaryContainer,
          ArtisticSurfaceCard,
          ArtisticGold.copy(alpha = 0.6f)
        )

        val radius = size.minDimension / 2
        val center = Offset(size.width / 2, size.height / 2)

        for (i in 0 until sliceCount) {
          drawArc(
            color = colors[i % colors.size],
            startAngle = i * sweepAngle,
            sweepAngle = sweepAngle,
            useCenter = true,
            size = Size(radius * 2, radius * 2),
            topLeft = Offset(center.x - radius, center.y - radius)
          )
          drawArc(
            color = Color.White.copy(alpha = 0.2f),
            startAngle = i * sweepAngle,
            sweepAngle = sweepAngle,
            useCenter = true,
            style = Stroke(width = 2.dp.toPx()),
            size = Size(radius * 2, radius * 2),
            topLeft = Offset(center.x - radius, center.y - radius)
          )
        }

        // Center hub
        drawCircle(
          color = ArtisticSurfaceHeader,
          radius = radius * 0.25f,
          center = center
        )
        drawCircle(
          color = ArtisticGold,
          radius = radius * 0.12f,
          center = center
        )
      }
    }

    // Spin Button
    Button(
      onClick = { spinWheel() },
      enabled = !isSpinning,
      modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .testTag("spin_wheel_button"),
      shape = RoundedCornerShape(20.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = ArtisticPrimary,
        contentColor = ArtisticOnPrimary,
        disabledContainerColor = ArtisticSecondaryContainer
      )
    ) {
      Icon(Icons.Default.Casino, contentDescription = null, modifier = Modifier.size(24.dp))
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = if (isSpinning) "در حال چرخش..." else "چرخش گردونه!",
        fontSize = 18.sp,
        fontWeight = FontWeight.Black
      )
    }
  }

  // Result Dialog
  if (showResultDialog && resultCard != null) {
    val card = resultCard!!
    AlertDialog(
      onDismissRequest = { showResultDialog = false },
      containerColor = ArtisticSurfaceCard,
      shape = RoundedCornerShape(28.dp),
      text = {
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // 1. Image at top (Large with border and 4 rounded corners)
          CardDisplayImage(
            imageUri = card.imageUri,
            category = card.category,
            modifier = Modifier
              .fillMaxWidth()
              .height(180.dp),
            shape = RoundedCornerShape(20.dp)
          )

          Spacer(modifier = Modifier.height(14.dp))

          // 2. Title underneath the image
          Text(
            text = card.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = ArtisticGold,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(8.dp))

          // 3. Description / Content underneath the title
          Text(
            text = card.content,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
          )

          Spacer(modifier = Modifier.height(10.dp))

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = ArtisticSecondaryContainer
          ) {
            Text(
              text = "دسته‌بندی: ${card.category}",
              fontSize = 11.sp,
              color = Color.White,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
          }
        }
      },
      confirmButton = {
        Button(
          onClick = { showResultDialog = false },
          colors = ButtonDefaults.buttonColors(
            containerColor = ArtisticPrimary,
            contentColor = ArtisticOnPrimary
          ),
          shape = RoundedCornerShape(14.dp)
        ) {
          Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("✓ انجام شد", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        OutlinedButton(
          onClick = {
            showResultDialog = false
            spinWheel()
          },
          shape = RoundedCornerShape(14.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, ArtisticSecondaryContainer)
        ) {
          Icon(Icons.Default.Refresh, contentDescription = null, tint = ArtisticPrimary, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("چرخش دوباره", color = ArtisticPrimary)
        }
      }
    )
  }
}
