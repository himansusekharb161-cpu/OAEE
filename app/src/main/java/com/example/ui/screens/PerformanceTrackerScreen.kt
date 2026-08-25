package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TestResultEntity
import com.example.data.model.UserProfile
import com.example.ui.theme.*

import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerformanceTrackerScreen(
    userProfile: UserProfile,
    testResults: List<TestResultEntity>,
    totalStudyMinutes: Int,
    onBack: () -> Unit,
    onTakeTest: () -> Unit,
    onNavigateToPastAttempts: () -> Unit = {}
) {
    val totalTests = testResults.size
    val avgScore = if (totalTests > 0) testResults.map { it.scorePercentage }.average().toFloat() else 0f
    val readinessScore = (avgScore * 0.7f + minOf(totalStudyMinutes / 10f, 30f)).coerceIn(0f, 100f).toInt()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Performance & Readiness Dashboard", fontWeight = FontWeight.Bold, color = TextWhitePrimary) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Overall Readiness Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = DeepIndigo),
                    border = BorderStroke(1.5.dp, ElectricCyan)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(imageVector = Icons.Default.Speed, contentDescription = null, tint = ElectricCyan)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AOEE 2026 EXAM READINESS INDEX",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CyanGlow,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(ElectricCyan, SuccessEmerald)))
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(AoeeNavyBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$readinessScore%",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhitePrimary
                                )
                                Text(
                                    text = if (readinessScore >= 75) "EXCELLENT" else if (readinessScore >= 50) "ON TRACK" else "NEEDS PRACTICE",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricCyan
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            MetricChip("Tests Taken", "$totalTests", Icons.Default.Assignment)
                            MetricChip("Avg Score", "${avgScore.toInt()}%", Icons.Default.Grade)
                            MetricChip("Study Hours", "${(totalStudyMinutes / 60f).let { String.format("%.1f", it) }}h", Icons.Default.Timer)
                        }
                    }
                }
            }

            // Visual Progress Chart Card
            item {
                Text(
                    text = "SCORE PROGRESS TREND (VISUAL DATA)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElectricCyan,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                    border = BorderStroke(1.dp, AoeeCardBorder)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Mock Test Performance Timeline",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhitePrimary
                            )
                            Text(
                                text = "Last ${testResults.size.coerceAtLeast(1)} Tests",
                                fontSize = 11.sp,
                                color = CyanGlow
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        ScoreHistoryChart(
                            scores = if (testResults.isNotEmpty()) testResults.map { it.scorePercentage } else listOf(45f, 60f, 72f, 85f, 90f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                        )
                    }
                }
            }

            // Subject Mastery Breakdown
            item {
                Text(
                    text = "SUBJECT MASTERY BREAKDOWN",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElectricCyan,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                    border = BorderStroke(1.dp, AoeeCardBorder)
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        SubjectProgressBar("Physics & Mechanics", 0.82f, "82% Accuracy")
                        SubjectProgressBar("Chemistry & Reactions", 0.74f, "74% Accuracy")
                        SubjectProgressBar("Mathematics & Calculus", 0.88f, "88% Accuracy")
                        SubjectProgressBar("General Aptitude & Odia", 0.92f, "92% Accuracy")
                    }
                }
            }

            // Test History List
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "RECENT TEST HISTORY (${testResults.size})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan,
                        letterSpacing = 1.sp
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (testResults.isNotEmpty()) {
                            OutlinedButton(
                                onClick = onNavigateToPastAttempts,
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, ElectricCyan)
                            ) {
                                Text("View All", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
                            }
                        }

                        Button(
                            onClick = onTakeTest,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan)
                        ) {
                            Text("Take Test", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            if (testResults.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                        border = BorderStroke(1.dp, AoeeCardBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Default.AssignmentLate, contentDescription = null, tint = TextMutedSecondary, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No mock tests taken yet.", fontSize = 14.sp, color = TextWhitePrimary, fontWeight = FontWeight.Bold)
                            Text("Complete a CBT test to see score history and rank breakdown.", fontSize = 12.sp, color = TextMutedSecondary)
                        }
                    }
                }
            } else {
                items(testResults.take(5)) { result ->
                    val formattedDate = java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault()).format(java.util.Date(result.timestamp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToPastAttempts() },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                        border = BorderStroke(1.dp, AoeeCardBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = result.testTitle,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhitePrimary
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "Date: $formattedDate",
                                    fontSize = 11.sp,
                                    color = CyanGlow,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Correct: ${result.correctAnswers}/${result.totalQuestions} • Time: ${result.timeSpentSeconds / 60}m ${result.timeSpentSeconds % 60}s",
                                    fontSize = 11.5.sp,
                                    color = TextMutedSecondary
                                )
                            }

                            Surface(
                                color = if (result.scorePercentage >= 50) SuccessEmerald.copy(alpha = 0.15f) else PoliceRedAlert.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, if (result.scorePercentage >= 50) SuccessEmerald else PoliceRedAlert)
                            ) {
                                Text(
                                    text = "${result.scorePercentage.toInt()}%",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (result.scorePercentage >= 50) SuccessEmerald else PoliceRedAlert,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricChip(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhitePrimary)
        }
        Text(text = label, fontSize = 11.sp, color = TextMutedSecondary)
    }
}

@Composable
private fun SubjectProgressBar(subject: String, progress: Float, label: String) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = subject, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextWhitePrimary)
            Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = ElectricCyan,
            trackColor = Color(0xFFE8DEF8)
        )
    }
}

@Composable
fun ScoreHistoryChart(scores: List<Float>, modifier: Modifier = Modifier) {
    val maxScore = 100f
    Canvas(modifier = modifier) {
        if (scores.isEmpty()) return@Canvas

        val width = size.width
        val height = size.height
        val pointGap = if (scores.size > 1) width / (scores.size - 1) else width

        val path = Path()
        val points = mutableListOf<Offset>()

        scores.forEachIndexed { index, score ->
            val x = index * pointGap
            val y = height - (score / maxScore * height)
            points.add(Offset(x, y))
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        // Draw background grid lines
        for (i in 1..3) {
            val gridY = height * (i / 4f)
            drawLine(
                color = Color.White.copy(alpha = 0.1f),
                start = Offset(0f, gridY),
                end = Offset(width, gridY),
                strokeWidth = 1f
            )
        }

        // Draw connecting trend line
        drawPath(
            path = path,
            color = ElectricCyan,
            style = Stroke(width = 3.dp.toPx())
        )

        // Draw data points
        points.forEach { pt ->
            drawCircle(
                color = SuccessEmerald,
                radius = 5.dp.toPx(),
                center = pt
            )
            drawCircle(
                color = Color.White,
                radius = 2.dp.toPx(),
                center = pt
            )
        }
    }
}
