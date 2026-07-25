package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

private val ArtisticColorScheme = darkColorScheme(
  primary = ArtisticPrimary,
  onPrimary = ArtisticOnPrimary,
  primaryContainer = ArtisticSecondaryContainer,
  onPrimaryContainer = ArtisticOnSecondaryContainer,
  secondary = ArtisticRose,
  onSecondary = ArtisticOnSurface,
  secondaryContainer = ArtisticSecondaryContainer,
  onSecondaryContainer = ArtisticOnSecondaryContainer,
  tertiary = ArtisticGold,
  background = ArtisticBackground,
  onBackground = ArtisticOnSurface,
  surface = ArtisticSurfaceHeader,
  onSurface = ArtisticOnSurface,
  surfaceVariant = ArtisticSurfaceCard,
  onSurfaceVariant = ArtisticOnSurface,
  outline = ArtisticOutline
)

@Composable
fun CouplesGameTheme(
  content: @Composable () -> Unit
) {
  CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
    MaterialTheme(
      colorScheme = ArtisticColorScheme,
      typography = Typography,
      content = content
    )
  }
}

