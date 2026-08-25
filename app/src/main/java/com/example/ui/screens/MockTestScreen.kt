package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.data.local.TestResultEntity
import com.example.data.model.MockTest
import com.example.ui.components.ExamTimerComponent
import com.example.ui.components.TimerDisplayMode
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockTestScreen(
    mockTest: MockTest,
    onBack: () -> Unit,
    onFinishTest: (result: TestResultEntity) -> Unit
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<Int, Int>() }
    val questionTimeSecondsMap = remember { mutableStateMapOf<Int, Int>() }
    
    val totalSeconds = mockTest.durationMinutes * 60
    var timeRemainingSeconds by remember { mutableIntStateOf(totalSeconds) }
    var currentQuestionSeconds by remember { mutableIntStateOf(0) }
    
    var showSubmitDialog by remember { mutableStateOf(false) }
    var timerDisplayMode by remember { mutableStateOf(TimerDisplayMode.EXPANDED_PRESSURE_HUD) }
    var isPressureModeEnabled by remember { mutableStateOf(true) }

    val currentQuestion = mockTest.questions.getOrNull(currentQuestionIndex)

    // Master 1-second clock loop: handles countdown, lap time, and auto-submit
    LaunchedEffect(key1 = timeRemainingSeconds) {
        if (timeRemainingSeconds > 0) {
            delay(1000L)
            timeRemainingSeconds -= 1
            currentQuestionSeconds += 1
            questionTimeSecondsMap[currentQuestionIndex] = (questionTimeSecondsMap[currentQuestionIndex] ?: 0) + 1
        } else {
            showSubmitDialog = true
        }
    }

    // Reset current question timer when question index changes
    LaunchedEffect(key1 = currentQuestionIndex) {
        currentQuestionSeconds = questionTimeSecondsMap[currentQuestionIndex] ?: 0
    }

    fun submitExam() {
        var correct = 0
        var wrong = 0
        var unattempted = 0

        mockTest.questions.forEachIndexed { index, question ->
            val selected = userAnswers[index]
            if (selected == null) {
                unattempted++
            } else if (selected == question.correctOptionIndex) {
                correct++
            } else {
                wrong++
            }
        }

        val total = mockTest.questions.size
        val scorePct = if (total > 0) (correct.toFloat() / total.toFloat()) * 100f else 0f
        val timeSpent = (totalSeconds - timeRemainingSeconds).coerceAtLeast(1)
        val avgSecPerQ = if (userAnswers.isNotEmpty()) timeSpent / userAnswers.size else 0

        val speedPaceGrade = when {
            avgSecPerQ in 1..45 -> "⚡ Lightning Fast Pace (${avgSecPerQ}s/q)"
            avgSecPerQ in 46..75 -> "🎯 Optimal AOEE Timing (${avgSecPerQ}s/q)"
            else -> "⏳ Deliberate / Time Intensive (${avgSecPerQ}s/q)"
        }

        val summary = when {
            scorePct >= 80 -> "🌟 Outstanding Performance! $speedPaceGrade. High readiness for AOEE Rank #1!"
            scorePct >= 50 -> "👍 Good Effort! $speedPaceGrade. Revise tricky questions and practice pace control with Punyansu AI."
            else -> "📚 Keep Practicing! $speedPaceGrade. Use Punyansu AI to master question shortcuts and speed drills."
        }

        val resultEntity = TestResultEntity(
            id = UUID.randomUUID().toString(),
            testTitle = mockTest.title,
            streamCode = mockTest.stream.code,
            totalQuestions = total,
            correctAnswers = correct,
            wrongAnswers = wrong,
            unattempted = unattempted,
            scorePercentage = scorePct,
            timeSpentSeconds = timeSpent.toLong(),
            timestamp = System.currentTimeMillis(),
            aiFeedbackSummary = summary
        )

        onFinishTest(resultEntity)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = mockTest.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhitePrimary
                        )
                        Text(
                            text = "AOEE CBT Examination Mode",
                            fontSize = 11.sp,
                            color = ElectricCyan
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { showSubmitDialog = true }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextWhitePrimary)
                    }
                },
                actions = {
                    // Header Timer Component (Syncs with user toggle mode)
                    if (timerDisplayMode != TimerDisplayMode.EXPANDED_PRESSURE_HUD) {
                        ExamTimerComponent(
                            totalSeconds = totalSeconds,
                            timeRemainingSeconds = timeRemainingSeconds,
                            currentQuestionIndex = currentQuestionIndex,
                            totalQuestions = mockTest.questions.size,
                            answeredCount = userAnswers.size,
                            currentQuestionSeconds = currentQuestionSeconds,
                            displayMode = timerDisplayMode,
                            isPressureModeEnabled = isPressureModeEnabled,
                            onToggleDisplayMode = { timerDisplayMode = it },
                            onTogglePressureMode = { isPressureModeEnabled = it },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    } else {
                        // Quick toggle button in top bar to collapse timer
                        IconButton(onClick = { timerDisplayMode = TimerDisplayMode.COMPACT_PILL }) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Timer Settings",
                                tint = CyanGlow
                            )
                        }
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            if (currentQuestionIndex > 0) currentQuestionIndex--
                        },
                        enabled = currentQuestionIndex > 0,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Previous")
                    }

                    Button(
                        onClick = { showSubmitDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessEmerald),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("submit_cbt_test_button")
                    ) {
                        Text("Submit Exam", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Button(
                        onClick = {
                            if (currentQuestionIndex < mockTest.questions.size - 1) currentQuestionIndex++
                        },
                        enabled = currentQuestionIndex < mockTest.questions.size - 1,
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Next", color = Color.Black)
                    }
                }
            }
        },
        containerColor = AoeeNavyBg
    ) { paddingValues ->
        if (currentQuestion != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // Timer Mode Toggle Bar (Pressure HUD, Compact, Zen Hide)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EXAM TIMER & PRESSURE HUD:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMutedSecondary
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        FilterChip(
                            selected = timerDisplayMode == TimerDisplayMode.EXPANDED_PRESSURE_HUD,
                            onClick = { timerDisplayMode = TimerDisplayMode.EXPANDED_PRESSURE_HUD },
                            label = { Text("⚡ Pressure HUD", fontSize = 10.5.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElectricCyan,
                                selectedLabelColor = Color.White,
                                containerColor = DeepIndigo.copy(alpha = 0.4f),
                                labelColor = TextMutedSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = timerDisplayMode == TimerDisplayMode.EXPANDED_PRESSURE_HUD,
                                borderColor = AoeeCardBorder,
                                selectedBorderColor = ElectricCyan
                            ),
                            modifier = Modifier.testTag("toggle_pressure_hud_chip")
                        )

                        FilterChip(
                            selected = timerDisplayMode == TimerDisplayMode.COMPACT_PILL,
                            onClick = { timerDisplayMode = TimerDisplayMode.COMPACT_PILL },
                            label = { Text("⏱️ Compact", fontSize = 10.5.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElectricCyan,
                                selectedLabelColor = Color.White,
                                containerColor = DeepIndigo.copy(alpha = 0.4f),
                                labelColor = TextMutedSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = timerDisplayMode == TimerDisplayMode.COMPACT_PILL,
                                borderColor = AoeeCardBorder,
                                selectedBorderColor = ElectricCyan
                            )
                        )

                        FilterChip(
                            selected = timerDisplayMode == TimerDisplayMode.HIDDEN_ZEN,
                            onClick = { timerDisplayMode = TimerDisplayMode.HIDDEN_ZEN },
                            label = { Text("🧘 Zen", fontSize = 10.5.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElectricCyan,
                                selectedLabelColor = Color.White,
                                containerColor = DeepIndigo.copy(alpha = 0.4f),
                                labelColor = TextMutedSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = timerDisplayMode == TimerDisplayMode.HIDDEN_ZEN,
                                borderColor = AoeeCardBorder,
                                selectedBorderColor = ElectricCyan
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // The Toggleable Exam Timer Component (when expanded)
                if (timerDisplayMode == TimerDisplayMode.EXPANDED_PRESSURE_HUD) {
                    ExamTimerComponent(
                        totalSeconds = totalSeconds,
                        timeRemainingSeconds = timeRemainingSeconds,
                        currentQuestionIndex = currentQuestionIndex,
                        totalQuestions = mockTest.questions.size,
                        answeredCount = userAnswers.size,
                        currentQuestionSeconds = currentQuestionSeconds,
                        displayMode = timerDisplayMode,
                        isPressureModeEnabled = isPressureModeEnabled,
                        onToggleDisplayMode = { timerDisplayMode = it },
                        onTogglePressureMode = { isPressureModeEnabled = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Question Navigation Ribbon
                Text(
                    text = "Question Navigator:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMutedSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(mockTest.questions) { index, _ ->
                        val isCurrent = index == currentQuestionIndex
                        val isAnswered = userAnswers.containsKey(index)
                        val questionSecs = questionTimeSecondsMap[index] ?: 0

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isCurrent -> ElectricCyan
                                            isAnswered -> SuccessEmerald
                                            else -> Color(0xFF1E293B)
                                        }
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isCurrent) ElectricCyan else AoeeCardBorder,
                                        shape = CircleShape
                                    )
                                    .clickable { currentQuestionIndex = index },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrent || isAnswered) Color.White else TextWhitePrimary
                                )
                            }

                            if (questionSecs > 0) {
                                Text(
                                    text = "${questionSecs}s",
                                    fontSize = 9.sp,
                                    color = if (questionSecs > 90) PoliceRedAlert else TextMutedSecondary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Active Question Card
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
                            Surface(
                                color = DeepIndigo,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = currentQuestion.subject,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyanGlow,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            Surface(
                                color = if (currentQuestion.tag.contains("FUTURE")) DeepIndigo else SuccessEmerald.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(1.dp, if (currentQuestion.tag.contains("FUTURE")) ElectricCyan else SuccessEmerald)
                            ) {
                                Text(
                                    text = currentQuestion.tag,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (currentQuestion.tag.contains("FUTURE")) CyanGlow else SuccessEmerald,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Q${currentQuestionIndex + 1}. ${currentQuestion.questionText}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhitePrimary,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        val selectedOption = userAnswers[currentQuestionIndex]

                        currentQuestion.options.forEachIndexed { optionIndex, optionText ->
                            val isSelected = selectedOption == optionIndex
                            val optionLabel = listOf("A", "B", "C", "D").getOrElse(optionIndex) { "${optionIndex + 1}" }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) DeepIndigo else Color(0xFF131D31))
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) ElectricCyan else AoeeCardBorder,
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .clickable {
                                        userAnswers[currentQuestionIndex] = optionIndex
                                    }
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) ElectricCyan else Color(0xFF1E293B)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = optionLabel,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else TextWhitePrimary
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = optionText,
                                    fontSize = 14.sp,
                                    color = TextWhitePrimary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showSubmitDialog) {
        AlertDialog(
            onDismissRequest = { showSubmitDialog = false },
            title = {
                Text(text = "Submit AOEE Mock Test?", fontWeight = FontWeight.Bold)
            },
            text = {
                val answeredCount = userAnswers.size
                val totalCount = mockTest.questions.size
                val timeUsedMinutes = (totalSeconds - timeRemainingSeconds) / 60
                val timeUsedSeconds = (totalSeconds - timeRemainingSeconds) % 60
                Text(
                    "You have answered $answeredCount of $totalCount questions in ${timeUsedMinutes}m ${timeUsedSeconds}s.\n\n" +
                            "Are you ready to submit and analyze your score and time management skills?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitDialog = false
                        submitExam()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessEmerald)
                ) {
                    Text("Confirm Submit")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showSubmitDialog = false }) {
                    Text("Continue Exam")
                }
            },
            containerColor = AoeeCardBg,
            titleContentColor = Color.White,
            textContentColor = TextMutedSecondary
        )
    }
}
