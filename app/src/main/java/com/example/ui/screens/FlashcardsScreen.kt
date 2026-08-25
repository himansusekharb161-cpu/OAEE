package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Flashcard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardsScreen(
    flashcards: List<Flashcard>,
    onBack: () -> Unit
) {
    val subjects = listOf("All") + flashcards.map { it.subject }.distinct()
    var selectedSubject by remember { mutableStateOf("All") }

    val filteredCards = if (selectedSubject == "All") flashcards else flashcards.filter { it.subject == selectedSubject }
    var currentIndex by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    var masteredCount by remember { mutableStateOf(0) }
    var reviewCount by remember { mutableStateOf(0) }

    LaunchedEffect(selectedSubject) {
        currentIndex = 0
        isFlipped = false
    }

    val currentCard = filteredCards.getOrNull(currentIndex)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AOEE Freshcards Active Recall 🎴", fontWeight = FontWeight.Bold, color = TextWhitePrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextWhitePrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AoeeNavyBg)
            )
        },
        containerColor = AoeeNavyBg
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Controls & Chips
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "SELECT REVISION SUBJECT:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElectricCyan,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(subjects) { subject ->
                        FilterChip(
                            selected = selectedSubject == subject,
                            onClick = { selectedSubject = subject },
                            label = { Text(subject, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DeepIndigo,
                                selectedLabelColor = CyanGlow
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Card ${if (filteredCards.isNotEmpty()) currentIndex + 1 else 0} of ${filteredCards.size}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhitePrimary
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = "✓ Mastered: $masteredCount", fontSize = 12.sp, color = SuccessEmerald, fontWeight = FontWeight.Bold)
                        Text(text = "↻ Review: $reviewCount", fontSize = 12.sp, color = ElectricCyan, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Main Flashcard Flip Area
            if (currentCard != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clickable { isFlipped = !isFlipped },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isFlipped) DeepIndigo else AoeeCardBg
                    ),
                    border = BorderStroke(1.5.dp, if (isFlipped) ElectricCyan else AoeeCardBorder)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = ElectricCyan.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = currentCard.subject.uppercase(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = ElectricCyan,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Text(
                                    text = if (isFlipped) "ANSWER / CONCEPT" else "TAP TO FLIP 🔄",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyanGlow
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(vertical = 16.dp)
                            ) {
                                Text(
                                    text = if (isFlipped) currentCard.answerOrConcept else currentCard.questionOrTopic,
                                    fontSize = if (isFlipped) 16.sp else 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhitePrimary,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 24.sp
                                )
                            }

                            Surface(
                                color = Color(0xFFF3EDF7),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = currentCard.tag,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextWhitePrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                Text("No flashcards found for selected filter.", fontSize = 14.sp, color = TextMutedSecondary)
            }

            // Bottom Action Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        reviewCount++
                        if (currentIndex < filteredCards.size - 1) {
                            currentIndex++
                            isFlipped = false
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Repeat, contentDescription = null, tint = TextWhitePrimary)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Need Review", color = TextWhitePrimary)
                }

                Button(
                    onClick = {
                        masteredCount++
                        if (currentIndex < filteredCards.size - 1) {
                            currentIndex++
                            isFlipped = false
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessEmerald)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Got It Right", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
