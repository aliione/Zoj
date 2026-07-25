package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import java.io.File
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.GameCardEntity
import com.example.ui.components.CardAvatarImage
import com.example.ui.components.CardDisplayImage
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

@Composable
fun CardsScreen(
  viewModel: GameViewModel,
  modifier: Modifier = Modifier
) {
  val cardList by viewModel.allCards.collectAsState()
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf("همه") }
  var showAddDialog by remember { mutableStateOf(false) }
  var editingCard by remember { mutableStateOf<GameCardEntity?>(null) }
  var deletingCard by remember { mutableStateOf<GameCardEntity?>(null) }

  val categories = remember(cardList) {
    listOf("همه") + cardList.map { it.category }.distinct()
  }

  val filteredCards = cardList.filter { card ->
    (selectedCategory == "همه" || card.category == selectedCategory) &&
      (searchQuery.isBlank() || card.title.contains(searchQuery) || card.content.contains(searchQuery))
  }

  Scaffold(
    modifier = modifier,
    containerColor = ArtisticBackground,
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showAddDialog = true },
        containerColor = ArtisticPrimary,
        contentColor = ArtisticOnPrimary,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.testTag("add_card_fab")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Add, contentDescription = "افزودن پوزیشن")
          Spacer(modifier = Modifier.width(6.dp))
          Text("افزودن جدید", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
      }
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(16.dp)
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
            imageVector = Icons.Default.Style,
            contentDescription = null,
            tint = ArtisticPrimary,
            modifier = Modifier.size(28.dp)
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = "مدیریت پوزیشن‌ها و کارت‌ها",
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "امکان افزودن، ویرایش، حذف و بارگذاری تصویر اختصاصی",
              fontSize = 11.sp,
              color = ArtisticOutline
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Search Field
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("جستجو در نام یا توضیحات...", color = ArtisticOutline, fontSize = 13.sp) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = ArtisticPrimary) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = ArtisticSurfaceCard,
          unfocusedContainerColor = ArtisticSurfaceCard,
          focusedBorderColor = ArtisticPrimary,
          unfocusedBorderColor = ArtisticOutline.copy(alpha = 0.5f),
          focusedTextColor = Color.White,
          unfocusedTextColor = Color.White
        )
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Categories Horizontal Row
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        items(categories) { category ->
          val isSelected = category == selectedCategory
          Surface(
            modifier = Modifier
              .clip(RoundedCornerShape(14.dp))
              .clickable { selectedCategory = category },
            color = if (isSelected) ArtisticPrimary else ArtisticSurfaceCard,
            border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, ArtisticOutline.copy(alpha = 0.3f))
          ) {
            Text(
              text = category,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = if (isSelected) ArtisticOnPrimary else Color.White,
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Cards List
      if (filteredCards.isEmpty()) {
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "هیچ کارتی یافت نشد. می‌توانید با دکمه بالا کارت جدید اضافه کنید.",
            color = ArtisticOutline,
            fontSize = 14.sp
          )
        }
      } else {
        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(12.dp),
          modifier = Modifier.fillMaxSize()
        ) {
          items(filteredCards, key = { it.id }) { card ->
            CardItemRow(
              card = card,
              onEditClick = { editingCard = card },
              onDeleteClick = { deletingCard = card },
              onFavoriteToggle = { viewModel.toggleFavorite(card) }
            )
          }
        }
      }
    }
  }

  // Add Card Dialog
  if (showAddDialog) {
    CardFormDialog(
      titleText = "افزودن پوزیشن / کارت جدید",
      initialCard = null,
      onDismiss = { showAddDialog = false },
      onSave = { title, content, category, level, timer, imageUri ->
        viewModel.addCustomCard(title, content, category, level, timer, imageUri)
        showAddDialog = false
      }
    )
  }

  // Edit Card Dialog
  editingCard?.let { cardToEdit ->
    CardFormDialog(
      titleText = "ویرایش پوزیشن / کارت",
      initialCard = cardToEdit,
      onDismiss = { editingCard = null },
      onSave = { title, content, category, level, timer, imageUri ->
        viewModel.updateCard(
          cardToEdit.copy(
            title = title,
            content = content,
            category = category,
            excitementLevel = level,
            timerSeconds = timer,
            imageUri = imageUri
          )
        )
        editingCard = null
      }
    )
  }

  // Delete Confirm Dialog
  deletingCard?.let { cardToDelete ->
    AlertDialog(
      onDismissRequest = { deletingCard = null },
      containerColor = ArtisticSurfaceCard,
      title = { Text("حذف کارت", color = Color.White, fontWeight = FontWeight.Bold) },
      text = { Text("آیا از حذف کارت «${cardToDelete.title}» اطمینان دارید؟", color = ArtisticOnSurface) },
      confirmButton = {
        Button(
          onClick = {
            viewModel.deleteCard(cardToDelete)
            deletingCard = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = ArtisticRose)
        ) {
          Text("حذف شود", color = Color.White, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { deletingCard = null }) {
          Text("انصراف", color = ArtisticOutline)
        }
      }
    )
  }
}

@Composable
fun CardItemRow(
  card: GameCardEntity,
  onEditClick: () -> Unit,
  onDeleteClick: () -> Unit,
  onFavoriteToggle: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = ArtisticSurfaceCard),
    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      CardDisplayImage(
        imageUri = card.imageUri,
        category = card.category,
        modifier = Modifier.size(80.dp),
        shape = RoundedCornerShape(14.dp)
      )

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = ArtisticPrimary
          ) {
            Text(
              text = card.title,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = ArtisticOnPrimary,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = ArtisticSecondaryContainer
          ) {
            Text(
              text = card.category,
              fontSize = 10.sp,
              color = Color.White,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = card.content,
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium,
          color = ArtisticOnSurface,
          maxLines = 2,
          lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(text = "زمان: ${card.timerSeconds} ثانیه", fontSize = 10.sp, color = ArtisticOutline)
          Text(text = "سطح: ${card.excitementLevel}", fontSize = 10.sp, color = ArtisticGold)
        }
      }

      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
      ) {
        IconButton(onClick = onFavoriteToggle, modifier = Modifier.size(32.dp)) {
          Icon(
            imageVector = if (card.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = null,
            tint = if (card.isFavorite) ArtisticRose else ArtisticOutline,
            modifier = Modifier.size(20.dp)
          )
        }
        IconButton(onClick = onEditClick, modifier = Modifier.size(32.dp)) {
          Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "ویرایش",
            tint = ArtisticPrimary,
            modifier = Modifier.size(20.dp)
          )
        }
        IconButton(onClick = onDeleteClick, modifier = Modifier.size(32.dp)) {
          Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "حذف",
            tint = ArtisticRose,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}

@Composable
fun CardFormDialog(
  titleText: String,
  initialCard: GameCardEntity?,
  onDismiss: () -> Unit,
  onSave: (title: String, content: String, category: String, level: Int, timerSeconds: Int, imageUri: String?) -> Unit
) {
  val context = LocalContext.current
  var title by remember { mutableStateOf(initialCard?.title ?: "") }
  var content by remember { mutableStateOf(initialCard?.content ?: "") }
  var category by remember { mutableStateOf(initialCard?.category ?: "پوزیشن‌های رابطه") }
  var level by remember { mutableStateOf(initialCard?.excitementLevel ?: 2) }
  var timerSecondsText by remember { mutableStateOf((initialCard?.timerSeconds ?: 60).toString()) }
  var imageUri by remember { mutableStateOf(initialCard?.imageUri) }

  val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
    uri?.let { inputUri ->
      try {
        val inputStream = context.contentResolver.openInputStream(inputUri)
        val imageFile = File(context.filesDir, "card_img_${System.currentTimeMillis()}.jpg")
        inputStream?.use { input ->
          imageFile.outputStream().use { output ->
            input.copyTo(output)
          }
        }
        imageUri = imageFile.toURI().toString()
      } catch (e: Exception) {
        imageUri = inputUri.toString()
      }
    }
  }

  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = ArtisticSurfaceCard,
    title = {
      Text(
        text = titleText,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )
    },
    text = {
      Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        // Large Image Preview Section
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          CardDisplayImage(
            imageUri = imageUri,
            category = category,
            modifier = Modifier
              .fillMaxWidth()
              .height(150.dp),
            shape = RoundedCornerShape(16.dp)
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedButton(
              onClick = { launcher.launch("image/*") },
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(if (imageUri.isNullOrBlank()) "انتخاب تصویر" else "تغییر تصویر", fontSize = 12.sp)
            }

            if (!imageUri.isNullOrBlank()) {
              OutlinedButton(
                onClick = { imageUri = null },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ArtisticRose)
              ) {
                Text("حذف تصویر", fontSize = 12.sp, color = ArtisticRose)
              }
            }
          }
        }

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("نام / عنوان پوزیشن") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
          )
        )

        OutlinedTextField(
          value = content,
          onValueChange = { content = it },
          label = { Text("توضیحات و نحوه انجام") },
          modifier = Modifier.fillMaxWidth(),
          minLines = 3,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
          )
        )

        OutlinedTextField(
          value = category,
          onValueChange = { category = it },
          label = { Text("دسته‌بندی (مثلاً: پوزیشن‌های رابطه، ماساژ و لمس، نوازش)") },
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
          )
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = timerSecondsText,
            onValueChange = { timerSecondsText = it.filter { c -> c.isDigit() } },
            label = { Text("زمان (ثانیه)") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White
            )
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text("سطح هیجان:", fontSize = 11.sp, color = Color.White)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              listOf(1, 2, 3).forEach { l ->
                Surface(
                  modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { level = l },
                  color = if (level == l) ArtisticPrimary else ArtisticSecondaryContainer
                ) {
                  Text(
                    text = "$l",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    color = if (level == l) ArtisticOnPrimary else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                  )
                }
              }
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank() || content.isNotBlank()) {
            val timerSec = timerSecondsText.toIntOrNull() ?: 60
            onSave(title, content, category, level, timerSec, imageUri)
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = ArtisticPrimary)
      ) {
        Text("ذخیره", color = ArtisticOnPrimary, fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("انصراف", color = ArtisticOutline)
      }
    }
  )
}
