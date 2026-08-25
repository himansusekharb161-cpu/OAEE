package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class TimerDisplayMode {
    EXPANDED_PRESSURE_HUD,
    COMPACT_PILL,
    HIDDEN_ZEN
}

enum class ExamPaceStatus(val label: String, val color: Color, val icon: String) {
    AHEAD_OF_PACE("Ahead of Pace ⚡", SuccessEmerald, "⚡"),
    ON_TRACK("On Track ⏱️", ElectricCyan, "⏱️"),
    FALLING_BEHIND("Time Crunch ⚠️", PoliceRedAlert, "⚠️")
}

@Composable
fun ExamTimerComponent(
    totalSeconds: Int,
    timeRemainingSeconds: Int,
    currentQuestionIndex: Int,
    totalQuestions: Int,
    answeredCount: Int,
    currentQuestionSeconds: Int,
    displayMode: TimerDisplayMode,
    isPressureModeEnabled: Boolean,
    onToggleDisplayMode: (TimerDisplayMode) -> Unit,
    onTogglePressureMode: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = if (totalSeconds > 0) {
        (timeRemainingSeconds.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val isCriticalTime = timeRemainingSeconds <= 120 || progress <= 0.20f
    val isWarningTime = timeRemainingSeconds <= 300 && !isCriticalTime

    // Infinite pulsing animation for high exam pressure simulation
    val infiniteTransition = rememberInfiniteTransition(label = "pressure_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isPressureModeEnabled && isCriticalTime) 1.05f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = if (isPressureModeEnabled && isCriticalTime) 0.9f else 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    // Time calculations
    val minutes = timeRemainingSeconds / 60
    val seconds = timeRemainingSeconds % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    // Pace analysis
    val expectedSecondsPerQuestion = if (totalQuestions > 0) totalSeconds / totalQuestions else 60
    val timeSpentSoFar = totalSeconds - timeRemainingSeconds
    val actualAvgSecondsPerAnswer = if (answeredCount > 0) timeSpentSoFar / answeredCount else currentQuestionSeconds

    val paceStatus = when {
        answeredCount == 0 -> ExamPaceStatus.ON_TRACK
        actualAvgSecondsPerAnswer <= expectedSecondsPerQuestion - 10 -> ExamPaceStatus.AHEAD_OF_PACE
        actualAvgSecondsPerAnswer <= expectedSecondsPerQuestion + 15 -> ExamPaceStatus.ON_TRACK
        else -> ExamPaceStatus.FALLING_BEHIND
    }

    val timerColor = when {
        isCriticalTime -> PoliceRedAlert
        isWarningTime -> GoldAccent
        else -> CyanGlow
    }

    val containerBorderColor = when {
        isPressureModeEnabled && isCriticalTime -> PoliceRedAlert.copy(alpha = pulseAlpha)
        isCriticalTime -> PoliceRedAlert
        isWarningTime -> GoldAccent.copy(alpha = 0.8f)
        else -> ElectricCyan.copy(alpha = 0.4f)
    }

    Crossfade(targetState = displayMode, label = "timer_mode_transition") { mode ->
        when (mode) {
            TimerDisplayMode.EXPANDED_PRESSURE_HUD -> {
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .scale(pulseScale)
                        .testTag("exam_timer_expanded_hud"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCriticalTime && isPressureModeEnabled) {
                            Color(0xFF2A0D15)
                        } else {
                            AoeeCardBg
                        }
                    ),
                    border = BorderStroke(1.5.dp, containerBorderColor)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Header Bar with Toggle Modes
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(if (isCriticalTime) PoliceRedAlert.copy(alpha = 0.25f) else DeepIndigo),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isCriticalTime) Icons.Default.Warning else Icons.Default.Timer,
                                        contentDescription = null,
                                        tint = timerColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = if (isPressureModeEnabled) "⚡ EXAM PRESSURE MODE" else "⏱️ TIME MANAGEMENT HUD",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isCriticalTime) PoliceRedAlert else TextWhitePrimary,
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = if (isCriticalTime) "Urgent! Final minutes ticking" else "Real CBT exam simulation",
                                        fontSize = 10.sp,
                                        color = TextMutedSecondary
                                    )
                                }
                            }

                            // Quick View Toggle Controls
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { onToggleDisplayMode(TimerDisplayMode.COMPACT_PILL) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Compress,
                                        contentDescription = "Minimize Timer",
                                        tint = TextMutedSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { onToggleDisplayMode(TimerDisplayMode.HIDDEN_ZEN) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.VisibilityOff,
                                        contentDescription = "Hide Timer (Zen Mode)",
                                        tint = TextMutedSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Large Timer Countdown & Progress
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = formattedTime,
                                    fontSize = 34.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = timerColor,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Remaining of ${(totalSeconds / 60)} mins allocated",
                                    fontSize = 11.sp,
                                    color = TextMutedSecondary
                                )
                            }

                            // Dynamic Live Pace Pill
                            Surface(
                                color = paceStatus.color.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, paceStatus.color)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = paceStatus.label,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = paceStatus.color
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Linear Progress Bar
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = timerColor,
                            trackColor = Color(0xFF1E293B)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Real-Time Time Analytics Sub-Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Current Question Lap Time
                            Surface(
                                modifier = Modifier.weight(1f),
                                color = Color(0xFF131D31),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(
                                    1.dp,
                                    if (currentQuestionSeconds > 90) PoliceRedAlert.copy(alpha = 0.6f) else AoeeCardBorder
                                )
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = "Current Q#${currentQuestionIndex + 1} Lap",
                                        fontSize = 9.5.sp,
                                        color = TextMutedSecondary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${currentQuestionSeconds}s spent",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (currentQuestionSeconds > 90) PoliceRedAlert else CyanGlow
                                    )
                                }
                            }

                            // Average Speed per Question
                            Surface(
                                modifier = Modifier.weight(1f),
                                color = Color(0xFF131D31),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, AoeeCardBorder)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = "Avg Speed / Q",
                                        fontSize = 9.5.sp,
                                        color = TextMutedSecondary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${actualAvgSecondsPerAnswer}s / Q",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhitePrimary
                                    )
                                }
                            }

                            // Target Speed
                            Surface(
                                modifier = Modifier.weight(1f),
                                color = Color(0xFF131D31),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, AoeeCardBorder)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = "Target / Q",
                                        fontSize = 9.5.sp,
                                        color = TextMutedSecondary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${expectedSecondsPerQuestion}s target",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SuccessEmerald
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Toggle Real Exam Pressure Mode Switch
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF0F172A))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = if (isPressureModeEnabled) ElectricCyan else TextMutedSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Exam Pressure Pulse & Critical Alerts",
                                    fontSize = 11.sp,
                                    color = TextWhitePrimary
                                )
                            }
                            Switch(
                                checked = isPressureModeEnabled,
                                onCheckedChange = { onTogglePressureMode(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = ElectricCyan,
                                    uncheckedThumbColor = TextMutedSecondary,
                                    uncheckedTrackColor = Color(0xFF1E293B)
                                ),
                                modifier = Modifier.scale(0.75f)
                            )
                        }
                    }
                }
            }

            TimerDisplayMode.COMPACT_PILL -> {
                Surface(
                    modifier = modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onToggleDisplayMode(TimerDisplayMode.EXPANDED_PRESSURE_HUD) }
                        .scale(if (isPressureModeEnabled && isCriticalTime) pulseScale else 1f)
                        .testTag("exam_timer_compact_pill"),
                    color = if (isCriticalTime && isPressureModeEnabled) PoliceAlertBg else DeepIndigo,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.5.dp, containerBorderColor)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isCriticalTime) Icons.Default.Warning else Icons.Default.Timer,
                            contentDescription = null,
                            tint = timerColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = formattedTime,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = timerColor
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(paceStatus.color)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = paceStatus.icon,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            TimerDisplayMode.HIDDEN_ZEN -> {
                Surface(
                    modifier = modifier
                        .clip(CircleShape)
                        .clickable { onToggleDisplayMode(TimerDisplayMode.EXPANDED_PRESSURE_HUD) }
                        .testTag("exam_timer_zen_mode"),
                    color = DeepIndigo,
                    shape = CircleShape,
                    border = BorderStroke(1.dp, AoeeCardBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Visibility,
                            contentDescription = "Show Timer",
                            tint = CyanGlow,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Timer (Zen)",
                            fontSize = 11.sp,
                            color = CyanGlow,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
