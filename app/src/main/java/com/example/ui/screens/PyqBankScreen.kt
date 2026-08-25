package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Question
import com.example.data.repository.ExamRepository
import com.example.ui.components.PremiumGatekeeperDialog
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PyqBankScreen(
    examRepository: ExamRepository,
    isPremiumUnlocked: Boolean = false,
    onNavigateToPayment: () -> Unit = {},
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showGatekeeperDialog by remember { mutableStateOf(false) }
    var gatekeeperFeature by remember { mutableStateOf("Full PYQ Solutions & AI Predictions") }

    val allQuestions = remember { examRepository.getAllPyqQuestions() }

    val filteredQuestions = when (selectedTab) {
        1 -> allQuestions.filter { it.tag.contains("PAST") }
        2 -> allQuestions.filter { it.tag.contains("FUTURE") }
        else -> allQuestions
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("AOEE PYQ & AI Prediction Bank", fontWeight = FontWeight.Bold, color = TextWhitePrimary) },
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
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = AoeeCardBg,
                    contentColor = ElectricCyan
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("All Sets (${allQuestions.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Past (2018-2025)", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = {
                            if (!isPremiumUnlocked) {
                                gatekeeperFeature = "2026-2027 AI Future Predictions"
                                showGatekeeperDialog = true
                            } else {
                                selectedTab = 2
                            }
                        },
                        text = { Text("Future (2026 AI)", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(filteredQuestions) { question ->
                        PyqQuestionCard(
                            question = question,
                            isPremiumUnlocked = isPremiumUnlocked,
                            onLockedFeatureClick = { feature ->
                                gatekeeperFeature = feature
                                showGatekeeperDialog = true
                            }
                        )
                    }
                }
            }
        }

        if (showGatekeeperDialog) {
            PremiumGatekeeperDialog(
                featureName = gatekeeperFeature,
                onDismiss = { showGatekeeperDialog = false },
                onNavigateToPayment = {
                    showGatekeeperDialog = false
                    onNavigateToPayment()
                }
            )
        }
    }
}

@Composable
fun PyqQuestionCard(
    question: Question,
    isPremiumUnlocked: Boolean = true,
    onLockedFeatureClick: (String) -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
        border = BorderStroke(1.dp, AoeeCardBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = DeepIndigo,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = question.subject,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanGlow,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    color = if (question.tag.contains("FUTURE")) DeepIndigo else SuccessEmerald.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, if (question.tag.contains("FUTURE")) ElectricCyan else SuccessEmerald)
                ) {
                    Text(
                        text = question.tag,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (question.tag.contains("FUTURE")) CyanGlow else SuccessEmerald,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = question.questionText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhitePrimary,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            val correctOptionText = question.options.getOrNull(question.correctOptionIndex) ?: ""
            val optionLabel = listOf("A", "B", "C", "D").getOrElse(question.correctOptionIndex) { "${question.correctOptionIndex + 1}" }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SuccessEmerald.copy(alpha = 0.12f))
                    .border(1.dp, SuccessEmerald, RoundedCornerShape(10.dp))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = SuccessEmerald,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Correct Answer ($optionLabel): $correctOptionText",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhitePrimary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (isPremiumUnlocked) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3EDF7)),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, AoeeCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = ElectricCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Punyansu AI Step-by-Step Explanation:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanGlow
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = question.explanation,
                                fontSize = 12.sp,
                                color = TextWhitePrimary,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            } else {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DeepIndigo),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onLockedFeatureClick("Step-by-Step AI Mathematical Solutions") }
                        .testTag("locked_solution_card_${question.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Punyansu AI Step-by-Step Solution 🔒",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent
                            )
                            Text(
                                text = "Tap to unlock full derivations, formulas & shortcuts with OAEE Pro",
                                fontSize = 10.5.sp,
                                color = TextMutedSecondary
                            )
                        }
                        Surface(
                            color = GoldAccent.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "PRO",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
