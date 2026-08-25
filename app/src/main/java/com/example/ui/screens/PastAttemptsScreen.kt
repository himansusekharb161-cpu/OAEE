package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TestResultEntity
import com.example.data.model.ExamStream
import com.example.data.model.UserProfile
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

enum class AttemptSortOption(val label: String) {
    NEWEST_FIRST("Newest First"),
    HIGHEST_SCORE("Highest Score"),
    LOWEST_SCORE("Lowest Score")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PastAttemptsScreen(
    userProfile: UserProfile,
    testResults: List<TestResultEntity>,
    onBack: () -> Unit,
    onTakeTest: () -> Unit,
    onDeleteAttempt: (String) -> Unit,
    onClearAllAttempts: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilterStream by remember { mutableStateOf<ExamStream?>(null) }
    var sortOption by remember { mutableStateOf(AttemptSortOption.NEWEST_FIRST) }
    var selectedDetailAttempt by remember { mutableStateOf<TestResultEntity?>(null) }
    var showClearConfirmation by remember { mutableStateOf(false) }

    val filteredAttempts = remember(testResults, searchQuery, selectedFilterStream, sortOption) {
        testResults.filter { attempt ->
            val matchesQuery = searchQuery.isBlank() ||
                    attempt.testTitle.contains(searchQuery, ignoreCase = true) ||
                    attempt.streamCode.contains(searchQuery, ignoreCase = true)
            val matchesStream = selectedFilterStream == null || attempt.streamCode.equals(selectedFilterStream?.name, ignoreCase = true)
            matchesQuery && matchesStream
        }.let { list ->
            when (sortOption) {
                AttemptSortOption.NEWEST_FIRST -> list.sortedByDescending { it.timestamp }
                AttemptSortOption.HIGHEST_SCORE -> list.sortedByDescending { it.scorePercentage }
                AttemptSortOption.LOWEST_SCORE -> list.sortedBy { it.scorePercentage }
            }
        }
    }

    val totalAttempts = testResults.size
    val averageScore = if (totalAttempts > 0) testResults.map { it.scorePercentage }.average().toFloat() else 0f
    val highestScore = if (totalAttempts > 0) testResults.maxOf { it.scorePercentage } else 0f
    val totalTimeSeconds = testResults.sumOf { it.timeSpentSeconds }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Past Exam Attempts",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TextWhitePrimary
                        )
                        Text(
                            text = "Local Room Database History • $totalAttempts Total Attempts",
                            fontSize = 11.sp,
                            color = ElectricCyan
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextWhitePrimary)
                    }
                },
                actions = {
                    if (testResults.isNotEmpty()) {
                        IconButton(onClick = { showClearConfirmation = true }) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = "Clear History",
                                tint = PoliceRedAlert
                            )
                        }
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
            contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- TOP SUMMARY STATS CARD ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DeepIndigo),
                    border = BorderStroke(1.5.dp, ElectricCyan)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.HistoryEdu,
                                    contentDescription = null,
                                    tint = ElectricCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "EXAM ATTEMPTS OVERVIEW",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = CyanGlow,
                                    letterSpacing = 1.sp
                                )
                            }

                            Surface(
                                color = ElectricCyan.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = "ROOM DB PERSISTED",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricCyan,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            AttemptStatItem(
                                title = "Total Attempts",
                                value = "$totalAttempts",
                                icon = Icons.Default.AssignmentTurnedIn,
                                tint = ElectricCyan,
                                modifier = Modifier.weight(1f)
                            )
                            AttemptStatItem(
                                title = "Best Score",
                                value = "${highestScore.toInt()}%",
                                icon = Icons.Default.EmojiEvents,
                                tint = GoldAccent,
                                modifier = Modifier.weight(1f)
                            )
                            AttemptStatItem(
                                title = "Avg Score",
                                value = "${averageScore.toInt()}%",
                                icon = Icons.Default.Timeline,
                                tint = SuccessEmerald,
                                modifier = Modifier.weight(1f)
                            )
                            AttemptStatItem(
                                title = "Total Time",
                                value = "${(totalTimeSeconds / 60)}m",
                                icon = Icons.Default.Timer,
                                tint = CyanGlow,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // --- SEARCH & SORT / FILTER CONTROLS ---
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Search bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search test title or stream...", color = TextMutedSecondary, fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = ElectricCyan
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = TextMutedSecondary)
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_attempts_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = AoeeCardBorder,
                            focusedTextColor = TextWhitePrimary,
                            unfocusedTextColor = TextWhitePrimary,
                            focusedContainerColor = AoeeCardBg,
                            unfocusedContainerColor = AoeeCardBg
                        )
                    )

                    // Sort Chips & Filter Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AttemptSortOption.entries.forEach { option ->
                            val isSelected = sortOption == option
                            FilterChip(
                                selected = isSelected,
                                onClick = { sortOption = option },
                                label = { Text(option.label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                leadingIcon = {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = DeepIndigo,
                                    selectedLabelColor = ElectricCyan,
                                    selectedLeadingIconColor = ElectricCyan,
                                    containerColor = AoeeCardBg,
                                    labelColor = TextMutedSecondary
                                ),
                                border = BorderStroke(1.dp, if (isSelected) ElectricCyan else AoeeCardBorder)
                            )
                        }
                    }
                }
            }

            // --- LIST OF PAST EXAM ATTEMPTS ---
            if (filteredAttempts.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                        border = BorderStroke(1.dp, AoeeCardBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(DeepIndigo),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AssignmentLate,
                                    contentDescription = null,
                                    tint = ElectricCyan,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = if (testResults.isEmpty()) "No Past Exam Attempts Found" else "No Matching Attempts",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhitePrimary
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = if (testResults.isEmpty())
                                    "Take your first CBT Mock Test to record your scores, accuracy, and exam date locally in Room database."
                                else
                                    "Try adjusting your search keyword or sort filter to see past exam attempts.",
                                fontSize = 12.sp,
                                color = TextMutedSecondary,
                                modifier = Modifier.padding(horizontal = 12.dp),
                                lineHeight = 18.sp
                            )

                            if (testResults.isEmpty()) {
                                Spacer(modifier = Modifier.height(20.dp))
                                Button(
                                    onClick = onTakeTest,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                                    modifier = Modifier.testTag("start_first_mock_test_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Start CBT Mock Test",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                items(filteredAttempts, key = { it.id }) { attempt ->
                    PastExamAttemptCard(
                        attempt = attempt,
                        onClick = { selectedDetailAttempt = attempt },
                        onDelete = { onDeleteAttempt(attempt.id) },
                        onRetake = onTakeTest
                    )
                }
            }
        }
    }

    // --- DETAILED ATTEMPT MODAL DIALOG ---
    selectedDetailAttempt?.let { attempt ->
        val dateFormat = remember { SimpleDateFormat("EEEE, dd MMMM yyyy • hh:mm:ss a", Locale.getDefault()) }
        val formattedDate = remember(attempt.timestamp) { dateFormat.format(Date(attempt.timestamp)) }
        val minutes = attempt.timeSpentSeconds / 60
        val seconds = attempt.timeSpentSeconds % 60

        AlertDialog(
            onDismissRequest = { selectedDetailAttempt = null },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Exam Attempt Analysis",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = TextWhitePrimary
                    )
                    IconButton(
                        onClick = { selectedDetailAttempt = null },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMutedSecondary)
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = attempt.testTitle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan
                    )

                    // Date & Time Taken Card
                    Surface(
                        color = Color(0xFF131D31),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, AoeeCardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Event,
                                contentDescription = "Date Taken",
                                tint = GoldAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Date & Time Taken",
                                    fontSize = 10.5.sp,
                                    color = TextMutedSecondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = formattedDate,
                                    fontSize = 12.sp,
                                    color = TextWhitePrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Score Achieved Banner
                    Surface(
                        color = if (attempt.scorePercentage >= 50) SuccessEmerald.copy(alpha = 0.15f) else PoliceRedAlert.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if (attempt.scorePercentage >= 50) SuccessEmerald else PoliceRedAlert),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Score Achieved",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (attempt.scorePercentage >= 50) SuccessEmerald else PoliceRedAlert
                                )
                                Text(
                                    text = "${attempt.scorePercentage.toInt()}%",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhitePrimary
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${attempt.correctAnswers} / ${attempt.totalQuestions} Correct",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhitePrimary
                                )
                                Text(
                                    text = "${attempt.wrongAnswers} Wrong • ${attempt.unattempted} Skipped",
                                    fontSize = 11.sp,
                                    color = TextMutedSecondary
                                )
                            }
                        }
                    }

                    // Duration Taken
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Duration Utilized:", fontSize = 12.sp, color = TextMutedSecondary)
                        Text("${minutes}m ${seconds}s", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhitePrimary)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Examination Stream:", fontSize = 12.sp, color = TextMutedSecondary)
                        Text(attempt.streamCode, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
                    }

                    // AI Feedback Summary
                    if (attempt.aiFeedbackSummary.isNotBlank()) {
                        Text(
                            text = "Punyansu AI Assessment:",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanGlow
                        )
                        Text(
                            text = attempt.aiFeedbackSummary,
                            fontSize = 11.5.sp,
                            color = TextWhitePrimary,
                            lineHeight = 16.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedDetailAttempt = null },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Close", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = DeepIndigo,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // --- CLEAR ALL CONFIRMATION DIALOG ---
    if (showClearConfirmation) {
        AlertDialog(
            onDismissRequest = { showClearConfirmation = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = PoliceRedAlert)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Clear All Attempt History?", fontWeight = FontWeight.Bold, color = TextWhitePrimary, fontSize = 16.sp)
                }
            },
            text = {
                Text(
                    text = "Are you sure you want to delete all locally stored past exam attempts from Room database? This action cannot be undone.",
                    fontSize = 13.sp,
                    color = TextMutedSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onClearAllAttempts()
                        showClearConfirmation = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PoliceRedAlert),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Clear All", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmation = false }) {
                    Text("Cancel", color = TextMutedSecondary)
                }
            },
            containerColor = AoeeCardBg,
            shape = RoundedCornerShape(18.dp)
        )
    }
}

@Composable
fun PastExamAttemptCard(
    attempt: TestResultEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onRetake: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }
    val formattedDate = remember(attempt.timestamp) { dateFormat.format(Date(attempt.timestamp)) }
    val minutes = attempt.timeSpentSeconds / 60
    val seconds = attempt.timeSpentSeconds % 60

    val isPassed = attempt.scorePercentage >= 50f
    val scoreBadgeColor = if (attempt.scorePercentage >= 75f) SuccessEmerald else if (isPassed) GoldAccent else PoliceRedAlert

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("exam_attempt_card_${attempt.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
        border = BorderStroke(1.dp, AoeeCardBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Row: Title and Score Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = attempt.testTitle,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhitePrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Date and Time Taken badge
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = "Date Taken",
                            tint = CyanGlow,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formattedDate,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CyanGlow
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Score Percentage Badge
                Surface(
                    color = scoreBadgeColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, scoreBadgeColor)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${attempt.scorePercentage.toInt()}%",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = scoreBadgeColor
                        )
                        Text(
                            text = if (attempt.scorePercentage >= 75f) "DISTINCTION" else if (isPassed) "PASSED" else "NEEDS WORK",
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = scoreBadgeColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = AoeeCardBorder.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(10.dp))

            // Stats Sub-Row: Question Breakdown, Time Spent, Stream
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Correct Count
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Correct",
                            tint = SuccessEmerald,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${attempt.correctAnswers}/${attempt.totalQuestions}",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhitePrimary
                        )
                    }

                    // Wrong Count
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "Wrong",
                            tint = PoliceRedAlert,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${attempt.wrongAnswers}",
                            fontSize = 11.5.sp,
                            color = TextMutedSecondary
                        )
                    }

                    // Time Spent
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Time",
                            tint = GoldAccent,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${minutes}m ${seconds}s",
                            fontSize = 11.5.sp,
                            color = TextMutedSecondary
                        )
                    }
                }

                // Delete and View Details actions
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete Attempt",
                            tint = TextMutedSecondary.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "View Details",
                        tint = ElectricCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AttemptStatItem(
    title: String,
    value: String,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextWhitePrimary
        )
        Text(
            text = title,
            fontSize = 9.5.sp,
            color = TextMutedSecondary,
            maxLines = 1
        )
    }
}
