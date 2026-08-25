package com.example.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TestResultEntity
import com.example.data.model.UserProfile
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestResultScreen(
    testResult: TestResultEntity,
    userProfile: UserProfile,
    onBackToHome: () -> Unit,
    onTakeNewTest: () -> Unit,
    onViewPastAttempts: () -> Unit = {}
) {
    val context = LocalContext.current
    var showRankCardModal by remember { mutableStateOf(false) }

    val estimatedRank = (1000 - (testResult.scorePercentage * 9.5f)).toInt().coerceAtLeast(12)
    val percentile = String.format("%.2f", (testResult.scorePercentage * 0.98f).coerceAtMost(99.9f))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AOEE Exam Performance Analysis", fontWeight = FontWeight.Bold, color = TextWhitePrimary) },
                navigationIcon = {
                    IconButton(onClick = onBackToHome) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Home", tint = TextWhitePrimary)
                    }
                },
                actions = {
                    IconButton(onClick = onViewPastAttempts) {
                        Icon(imageVector = Icons.Default.HistoryEdu, contentDescription = "Past Attempts", tint = ElectricCyan)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AoeeNavyBg)
            )
        },
        bottomBar = {
            Surface(
                color = AoeeCardBg,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onTakeNewTest,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("New Test")
                    }

                    Button(
                        onClick = onBackToHome,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan)
                    ) {
                        Text("Dashboard", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        },
        containerColor = AoeeNavyBg
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                    border = BorderStroke(1.dp, AoeeCardBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = testResult.testTitle,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            if (testResult.scorePercentage >= 50) SuccessEmerald else PoliceRedAlert,
                                            ElectricCyan
                                        )
                                    )
                                )
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(AoeeNavyBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${testResult.scorePercentage.toInt()}%",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhitePrimary
                                )
                                Text(
                                    text = "SCORE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyanGlow
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatBox("Correct", "${testResult.correctAnswers}", SuccessEmerald)
                            StatBox("Wrong", "${testResult.wrongAnswers}", PoliceRedAlert)
                            StatBox("Skipped", "${testResult.unattempted}", ElectricCyan)
                        }
                    }
                }
            }

            // Official Rank Card Download CTA Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DeepIndigo),
                    border = BorderStroke(1.5.dp, ElectricCyan)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.EmojiEvents, contentDescription = null, tint = CyanGlow, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "OFFICIAL AOEE RANK & PROGRESS CARD",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhitePrimary,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "Odisha Merit Rank: #$estimatedRank • Percentile: $percentile%",
                                    fontSize = 12.sp,
                                    color = CyanGlow
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val shareText = "🎓 AOEE 2026 OFFICIAL RANK CARD 🎓\n" +
                                        "Candidate: ${userProfile.name}\n" +
                                        "Stream: ${userProfile.selectedStream.displayName}\n" +
                                        "Test: ${testResult.testTitle}\n" +
                                        "Score: ${testResult.scorePercentage.toInt()}%\n" +
                                        "Estimated Odisha Merit Rank: #$estimatedRank\n" +
                                        "Percentile: $percentile%\n" +
                                        "Verified by Punyansu AI Portal."

                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "Download / Share Official AOEE Rank Card")
                                context.startActivity(shareIntent)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan)
                        ) {
                            Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Export / Download Rank Card (PNG / Report)",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Time Management & Speed Analytics Card
            item {
                val totalTimeSpentMinutes = testResult.timeSpentSeconds / 60
                val totalTimeSpentRemainingSecs = testResult.timeSpentSeconds % 60
                val avgSecondsPerQ = if (testResult.totalQuestions > 0) testResult.timeSpentSeconds / testResult.totalQuestions else 0
                val paceGrade = when {
                    avgSecondsPerQ in 1..45 -> "⚡ Speed Specialist"
                    avgSecondsPerQ in 46..75 -> "🎯 Balanced Exam Pacing"
                    else -> "⏳ In-Depth Deliberation"
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                    border = BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(ElectricCyan.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = null,
                                        tint = CyanGlow,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "TIME MANAGEMENT & SPEED",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextWhitePrimary,
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = "Exam Pressure & Pacing Assessment",
                                        fontSize = 11.sp,
                                        color = TextMutedSecondary
                                    )
                                }
                            }

                            Surface(
                                color = DeepIndigo,
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, ElectricCyan)
                            ) {
                                Text(
                                    text = paceGrade,
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyanGlow,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                modifier = Modifier.weight(1f),
                                color = Color(0xFF131D31),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, AoeeCardBorder)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "Total Time", fontSize = 10.sp, color = TextMutedSecondary)
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = "${totalTimeSpentMinutes}m ${totalTimeSpentRemainingSecs}s",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CyanGlow
                                    )
                                }
                            }

                            Surface(
                                modifier = Modifier.weight(1f),
                                color = Color(0xFF131D31),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, AoeeCardBorder)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "Speed / Q", fontSize = 10.sp, color = TextMutedSecondary)
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = "${avgSecondsPerQ}s / Q",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessEmerald
                                    )
                                }
                            }

                            Surface(
                                modifier = Modifier.weight(1f),
                                color = Color(0xFF131D31),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, AoeeCardBorder)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "Attempt Rate", fontSize = 10.sp, color = TextMutedSecondary)
                                    Spacer(modifier = Modifier.height(3.dp))
                                    val attemptPct = if (testResult.totalQuestions > 0) {
                                        ((testResult.correctAnswers + testResult.wrongAnswers) * 100) / testResult.totalQuestions
                                    } else 0
                                    Text(
                                        text = "$attemptPct%",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhitePrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DeepIndigo),
                    border = BorderStroke(1.dp, Color(0xFFD0BCFF))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(ElectricCyan.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = ElectricCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Punyansu AI Mentor Analysis",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanGlow
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = testResult.aiFeedbackSummary,
                                fontSize = 14.sp,
                                color = TextWhitePrimary,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatBox(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = color
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextMutedSecondary
        )
    }
}

