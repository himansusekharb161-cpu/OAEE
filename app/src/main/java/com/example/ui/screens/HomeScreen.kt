package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ExamStream
import com.example.data.model.UserProfile
import com.example.ui.components.PremiumGatekeeperDialog
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    userProfile: UserProfile,
    onNavigateToChat: () -> Unit,
    onNavigateToMockTest: () -> Unit,
    onNavigateToPastAttempts: () -> Unit,
    onNavigateToPyqBank: () -> Unit,
    onNavigateToPerformance: () -> Unit,
    onNavigateToStudyTimer: () -> Unit,
    onNavigateToFlashcards: () -> Unit,
    onNavigateToStudyInfo: () -> Unit,
    onNavigateToSecurity: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNavigateToUpiPayment: () -> Unit,
    onTriggerNotification: () -> Unit,
    onTriggerSafetyTest: () -> Unit,
    onLogout: () -> Unit
) {
    var showGatekeeperDialog by remember { mutableStateOf(false) }
    var gatedFeatureName by remember { mutableStateOf("") }

    // Gatekeeping mechanism: checks for 'Premium' status flag in user profile
    fun checkPremiumAndExecute(featureName: String, action: () -> Unit) {
        if (userProfile.isPremiumUnlocked) {
            action()
        } else {
            gatedFeatureName = featureName
            showGatekeeperDialog = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AoeeNavyBg)
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
        // --- TOP BAR & USER BRANDING ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.clickable { onNavigateToAuth() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(ElectricCyan, GoldAccent)))
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(AoeeCardBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = ElectricCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Namaskar, ${userProfile.name} 👋",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhitePrimary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Mail,
                                contentDescription = null,
                                tint = Color(0xFFEA4335),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (userProfile.isAuthenticated) userProfile.email else "Tap to Login / Switch Profile",
                                fontSize = 11.sp,
                                color = if (userProfile.isAuthenticated) SuccessEmerald else GoldAccent
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Quick UPI Upgrade Button
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onNavigateToUpiPayment() }
                            .testTag("top_upi_pay_button"),
                        color = if (userProfile.isPremiumUnlocked) SuccessEmerald.copy(alpha = 0.2f) else GoldAccent.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, if (userProfile.isPremiumUnlocked) SuccessEmerald else GoldAccent),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (userProfile.isPremiumUnlocked) Icons.Default.CheckCircle else Icons.Default.Stars,
                                contentDescription = "Pro Subscription",
                                tint = if (userProfile.isPremiumUnlocked) SuccessEmerald else GoldAccent,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (userProfile.isPremiumUnlocked) "PRO ACTIVE" else "PRO PASS ⭐",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (userProfile.isPremiumUnlocked) SuccessEmerald else GoldAccent
                            )
                        }
                    }

                    IconButton(
                        onClick = onTriggerNotification,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(AoeeCardBg)
                            .testTag("notification_bell_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Push Reminders",
                            tint = ElectricCyan
                        )
                    }

                    IconButton(
                        onClick = onNavigateToSecurity,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(AoeeCardBg)
                            .testTag("security_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Privacy & Security",
                            tint = GoldAccent
                        )
                    }
                }
            }
        }

        // --- HERO BANNER & STREAM BADGE ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = AoeeCardBg)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_aoee_hero_1785984441691),
                        contentDescription = "AOEE Hero Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color(0xEC0F172A))
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Surface(
                            color = GoldAccent,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = userProfile.selectedStream.displayName,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "AOEE Entrance Prep Portal",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Text(
                            text = "Powered by Punyansu AI Model • Class 1-12 to 55+ Yrs Lifelong Education",
                            fontSize = 11.sp,
                            color = CyanGlow
                        )
                    }
                }
            }
        }

        // --- AI MODEL STATUS & QUICK STATS BAR ---
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Punyansu AI status chip
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToPerformance() },
                    colors = CardDefaults.cardColors(containerColor = DeepIndigo),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color(0xFFD0BCFF))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Speed, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Exam Readiness",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanGlow
                            )
                            Text(
                                text = "Track Readiness",
                                fontSize = 10.sp,
                                color = TextMutedSecondary
                            )
                        }
                    }
                }

                // Security Shield chip
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToStudyTimer() },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3EDF7)),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, AoeeCardBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = ElectricCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Focus Timer ⏱️",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhitePrimary
                            )
                            Text(
                                text = "Pomodoro Mode",
                                fontSize = 10.sp,
                                color = SuccessEmerald
                            )
                        }
                    }
                }
            }
        }

        // --- CORE NAVIGATION GRID / TILES ---
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "FEATURE TILES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ElectricCyan,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. AI Chatbot Card
                MainFeatureCard(
                    title = "Punyansu AI Entrance Chatbot",
                    subtitle = "Ask Physics, Chemistry, Math, Biology doubts & Odia explanations in real-time.",
                    icon = Icons.Default.AutoAwesome,
                    badgeText = if (userProfile.isPremiumUnlocked) "PUNYANSU AI MODEL" else "🔒 PRO ACCESS",
                    badgeColor = if (userProfile.isPremiumUnlocked) ElectricCyan else GoldAccent,
                    cardBg = DeepIndigo,
                    testTag = "ai_chatbot_tile",
                    onClick = { checkPremiumAndExecute("Punyansu AI Entrance Doubt Solver", onNavigateToChat) }
                )

                // 1. Pro Subscription & Ad-Free Pass Card
                MainFeatureCard(
                    title = "⭐ OAEE Pro Subscription & Ad-Free Pass",
                    subtitle = "Instant 1-Click Pay & Unlock • All 16+ Odisha Entrance PYQs, Unlimited CBT Mock Tests & Punyansu AI Mentor.",
                    icon = Icons.Default.Stars,
                    badgeText = if (userProfile.isPremiumUnlocked) "ACTIVE PRO ✅" else "1-CLICK UNLOCK",
                    badgeColor = if (userProfile.isPremiumUnlocked) SuccessEmerald else GoldAccent,
                    cardBg = DeepIndigo,
                    testTag = "direct_bank_upi_tile",
                    onClick = onNavigateToUpiPayment
                )

                // 2. Candidate Login & Auth Portal
                MainFeatureCard(
                    title = "Candidate Authentication & Profile 🔐",
                    subtitle = "Sign in, update roll ID, set security PIN & choose target Odisha entrance stream.",
                    icon = Icons.Default.HowToReg,
                    badgeText = "LOGIN PORTAL",
                    badgeColor = GoldAccent,
                    cardBg = AoeeCardBg,
                    testTag = "auth_login_tile",
                    onClick = onNavigateToAuth
                )

                // 2. Exam Progress & Performance Dashboard
                MainFeatureCard(
                    title = "Exam Progress Dashboard",
                    subtitle = "Readiness score, score timeline, accuracy breakdown & rank prediction.",
                    icon = Icons.Default.Leaderboard,
                    badgeText = "PERFORMANCE ENGINE",
                    badgeColor = SuccessEmerald,
                    cardBg = AoeeCardBg,
                    testTag = "performance_dashboard_tile",
                    onClick = onNavigateToPerformance
                )

                // 3. Focus Study Timer
                MainFeatureCard(
                    title = "Focus Study Timer ⏱️",
                    subtitle = "Pomodoro study timer with subject breakdown & daily focus logging.",
                    icon = Icons.Default.Timer,
                    badgeText = "POMODORO TIMER",
                    badgeColor = ElectricCyan,
                    cardBg = AoeeCardBg,
                    testTag = "study_timer_tile",
                    onClick = onNavigateToStudyTimer
                )

                // 4. Freshcards System
                MainFeatureCard(
                    title = "Freshcards Active Recall System",
                    subtitle = "Interactive flip revision cards for core formulas, concepts & general knowledge.",
                    icon = Icons.Default.Style,
                    badgeText = if (userProfile.isPremiumUnlocked) "FLASHCARDS" else "🔒 PRO SETS",
                    badgeColor = GoldAccent,
                    cardBg = AoeeCardBg,
                    testTag = "flashcards_tile",
                    onClick = { checkPremiumAndExecute("Freshcards Active Recall System", onNavigateToFlashcards) }
                )

                // 5. CBT Mock Test Card
                MainFeatureCard(
                    title = "Past to Future Entrance Mock Test",
                    subtitle = "AI-generated CBT mock tests combining 2018-2025 past papers & 2026-2027 future predictions.",
                    icon = Icons.Default.Quiz,
                    badgeText = if (userProfile.isPremiumUnlocked) "CBT EXAM ENGINE" else "🔒 PRO ENGINE",
                    badgeColor = GoldAccent,
                    cardBg = AoeeCardBg,
                    testTag = "mock_test_tile",
                    onClick = { checkPremiumAndExecute("CBT Entrance Mock Test Engine", onNavigateToMockTest) }
                )

                // 6. Past Exam Attempts & Scores Card
                MainFeatureCard(
                    title = "Past Exam Attempts & History",
                    subtitle = "View all past CBT mock tests, scores achieved, exam dates & local Room database analytics.",
                    icon = Icons.Default.HistoryEdu,
                    badgeText = "ROOM DB PERSISTED",
                    badgeColor = ElectricCyan,
                    cardBg = DeepIndigo,
                    testTag = "past_attempts_tile",
                    onClick = onNavigateToPastAttempts
                )

                // 7. PYQ Question Bank
                MainFeatureCard(
                    title = "Previous Year & AI Prediction Bank",
                    subtitle = "Browse verified 2018-2025 AOEE entrance questions & AI predicted high-probability sets.",
                    icon = Icons.Default.MenuBook,
                    badgeText = if (userProfile.isPremiumUnlocked) "PYQ BANK" else "🔒 PRO BANK",
                    badgeColor = SuccessEmerald,
                    cardBg = AoeeCardBg,
                    testTag = "pyq_bank_tile",
                    onClick = { checkPremiumAndExecute("2018-2025 PYQ Question Bank & Solutions", onNavigateToPyqBank) }
                )

                // 7. Study Information & Guidelines
                MainFeatureCard(
                    title = "Exam Information & Guidelines",
                    subtitle = "Official exam schedule, syllabus breakdown, eligibility (Class 1-12 to 55+ Yrs) & counseling.",
                    icon = Icons.Default.Info,
                    badgeText = "EXAM GUIDE",
                    badgeColor = CyanGlow,
                    cardBg = AoeeCardBg,
                    testTag = "study_info_tile",
                    onClick = onNavigateToStudyInfo
                )

                // 8. Privacy & Security
                MainFeatureCard(
                    title = "Privacy 🔏 & Security Settings",
                    subtitle = "App lock passcode, local database security audit, privacy policies & safety logs.",
                    icon = Icons.Default.Shield,
                    badgeText = "SECURITY 🔏",
                    badgeColor = ElectricCyan,
                    cardBg = AoeeCardBg,
                    testTag = "security_settings_tile",
                    onClick = onNavigateToSecurity
                )

                // 9. Olta Question Trigger Test Button
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTriggerSafetyTest() },
                    colors = CardDefaults.cardColors(containerColor = PoliceAlertBg),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, PoliceAlertBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(PoliceRedAlert.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalPolice,
                                contentDescription = null,
                                tint = PoliceRedAlert,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Punyansu AI Safety Protocol Test",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PoliceRedAlert
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(PoliceRedAlert)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "DELHI POLICE ALERT",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                            Text(
                                text = "Test what happens if someone asks an 'Olta' / illegal question to Punyansu AI!",
                                fontSize = 11.sp,
                                color = TextMutedSecondary
                            )
                        }
                    }
                }
            }
        }
    }

    // Gatekeeping Dialog triggered if non-premium user taps a Pro feature
    if (showGatekeeperDialog) {
        PremiumGatekeeperDialog(
            featureName = gatedFeatureName,
            onDismiss = { showGatekeeperDialog = false },
            onNavigateToPayment = {
                showGatekeeperDialog = false
                onNavigateToUpiPayment()
            }
        )
    }
    }
}


@Composable
fun MainFeatureCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    badgeText: String,
    badgeColor: Color,
    cardBg: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, AoeeCardBorder)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(badgeColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = badgeColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    color = badgeColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = badgeColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhitePrimary
                )

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextMutedSecondary,
                    lineHeight = 16.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextMutedSecondary
            )
        }
    }
}
