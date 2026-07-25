package com.example

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.CardsScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PinLockScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SpinWheelScreen
import com.example.ui.theme.ArtisticOnPrimary
import com.example.ui.theme.ArtisticOutline
import com.example.ui.theme.ArtisticPrimary
import com.example.ui.theme.ArtisticSurfaceHeader
import com.example.ui.theme.CouplesGameTheme
import com.example.ui.viewmodel.GameViewModel

enum class AppTab(val title: String, val icon: ImageVector, val testTag: String) {
  HOME("خانه", Icons.Default.Home, "tab_home"),
  SPIN("گردونه", Icons.Default.Casino, "tab_spin"),
  CARDS("کارت‌ها", Icons.Default.Style, "tab_cards"),
  HISTORY("تاریخچه", Icons.Default.History, "tab_history"),
  SETTINGS("تنظیمات", Icons.Default.Settings, "tab_settings")
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      CouplesGameTheme {
        MainAppContent()
      }
    }
  }
}

@Composable
fun MainAppContent(gameViewModel: GameViewModel = viewModel()) {
  val uiState by gameViewModel.uiState.collectAsState()
  var currentTab by remember { mutableStateOf(AppTab.HOME) }

  val permissionsToRequest = remember {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
      arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
  }

  val permissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestMultiplePermissions()
  ) { _ -> }

  LaunchedEffect(Unit) {
    permissionLauncher.launch(permissionsToRequest)
  }

  if (uiState.isAppLocked && uiState.pinLockCode.isNotEmpty()) {
    PinLockScreen(
      viewModel = gameViewModel,
      expectedCode = uiState.pinLockCode
    )
  } else {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      bottomBar = {
        NavigationBar(
          containerColor = ArtisticSurfaceHeader,
          tonalElevation = 8.dp
        ) {
          AppTab.values().forEach { tab ->
            val isSelected = currentTab == tab
            NavigationBarItem(
              selected = isSelected,
              onClick = { currentTab = tab },
              icon = {
                Icon(
                  imageVector = tab.icon,
                  contentDescription = tab.title,
                  modifier = Modifier.size(24.dp)
                )
              },
              label = {
                Text(
                  text = tab.title,
                  fontSize = 11.sp
                )
              },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ArtisticOnPrimary,
                selectedTextColor = ArtisticPrimary,
                indicatorColor = ArtisticPrimary,
                unselectedIconColor = ArtisticOutline,
                unselectedTextColor = ArtisticOutline
              ),
              modifier = Modifier.testTag(tab.testTag)
            )
          }
        }
      }
    ) { innerPadding ->
      val modifier = Modifier.padding(innerPadding)
      when (currentTab) {
        AppTab.HOME -> HomeScreen(viewModel = gameViewModel, uiState = uiState, modifier = modifier)
        AppTab.SPIN -> SpinWheelScreen(viewModel = gameViewModel, modifier = modifier)
        AppTab.CARDS -> CardsScreen(viewModel = gameViewModel, modifier = modifier)
        AppTab.HISTORY -> HistoryScreen(viewModel = gameViewModel, modifier = modifier)
        AppTab.SETTINGS -> SettingsScreen(viewModel = gameViewModel, uiState = uiState, modifier = modifier)
      }
    }
  }
}
