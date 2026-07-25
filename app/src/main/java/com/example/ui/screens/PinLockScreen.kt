package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ArtisticBackground
import com.example.ui.theme.ArtisticOnPrimary
import com.example.ui.theme.ArtisticOutline
import com.example.ui.theme.ArtisticPrimary
import com.example.ui.theme.ArtisticRose
import com.example.ui.viewmodel.GameViewModel

@Composable
fun PinLockScreen(
  viewModel: GameViewModel,
  expectedCode: String,
  modifier: Modifier = Modifier
) {
  var enteredCode by remember { mutableStateOf("") }
  var isError by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ArtisticBackground)
      .padding(32.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Surface(
      shape = CircleShape,
      color = ArtisticPrimary.copy(alpha = 0.15f),
      modifier = Modifier.size(80.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Lock,
        contentDescription = null,
        tint = ArtisticPrimary,
        modifier = Modifier
          .padding(20.dp)
          .fillMaxSize()
      )
    }

    Spacer(modifier = Modifier.height(24.dp))

    Text(
      text = "ورود به بازی زوجین",
      fontSize = 22.sp,
      fontWeight = FontWeight.Bold,
      color = Color.White
    )

    Text(
      text = "لطفاً رمز ۴ رقمی برنامه را وارد کنید",
      fontSize = 13.sp,
      color = ArtisticOutline,
      modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
    )

    OutlinedTextField(
      value = enteredCode,
      onValueChange = {
        if (it.length <= 4) {
          enteredCode = it
          isError = false
        }
      },
      visualTransformation = PasswordVisualTransformation(),
      isError = isError,
      label = { Text("رمز عبور") },
      singleLine = true,
      modifier = Modifier
        .fillMaxWidth(0.8f)
        .testTag("unlock_pin_input"),
      shape = RoundedCornerShape(16.dp)
    )

    if (isError) {
      Text(
        text = "رمز وارد شده اشتباه است!",
        color = ArtisticRose,
        fontSize = 12.sp,
        modifier = Modifier.padding(top = 8.dp)
      )
    }

    Spacer(modifier = Modifier.height(24.dp))

    Button(
      onClick = {
        if (enteredCode == expectedCode) {
          viewModel.unlockApp()
        } else {
          isError = true
        }
      },
      modifier = Modifier
        .fillMaxWidth(0.8f)
        .height(54.dp)
        .testTag("unlock_app_button"),
      shape = RoundedCornerShape(16.dp),
      colors = ButtonDefaults.buttonColors(containerColor = ArtisticPrimary)
    ) {
      Text("ورود", color = ArtisticOnPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
  }
}
