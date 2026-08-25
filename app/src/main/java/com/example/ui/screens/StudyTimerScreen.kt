package com.example.ui.screens

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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.StudySessionEntity
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyTimerScreen(
    sessions: List<StudySessionEntity>,
    onSaveSession: (StudySessionEntity) -> Unit,
    onBack: () -> Unit
) {
    val subjects = listOf("Physics", "Chemistry", "Mathematics", "Odia GK", "General Aptitude", "Adult OSOU")
    var selectedSubject by remember { mutableStateOf(subjects.first()) }
    var targetMinutes by remember { mutableStateOf(25) }
    
    var timeRemainingSeconds by remember { mutableStateOf(targetMinutes * 60) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(targetMinutes) {
        if (!isRunning) {
            timeRemainingSeconds = targetMinutes * 60
        }
    }

    LaunchedEffect(isRunning, timeRemainingSeconds) {
        if (isRunning && timeRemainingSeconds > 0) {
            delay(1000L)
            timeRemainingSeconds--
        } else if (isRunning && timeRemainingSeconds == 0) {
            isRunning = false
            // Save session
            val completedSession = StudySessionEntity(
                id = UUID.randomUUID().toString(),
                subject = selectedSubject,
                durationMinutes = targetMinutes,
                timestamp = System.currentTimeMillis(),
                sessionType = "$targetMinutes-Min Focus"
            )
            onSaveSession(completedSession)
            timeRemainingSeconds = targetMinutes * 60
        }
    }

    val minutes = timeRemainingSeconds / 60
    val seconds = timeRemainingSeconds % 60
    val timerText = String.format("%02d:%02d", minutes, seconds)
    val progress = (timeRemainingSeconds.toFloat() / (targetMinutes * 60f)).coerceIn(0f, 1f)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Focus Study Timer ⏱️", fontWeight = FontWeight.Bold, color = TextWhitePrimary) },
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
            // Subject & Target Picker
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                    border = BorderStroke(1.dp, AoeeCardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "SELECT STUDY SUBJECT:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            subjects.take(3).forEach { sub ->
                                FilterChip(
                                    selected = selectedSubject == sub,
                                    onClick = { selectedSubject = sub },
                                    label = { Text(sub, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = DeepIndigo,
                                        selectedLabelColor = CyanGlow
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "SESSION DURATION:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(15, 25, 45, 60).forEach { mins ->
                                Button(
                                    onClick = {
                                        targetMinutes = mins
                                        isRunning = false
                                        timeRemainingSeconds = mins * 60
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (targetMinutes == mins) ElectricCyan else Color(0xFFF3EDF7)
                                    ),
                                    contentPadding = PaddingValues(vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "${mins}m",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (targetMinutes == mins) Color.White else TextWhitePrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Main Circular Clock Widget
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
                            text = "CURRENT SESSION: $selectedSubject",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhitePrimary
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(ElectricCyan, SuccessEmerald)
                                    )
                                )
                                .padding(8.dp)
                                .clip(CircleShape)
                                .background(AoeeNavyBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = timerText,
                                    fontSize = 44.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextWhitePrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isRunning) "FOCUS MODE ACTIVE" else "PAUSED",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isRunning) SuccessEmerald else CyanGlow
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { isRunning = !isRunning },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isRunning) PoliceRedAlert else SuccessEmerald
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isRunning) "Pause Focus" else "Start Focus",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    isRunning = false
                                    timeRemainingSeconds = targetMinutes * 60
                                },
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = TextWhitePrimary)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Reset Timer", color = TextWhitePrimary)
                            }
                        }
                    }
                }
            }

            // Completed Sessions Log
            item {
                Text(
                    text = "LOGGED FOCUS SESSIONS (${sessions.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElectricCyan,
                    letterSpacing = 1.sp
                )
            }

            if (sessions.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                        border = BorderStroke(1.dp, AoeeCardBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircleOutline, contentDescription = null, tint = TextMutedSecondary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("No study sessions logged today. Start timer to record focus hours!", fontSize = 12.sp, color = TextMutedSecondary)
                        }
                    }
                }
            } else {
                items(sessions) { session ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                        border = BorderStroke(1.dp, AoeeCardBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(SuccessEmerald.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = SuccessEmerald, modifier = Modifier.size(18.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(text = session.subject, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhitePrimary)
                                    Text(text = session.sessionType, fontSize = 11.sp, color = TextMutedSecondary)
                                }
                            }

                            Text(
                                text = "+${session.durationMinutes} Min",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SuccessEmerald
                            )
                        }
                    }
                }
            }
        }
    }
}
