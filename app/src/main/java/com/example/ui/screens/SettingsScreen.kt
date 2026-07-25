package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ArtisticBackground
import com.example.ui.theme.ArtisticOnPrimary
import com.example.ui.theme.ArtisticOutline
import com.example.ui.theme.ArtisticPrimary
import com.example.ui.theme.ArtisticRose
import com.example.ui.theme.ArtisticSecondaryContainer
import com.example.ui.theme.ArtisticSurfaceCard
import com.example.ui.theme.ArtisticSurfaceHeader
import com.example.ui.viewmodel.GameUiState
import com.example.ui.viewmodel.GameViewModel

import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton

@Composable
fun SettingsScreen(
  viewModel: GameViewModel,
  uiState: GameUiState,
  modifier: Modifier = Modifier
) {
  var pinInput by remember { mutableStateOf(uiState.pinLockCode) }
  var showResetConfirmDialog by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(ArtisticBackground)
      .padding(20.dp)
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally
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
          imageVector = Icons.Default.Settings,
          contentDescription = null,
          tint = ArtisticPrimary,
          modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "تنظیمات برنامه",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "شخصی‌سازی ویبره، رمز عبور، بازنشانی داده‌ها",
            fontSize = 11.sp,
            color = ArtisticOutline
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Preferences Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = ArtisticSurfaceCard)
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Text(
          text = "تنظیمات بازی",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = ArtisticPrimary
        )

        // Haptic Feedback Switch
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Vibration, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(10.dp))
            Text("ویبره هنگام نمایش کارت", fontSize = 14.sp, color = Color.White)
          }
          Switch(
            checked = uiState.isHapticEnabled,
            onCheckedChange = { viewModel.setHapticEnabled(it) },
            colors = SwitchDefaults.colors(
              checkedThumbColor = ArtisticOnPrimary,
              checkedTrackColor = ArtisticPrimary
            )
          )
        }

        // Hide Card Content by default
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.VisibilityOff, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(10.dp))
            Text("مخفی ماندن کارت تا زمان لمس", fontSize = 14.sp, color = Color.White)
          }
          Switch(
            checked = uiState.isHideCardByDefault,
            onCheckedChange = { viewModel.setHideCardByDefault(it) },
            colors = SwitchDefaults.colors(
              checkedThumbColor = ArtisticOnPrimary,
              checkedTrackColor = ArtisticPrimary
            )
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // PIN Lock Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = ArtisticSurfaceCard)
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = if (uiState.pinLockCode.isNotEmpty()) Icons.Default.Lock else Icons.Default.LockOpen,
            contentDescription = null,
            tint = ArtisticPrimary
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "قفل اختصاصی برنامه با رمز",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = ArtisticPrimary
          )
        }

        Text(
          text = "برای امنیت بیشتر می‌توانید رمز ۴ رقمی برای ورود به برنامه تنظیم کنید.",
          fontSize = 12.sp,
          color = ArtisticOutline
        )

        OutlinedTextField(
          value = pinInput,
          onValueChange = { if (it.length <= 4) pinInput = it },
          label = { Text("رمز ۴ رقمی (مثلاً ۱۲۳۴)") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("pin_lock_input"),
          singleLine = true,
          shape = RoundedCornerShape(16.dp)
        )

        Button(
          onClick = {
            viewModel.setPinLockCode(pinInput)
          },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = ArtisticPrimary)
        ) {
          Text(
            text = if (pinInput.isEmpty()) "حذف رمز عبور" else "ذخیره رمز عبور",
            color = ArtisticOnPrimary,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Reset Data Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = ArtisticSurfaceCard)
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Refresh, contentDescription = null, tint = ArtisticRose)
          Spacer(modifier = Modifier.width(10.dp))
          Text("بازنشانی اطلاعات پیش‌فرض", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ArtisticRose)
        }
        Text(
          text = "در صورت نیاز می‌توانید لیست پوزیشن‌ها و کارت‌ها را به حالت اول بازگردانید.",
          fontSize = 12.sp,
          color = ArtisticOutline
        )
        Button(
          onClick = { showResetConfirmDialog = true },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = ArtisticRose)
        ) {
          Text("بازنشانی به داده‌های اولیه", color = Color.White, fontWeight = FontWeight.Bold)
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // App Info Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = ArtisticSurfaceCard)
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text("نسخه ۱.۰ - کاملاً آفلاین و امن", fontSize = 12.sp, color = ArtisticOutline)
        Spacer(modifier = Modifier.height(4.dp))
        Text("طراحی شده اختصاصی برای زوجین", fontSize = 12.sp, color = ArtisticPrimary, fontWeight = FontWeight.Bold)
      }
    }
  }

  if (showResetConfirmDialog) {
    AlertDialog(
      onDismissRequest = { showResetConfirmDialog = false },
      containerColor = ArtisticSurfaceCard,
      title = { Text("بازنشانی داده‌ها", color = Color.White, fontWeight = FontWeight.Bold) },
      text = { Text("آیا مطمئن هستید؟ با این کار تمامی کارت‌ها بازسازی خواهند شد.", color = ArtisticOutline) },
      confirmButton = {
        Button(
          onClick = {
            viewModel.resetToDefaultData()
            showResetConfirmDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = ArtisticRose)
        ) {
          Text("بله، بازنشانی شود", color = Color.White, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showResetConfirmDialog = false }) {
          Text("انصراف", color = ArtisticOutline)
        }
      }
    )
  }
}
