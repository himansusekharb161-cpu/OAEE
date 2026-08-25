package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.data.repository.ExamCategoryProvider
import com.example.data.repository.ExamRepository
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamCategoryDashboardScreen(
    initialStream: ExamStream = ExamStream.CT_ENTRANCE,
    examRepository: ExamRepository,
    isPremiumUnlocked: Boolean = false,
    onStartMockTestForStream: (ExamStream) -> Unit,
    onFeedQuestionToAi: (Question) -> Unit,
    onNavigateToPyqBank: () -> Unit,
    onNavigateToFlashcards: () -> Unit,
    onNavigateToPayment: () -> Unit,
    onBack: () -> Unit
) {
    var selectedStream by remember { mutableStateOf(initialStream) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var showStreamPicker by remember { mutableStateOf(false) }

    val categoryDetail = remember(selectedStream) {
        ExamCategoryProvider.getCategoryDetailForStream(selectedStream)
    }

    val streamMockTests = remember(selectedStream) {
        examRepository.getMockTestsForStream(selectedStream)
    }

    val tabs = listOf(
        "🚀 Practice Modules",
        "📑 Syllabus & Topics",
        "📊 Pattern & Cutoffs",
        "🎓 Eligibility & Age",
        "🤖 AI 2026 Forecast"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = categoryDetail.shortCode,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhitePrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = getTierColor(categoryDetail.tier).copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, getTierColor(categoryDetail.tier)),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = categoryDetail.tier,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = getTierColor(categoryDetail.tier),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = categoryDetail.odiaTitle,
                            fontSize = 11.sp,
                            color = CyanGlow,
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("category_dashboard_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextWhitePrimary
                        )
                    }
                },
                actions = {
                    // Switch Category Stream Button
                    Button(
                        onClick = { showStreamPicker = true },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepIndigo),
                        border = BorderStroke(1.dp, AoeeCardBorder),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("switch_exam_stream_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Switch Exam",
                            tint = ElectricCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Switch Exam", fontSize = 11.sp, color = TextWhitePrimary, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AoeeNavyBg)
            )
        },
        containerColor = AoeeNavyBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AoeeNavyBg)
        ) {
            // --- TOP CATEGORY QUICK SWITCHER CHIPS ---
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AoeeNavySurface)
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val featuredStreams = listOf(
                    ExamStream.CT_ENTRANCE,
                    ExamStream.ENGINEERING,
                    ExamStream.NAVODAYA_ENTRANCE,
                    ExamStream.PATHANI_SAMANTA,
                    ExamStream.OAV_ENTRANCE,
                    ExamStream.DIPLOMA_DET,
                    ExamStream.OAS_IAS_CIVIL,
                    ExamStream.ADULT_CONTINUING_ED,
                    ExamStream.TEACHER_OTET,
                    ExamStream.MEDICAL_PHARMA,
                    ExamStream.GRADUATION_CPET,
                    ExamStream.SCHOOL_FOUNDATION
                )

                items(featuredStreams) { stream ->
                    val isSelected = stream == selectedStream
                    val streamDetail = ExamCategoryProvider.getCategoryDetailForStream(stream)
                    val tierColor = getTierColor(streamDetail.tier)

                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedStream = stream },
                        label = {
                            Text(
                                text = streamDetail.shortCode,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        leadingIcon = if (isSelected) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = if (isSelected) Color.Black else tierColor
                                )
                            }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = AoeeCardBg,
                            labelColor = TextWhitePrimary,
                            selectedContainerColor = tierColor,
                            selectedLabelColor = Color.Black
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) tierColor else AoeeCardBorder
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            // --- TAB SELECTOR ---
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = AoeeCardBg,
                contentColor = ElectricCyan,
                edgePadding = 12.dp,
                divider = { HorizontalDivider(color = AoeeCardBorder) }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) ElectricCyan else TextMutedSecondary
                            )
                        }
                    )
                }
            }

            // --- TAB CONTENT BODY ---
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. HERO CATEGORY BANNER
                item {
                    CategoryHeroCard(
                        detail = categoryDetail,
                        onOpenSwitcher = { showStreamPicker = true }
                    )
                }

                when (selectedTab) {
                    0 -> {
                        // TAB 0: PRACTICE MODULES & RAPID QUIZ
                        item {
                            Text(
                                text = "🎯 EXAM PRACTICE MODULES",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanGlow,
                                letterSpacing = 1.sp
                            )
                        }

                        item {
                            PracticeModulesGrid(
                                detail = categoryDetail,
                                isPremiumUnlocked = isPremiumUnlocked,
                                onStartCbtTest = { onStartMockTestForStream(selectedStream) },
                                onOpenFlashcards = onNavigateToFlashcards,
                                onOpenPyqBank = onNavigateToPyqBank,
                                onOpenPayment = onNavigateToPayment
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "⚡ HIGH-YIELD SPEED DRILL (${categoryDetail.shortCode})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent,
                                letterSpacing = 1.sp
                            )
                        }

                        // Embedded Interactive Practice Drill with real stream questions
                        item {
                            val questions = streamMockTests.flatMap { it.questions }
                            if (questions.isNotEmpty()) {
                                InteractiveSpeedQuizCard(
                                    questions = questions,
                                    examTitle = categoryDetail.fullTitle,
                                    onFeedToAi = onFeedQuestionToAi
                                )
                            } else {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Speed drills active! Launch the Full CBT Mock Test below for complete timed assessment.",
                                        modifier = Modifier.padding(16.dp),
                                        color = TextMutedSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        // Key Formula & Concept Flashcards Deck
                        if (categoryDetail.keyFormulas.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "🃏 KEY FORMULAS & HIGH-RECALL CONCEPTS",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricCyan,
                                    letterSpacing = 1.sp
                                )
                            }

                            items(categoryDetail.keyFormulas) { (topic, formula) ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, AoeeCardBorder),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lightbulb,
                                            contentDescription = null,
                                            tint = GoldAccent,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = topic,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = CyanGlow
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = formula,
                                                fontSize = 12.sp,
                                                color = TextWhitePrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        // TAB 1: SYLLABUS & TOPICS
                        item {
                            Text(
                                text = "📑 SUBJECT WEIGHTAGE & MARKS DISTRIBUTION",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanGlow,
                                letterSpacing = 1.sp
                            )
                        }

                        items(categoryDetail.subjects) { subject ->
                            SubjectWeightageCard(subject = subject)
                        }

                        item {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "📚 DETAILED SYLLABUS MODULES & CHAPTERS",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ElectricCyan,
                                letterSpacing = 1.sp
                            )
                        }

                        items(categoryDetail.syllabusModules) { module ->
                            SyllabusModuleCard(module = module)
                        }
                    }

                    2 -> {
                        // TAB 2: PATTERN & CUTOFFS
                        item {
                            Text(
                                text = "📊 SECTION-WISE EXAM STRUCTURE & TIME ALLOTMENT",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanGlow,
                                letterSpacing = 1.sp
                            )
                        }

                        items(categoryDetail.patternSections) { section ->
                            PatternSectionCard(section = section)
                        }

                        item {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "🎯 PAST YEARS CUTOFF MARKS & SAFE TARGETS",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent,
                                letterSpacing = 1.sp
                            )
                        }

                        items(categoryDetail.cutoffStats) { stat ->
                            CutoffStatCard(stat = stat)
                        }
                    }

                    3 -> {
                        // TAB 3: ELIGIBILITY & AGE CRITERIA
                        item {
                            Text(
                                text = "🎓 ELIGIBILITY, QUALIFICATIONS & AGE LIMITS",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanGlow,
                                letterSpacing = 1.sp
                            )
                        }

                        item {
                            EligibilityOverviewCard(detail = categoryDetail)
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "📋 DETAILED ELIGIBILITY RULES & RESERVATION POLICIES",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ElectricCyan,
                                letterSpacing = 1.sp
                            )
                        }

                        items(categoryDetail.eligibilityRules) { rule ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, AoeeCardBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = SuccessEmerald,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = rule,
                                        fontSize = 12.sp,
                                        color = TextWhitePrimary,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }

                    4 -> {
                        // TAB 4: PUNYANSU AI 2026 FORECAST & TIMELINE
                        item {
                            Text(
                                text = "🤖 PUNYANSU AI 2026-2027 EXAM PREDICTIONS",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent,
                                letterSpacing = 1.sp
                            )
                        }

                        items(categoryDetail.aiPredictions2026) { prediction ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = DeepIndigo),
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, Color(0xFFD0BCFF).copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = ElectricCyan,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Punyansu AI Deep Insight",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CyanGlow
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = prediction,
                                            fontSize = 12.sp,
                                            color = TextWhitePrimary,
                                            lineHeight = 17.sp
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "📅 OFFICIAL ROADMAP & COUNSELING SCHEDULE",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanGlow,
                                letterSpacing = 1.sp
                            )
                        }

                        items(categoryDetail.counselingRoadmap) { event ->
                            TimelineEventCard(event = event)
                        }
                    }
                }
            }
        }
    }

    // Exam Stream Selector Modal Bottom Sheet / Dialog
    if (showStreamPicker) {
        ExamStreamPickerDialog(
            currentStream = selectedStream,
            onSelectStream = { stream ->
                selectedStream = stream
                showStreamPicker = false
            },
            onDismiss = { showStreamPicker = false }
        )
    }
}

// --- SUPPORTING COMPOSABLES ---

@Composable
fun CategoryHeroCard(
    detail: ExamCategoryDetail,
    onOpenSwitcher: () -> Unit
) {
    val tierColor = getTierColor(detail.tier)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
        border = BorderStroke(1.dp, tierColor.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(
                            tierColor.copy(alpha = 0.15f),
                            AoeeCardBg
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = tierColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = detail.tier.uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = tierColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    color = GoldAccent.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "AOEE 2026-2027",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = detail.fullTitle,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhitePrimary,
                lineHeight = 22.sp
            )

            Text(
                text = detail.tagLine,
                fontSize = 12.sp,
                color = TextMutedSecondary,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = Color(0xFF1E293B),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = null,
                        tint = ElectricCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Conducting Body: ${detail.conductingBody}",
                        fontSize = 11.sp,
                        color = CyanGlow,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Stats Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickStatChip(label = "Duration", value = "${detail.examDurationMinutes}m", modifier = Modifier.weight(1f))
                QuickStatChip(label = "Questions", value = "${detail.totalQuestions} Qs", modifier = Modifier.weight(1f))
                QuickStatChip(label = "Max Marks", value = "${detail.totalMarks} M", modifier = Modifier.weight(1f))
                QuickStatChip(label = "Mode", value = detail.examMode.split(" ").firstOrNull() ?: "CBT", modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun QuickStatChip(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = AoeeNavyBg,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, AoeeCardBorder)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
            Text(text = label, fontSize = 9.sp, color = TextMutedSecondary)
        }
    }
}

@Composable
fun PracticeModulesGrid(
    detail: ExamCategoryDetail,
    isPremiumUnlocked: Boolean,
    onStartCbtTest: () -> Unit,
    onOpenFlashcards: () -> Unit,
    onOpenPyqBank: () -> Unit,
    onOpenPayment: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // 1. Full CBT Mock Test Button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onStartCbtTest() }
                .testTag("launch_stream_cbt_test_btn"),
            colors = CardDefaults.cardColors(containerColor = DeepIndigo),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, ElectricCyan)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(ElectricCyan.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = ElectricCyan,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Start ${detail.shortCode} CBT Mock Test",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhitePrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = ElectricCyan,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "LIVE CBT",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Text(
                        text = "Real exam simulation with timer, instant score & Punyansu AI explanations.",
                        fontSize = 11.sp,
                        color = CyanGlow
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = ElectricCyan
                )
            }
        }

        // Secondary Modules Grid (PYQ Bank & Flashcards)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // PYQ Bank
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onOpenPyqBank() }
                    .testTag("stream_pyq_bank_btn"),
                colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, AoeeCardBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, tint = SuccessEmerald, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("2018-2025 PYQs", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhitePrimary)
                    Text("Verified past exam papers", fontSize = 10.sp, color = TextMutedSecondary)
                }
            }

            // Flashcards
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onOpenFlashcards() }
                    .testTag("stream_flashcards_btn"),
                colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, AoeeCardBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Icon(imageVector = Icons.Default.Style, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Formula Cards", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhitePrimary)
                    Text("Active recall decks", fontSize = 10.sp, color = TextMutedSecondary)
                }
            }
        }
    }
}

@Composable
fun InteractiveSpeedQuizCard(
    questions: List<Question>,
    examTitle: String,
    onFeedToAi: (Question) -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var hasAnswered by remember { mutableStateOf(false) }

    val currentQuestion = questions.getOrNull(currentIndex) ?: questions.first()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
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
                    color = GoldAccent.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "QUESTION ${currentIndex + 1} OF ${questions.size}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Surface(
                    color = CyanGlow.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = currentQuestion.subject,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanGlow,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = currentQuestion.questionText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhitePrimary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Options List
            currentQuestion.options.forEachIndexed { index, optionText ->
                val isSelected = selectedOption == index
                val isCorrect = index == currentQuestion.correctOptionIndex

                val optionBg = when {
                    hasAnswered && isCorrect -> SuccessEmerald.copy(alpha = 0.2f)
                    hasAnswered && isSelected && !isCorrect -> ErrorCrimson.copy(alpha = 0.2f)
                    isSelected -> ElectricCyan.copy(alpha = 0.15f)
                    else -> AoeeNavyBg
                }

                val optionBorder = when {
                    hasAnswered && isCorrect -> SuccessEmerald
                    hasAnswered && isSelected && !isCorrect -> ErrorCrimson
                    isSelected -> ElectricCyan
                    else -> AoeeCardBorder
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(enabled = !hasAnswered) {
                            selectedOption = index
                            hasAnswered = true
                        },
                    color = optionBg,
                    border = BorderStroke(1.dp, optionBorder),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${('A' + index)}. ",
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) ElectricCyan else TextMutedSecondary,
                            fontSize = 13.sp
                        )
                        Text(
                            text = optionText,
                            color = TextWhitePrimary,
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f)
                        )
                        if (hasAnswered && isCorrect) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = SuccessEmerald, modifier = Modifier.size(18.dp))
                        } else if (hasAnswered && isSelected && !isCorrect) {
                            Icon(imageVector = Icons.Default.Cancel, contentDescription = null, tint = ErrorCrimson, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            if (hasAnswered) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = Color(0xFF1E293B),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, AoeeCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "💡 Explanation:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanGlow
                        )
                        Text(
                            text = currentQuestion.explanation,
                            fontSize = 11.sp,
                            color = TextWhitePrimary,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onFeedToAi(currentQuestion) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricCyan),
                        border = BorderStroke(1.dp, CyanGlow.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Feed to Punyansu AI", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }

                    if (currentIndex < questions.size - 1) {
                        Button(
                            onClick = {
                                currentIndex++
                                selectedOption = null
                                hasAnswered = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Next Question →", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = {
                                currentIndex = 0
                                selectedOption = null
                                hasAnswered = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Restart Drill 🔄", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubjectWeightageCard(subject: SubjectWeightage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
        border = BorderStroke(1.dp, AoeeCardBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = subject.subjectName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhitePrimary
                )
                Text(
                    text = "${subject.marks} Marks (${subject.weightagePercent}%)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ElectricCyan
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { subject.weightagePercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = ElectricCyan,
                trackColor = AoeeNavyBg
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "High Yield: " + subject.highYieldTopics.joinToString(" • "),
                fontSize = 11.sp,
                color = TextMutedSecondary
            )
        }
    }
}

@Composable
fun SyllabusModuleCard(module: SyllabusModule) {
    var expanded by remember { mutableStateOf(false) }

    val priorityColor = when (module.aiPriority) {
        "CRITICAL" -> ErrorCrimson
        "HIGH" -> GoldAccent
        else -> ElectricCyan
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
        border = BorderStroke(1.dp, AoeeCardBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        color = priorityColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "${module.aiPriority} PRIORITY • ~${module.estimatedHours}h",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = priorityColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = module.moduleName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhitePrimary
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = TextMutedSecondary
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    HorizontalDivider(color = AoeeCardBorder, modifier = Modifier.padding(bottom = 8.dp))
                    module.topics.forEach { topic ->
                        Row(
                            modifier = Modifier.padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(text = "• ", color = CyanGlow, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(text = topic, color = TextMutedSecondary, fontSize = 11.sp, lineHeight = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PatternSectionCard(section: PatternSection) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
        border = BorderStroke(1.dp, AoeeCardBorder)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = section.sectionName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhitePrimary)
                Text(
                    text = "${section.questionCount} Questions • Negative: ${section.negativeMarking}",
                    fontSize = 11.sp,
                    color = TextMutedSecondary
                )
            }
            Surface(
                color = DeepIndigo,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, AoeeCardBorder)
            ) {
                Text(
                    text = "${section.recommendedTimeMins} Mins",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElectricCyan,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun CutoffStatCard(stat: CutoffStat) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
        border = BorderStroke(1.dp, AoeeCardBorder)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = stat.category, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhitePrimary)
                Text(text = "Previous Cutoff: ${stat.previousYearCutoff}", fontSize = 11.sp, color = TextMutedSecondary)
            }
            Surface(
                color = SuccessEmerald.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, SuccessEmerald.copy(alpha = 0.5f))
            ) {
                Text(
                    text = "Safe: ${stat.safeTargetScore}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SuccessEmerald,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun EligibilityOverviewCard(detail: ExamCategoryDetail) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
        border = BorderStroke(1.dp, AoeeCardBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Event, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Age Criteria:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GoldAccent)
            }
            Text(
                text = detail.ageCriteria,
                fontSize = 12.sp,
                color = TextWhitePrimary,
                modifier = Modifier.padding(start = 26.dp, top = 2.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.School, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Educational Qualification:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
            }
            Text(
                text = detail.qualificationSummary,
                fontSize = 12.sp,
                color = TextWhitePrimary,
                modifier = Modifier.padding(start = 26.dp, top = 2.dp)
            )
        }
    }
}

@Composable
fun TimelineEventCard(event: TimelineEvent) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
        border = BorderStroke(1.dp, AoeeCardBorder)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(ElectricCyan)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = event.stage, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhitePrimary)
                Text(text = event.timeline, fontSize = 11.sp, color = CyanGlow)
            }
            Surface(
                color = DeepIndigo,
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = event.status,
                    fontSize = 9.sp,
                    color = ElectricCyan,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun ExamStreamPickerDialog(
    currentStream: ExamStream,
    onSelectStream: (ExamStream) -> Unit,
    onDismiss: () -> Unit
) {
    val categories = ExamCategoryProvider.getAllCategories()
    val grouped = categories.groupBy { it.tier }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Category, contentDescription = null, tint = ElectricCyan)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Select Exam Dashboard", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextWhitePrimary)
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                grouped.forEach { (tier, items) ->
                    item {
                        Text(
                            text = tier.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = getTierColor(tier),
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )
                    }

                    items(items) { item ->
                        val isSelected = item.stream == currentStream
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onSelectStream(item.stream) },
                            color = if (isSelected) getTierColor(tier).copy(alpha = 0.2f) else AoeeCardBg,
                            border = BorderStroke(1.dp, if (isSelected) getTierColor(tier) else AoeeCardBorder),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.shortCode,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhitePrimary
                                    )
                                    Text(
                                        text = item.odiaTitle,
                                        fontSize = 10.sp,
                                        color = CyanGlow
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = getTierColor(tier),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = ElectricCyan)
            }
        },
        containerColor = AoeeNavyBg,
        shape = RoundedCornerShape(20.dp)
    )
}

fun getTierColor(tier: String): Color {
    return when {
        tier.contains("Teacher") -> GoldAccent
        tier.contains("Technical") || tier.contains("Engineering") -> ElectricCyan
        tier.contains("School") || tier.contains("Scholarship") -> SuccessEmerald
        tier.contains("Civil") || tier.contains("Govt") -> Color(0xFFD0BCFF)
        else -> Color(0xFF38BDF8)
    }
}
