package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyStudyTip
import com.example.data.model.ExamStream
import com.example.data.repository.DailyStudyTipsProvider
import com.example.ui.theme.*
import com.example.util.DailyStudyTipsManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyStudyTipsScreen(
    currentStream: ExamStream,
    onBack: () -> Unit,
    onAskAiAboutTip: (String) -> Unit,
    onNavigateToCategoryDashboard: (ExamStream) -> Unit
) {
    val context = LocalContext.current

    var isNotificationsEnabled by remember {
        mutableStateOf(DailyStudyTipsManager.isNotificationsEnabled(context))
    }
    var selectedPreset by remember {
        mutableStateOf(DailyStudyTipsManager.getDeliveryTimePreset(context))
    }
    var bookmarkedTipIds by remember {
        mutableStateOf(DailyStudyTipsManager.getBookmarkedTipIds(context))
    }
    var nextScheduleText by remember {
        mutableStateOf(DailyStudyTipsManager.getFormattedNextScheduledTime(context))
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf("ALL") }
    var showOnlyBookmarked by remember { mutableStateOf(false) }
    var showCustomTimeDialog by remember { mutableStateOf(false) }

    val todayTip = remember(currentStream) {
        DailyStudyTipsProvider.getTipOfTheDay(currentStream)
    }

    val allTips = remember { DailyStudyTipsProvider.getAllTips() }

    val categories = listOf(
        "ALL" to "All Odisha Tips (ସମସ୍ତ)",
        "CT & Pedagogy" to "CT & Pedagogy (ଶିକ୍ଷାଦାନ)",
        "OJEE CBT Tactics" to "OJEE CBT (ପ୍ରବେଶିକା)",
        "Navodaya & Math" to "Navodaya & Math (ନବୋଦୟ)",
        "OAS & State GK" to "OAS & GK (ପ୍ରଶାସନିକ)",
        "Lifelong Learning" to "Lifelong (ପ୍ରୌଢ଼ ଶିକ୍ଷା)",
        "Exam Strategy" to "Exam Strategy (ରଣନୀତି)"
    )

    val filteredTips = remember(searchQuery, selectedCategoryFilter, showOnlyBookmarked, bookmarkedTipIds) {
        var list = if (searchQuery.isNotBlank()) {
            DailyStudyTipsProvider.searchTips(searchQuery)
        } else {
            allTips
        }

        if (selectedCategoryFilter != "ALL") {
            list = list.filter { it.category.equals(selectedCategoryFilter, ignoreCase = true) }
        }

        if (showOnlyBookmarked) {
            list = list.filter { bookmarkedTipIds.contains(it.id) }
        }

        list
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "💡 Daily Study Tips",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhitePrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = CyanGlow.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "ODISHA 2026",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = CyanGlow,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "ଦୈନିକ ପରୀକ୍ଷା ପରାମର୍ଶ, ସୂତ୍ର ଓ ସମୟ ପ୍ରବନ୍ଧନ",
                            fontSize = 11.sp,
                            color = GoldAccent
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("daily_tips_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextWhitePrimary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            DailyStudyTipsManager.triggerTestNotification(context, currentStream)
                            Toast.makeText(context, "🔔 Test Study Tip notification sent to status bar!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.testTag("trigger_test_notification_top_action")
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Send Test Notification",
                            tint = CyanGlow
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepIndigo)
            )
        },
        containerColor = AoeeNavyBg
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {

            // --- 1. NOTIFICATION SERVICE CONTROL BANNER ---
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .testTag("daily_tips_notification_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DeepIndigo),
                    border = BorderStroke(1.dp, if (isNotificationsEnabled) CyanGlow.copy(alpha = 0.5f) else AoeeCardBorder)
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
                                Surface(
                                    color = if (isNotificationsEnabled) CyanGlow.copy(alpha = 0.2f) else ErrorCrimson.copy(alpha = 0.2f),
                                    shape = CircleShape,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = if (isNotificationsEnabled) Icons.Default.NotificationsActive else Icons.Outlined.NotificationsOff,
                                            contentDescription = null,
                                            tint = if (isNotificationsEnabled) CyanGlow else ErrorCrimson,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = "Daily Tip Notification Service",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhitePrimary
                                    )
                                    Text(
                                        text = if (isNotificationsEnabled) "Active • Alerts delivered daily" else "Disabled • Notifications paused",
                                        fontSize = 11.sp,
                                        color = if (isNotificationsEnabled) CyanGlow else TextMutedSecondary
                                    )
                                }
                            }

                            Switch(
                                checked = isNotificationsEnabled,
                                onCheckedChange = { enabled ->
                                    isNotificationsEnabled = enabled
                                    DailyStudyTipsManager.setNotificationsEnabled(context, enabled)
                                    nextScheduleText = DailyStudyTipsManager.getFormattedNextScheduledTime(context)
                                    val msg = if (enabled) "Daily Study Tips notifications activated!" else "Daily notifications paused"
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = CyanGlow,
                                    uncheckedThumbColor = TextMutedSecondary,
                                    uncheckedTrackColor = AoeeCardBg
                                ),
                                modifier = Modifier.testTag("daily_tips_toggle_switch")
                            )
                        }

                        if (isNotificationsEnabled) {
                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = AoeeCardBorder.copy(alpha = 0.4f))
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "SELECT DAILY DELIVERY TIME:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMutedSecondary,
                                letterSpacing = 1.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Time presets row
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(DailyStudyTipsManager.DeliveryTimePreset.values()) { preset ->
                                    val isSelected = selectedPreset == preset
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {
                                            if (preset == DailyStudyTipsManager.DeliveryTimePreset.CUSTOM) {
                                                showCustomTimeDialog = true
                                            } else {
                                                selectedPreset = preset
                                                DailyStudyTipsManager.setDeliveryTime(context, preset)
                                                nextScheduleText = DailyStudyTipsManager.getFormattedNextScheduledTime(context)
                                                Toast.makeText(context, "Delivery time set to ${preset.label}", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        label = {
                                            Text(
                                                text = "${preset.emoji} ${preset.label.substringBefore(" (")}",
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = CyanGlow,
                                            selectedLabelColor = Color.Black,
                                            containerColor = AoeeCardBg,
                                            labelColor = TextWhitePrimary
                                        ),
                                        border = FilterChipDefaults.filterChipBorder(
                                            borderColor = if (isSelected) CyanGlow else AoeeCardBorder,
                                            enabled = true,
                                            selected = isSelected
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Next schedule banner & test trigger button
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(AoeeNavyBg.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = GoldAccent,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Next: $nextScheduleText",
                                        fontSize = 11.sp,
                                        color = TextWhitePrimary,
                                        maxLines = 1
                                    )
                                }

                                Button(
                                    onClick = {
                                        DailyStudyTipsManager.triggerTestNotification(context, currentStream)
                                        Toast.makeText(context, "🔔 Notification triggered! Check Android status bar.", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = CyanGlow),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier
                                        .height(30.dp)
                                        .testTag("test_daily_notification_button")
                                ) {
                                    Text(
                                        text = "⚡ Test Alert",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // --- 2. TODAY'S FEATURED TIP OF THE DAY HERO CARD ---
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TODAY'S FEATURED STUDY TIP",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanGlow,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = "ଆଜିର ମୁଖ୍ୟ ପରାମର୍ଶ",
                            fontSize = 11.sp,
                            color = GoldAccent
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    StudyTipCard(
                        tip = todayTip,
                        isTodayFeatured = true,
                        isBookmarked = bookmarkedTipIds.contains(todayTip.id),
                        onToggleBookmark = {
                            val bookmarked = DailyStudyTipsManager.toggleBookmark(context, todayTip.id)
                            bookmarkedTipIds = DailyStudyTipsManager.getBookmarkedTipIds(context)
                            Toast.makeText(context, if (bookmarked) "Tip bookmarked ⭐" else "Bookmark removed", Toast.LENGTH_SHORT).show()
                        },
                        onSendNotification = {
                            DailyStudyTipsManager.showDailyTipNotification(context, todayTip)
                            Toast.makeText(context, "Sent '${todayTip.title}' to notification drawer!", Toast.LENGTH_SHORT).show()
                        },
                        onAskAi = {
                            onAskAiAboutTip("Can you explain this Odisha entrance study tip in more detail with examples and an action plan? Title: ${todayTip.title}. Advice: ${todayTip.englishAdvice}. Rule: ${todayTip.actionableRule}")
                        },
                        onCopyTip = {
                            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("AOEE Daily Tip", "${todayTip.title}\n\n${todayTip.englishAdvice}\n\n${todayTip.odiaAdvice}\n\nRule: ${todayTip.actionableRule}")
                            cm.setPrimaryClip(clip)
                            Toast.makeText(context, "Tip copied to clipboard!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            // --- 3. FILTER & SEARCH BAR ---
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "EXPLORE ALL STUDY TIPS (${filteredTips.size})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhitePrimary,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Search input
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("study_tips_search_input"),
                        placeholder = { Text("Search advice, Sandhi, OJEE pacing, formulas...", fontSize = 13.sp, color = TextMutedSecondary) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = CyanGlow) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextMutedSecondary)
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanGlow,
                            unfocusedBorderColor = AoeeCardBorder,
                            focusedContainerColor = AoeeCardBg,
                            unfocusedContainerColor = AoeeCardBg,
                            focusedTextColor = TextWhitePrimary,
                            unfocusedTextColor = TextWhitePrimary
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Category filter chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { (catKey, catLabel) ->
                            val isSelected = selectedCategoryFilter == catKey
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCategoryFilter = catKey },
                                label = {
                                    Text(
                                        text = catLabel,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = DeepIndigo,
                                    selectedLabelColor = CyanGlow,
                                    containerColor = AoeeCardBg,
                                    labelColor = TextMutedSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = if (isSelected) CyanGlow else AoeeCardBorder,
                                    enabled = true,
                                    selected = isSelected
                                )
                            )
                        }

                        item {
                            FilterChip(
                                selected = showOnlyBookmarked,
                                onClick = { showOnlyBookmarked = !showOnlyBookmarked },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (showOnlyBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                        contentDescription = null,
                                        tint = if (showOnlyBookmarked) GoldAccent else TextMutedSecondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = "Saved (${bookmarkedTipIds.size})",
                                        fontSize = 11.sp,
                                        fontWeight = if (showOnlyBookmarked) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = GoldAccent.copy(alpha = 0.2f),
                                    selectedLabelColor = GoldAccent,
                                    containerColor = AoeeCardBg,
                                    labelColor = TextMutedSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = if (showOnlyBookmarked) GoldAccent else AoeeCardBorder,
                                    enabled = true,
                                    selected = showOnlyBookmarked
                                )
                            )
                        }
                    }
                }
            }

            // --- 4. LIST OF ALL STUDY TIPS ---
            if (filteredTips.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                tint = TextMutedSecondary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No study tips found matching your search",
                                fontSize = 14.sp,
                                color = TextMutedSecondary
                            )
                            Text(
                                text = "କୌଣସି ପରାମର୍ଶ ମିଳିଲା ନାହିଁ",
                                fontSize = 12.sp,
                                color = GoldAccent
                            )
                        }
                    }
                }
            } else {
                items(filteredTips, key = { it.id }) { tip ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        StudyTipCard(
                            tip = tip,
                            isTodayFeatured = false,
                            isBookmarked = bookmarkedTipIds.contains(tip.id),
                            onToggleBookmark = {
                                val bookmarked = DailyStudyTipsManager.toggleBookmark(context, tip.id)
                                bookmarkedTipIds = DailyStudyTipsManager.getBookmarkedTipIds(context)
                                Toast.makeText(context, if (bookmarked) "Tip saved to Bookmarks ⭐" else "Bookmark removed", Toast.LENGTH_SHORT).show()
                            },
                            onSendNotification = {
                                DailyStudyTipsManager.showDailyTipNotification(context, tip)
                                Toast.makeText(context, "Notification sent for '${tip.title}'!", Toast.LENGTH_SHORT).show()
                            },
                            onAskAi = {
                                onAskAiAboutTip("How should I implement this study advice for my Odisha exam prep? Title: ${tip.title}. Advice: ${tip.englishAdvice}. Rule: ${tip.actionableRule}")
                            },
                            onCopyTip = {
                                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("AOEE Tip", "${tip.title}\n\n${tip.englishAdvice}\n\n${tip.odiaAdvice}\n\nRule: ${tip.actionableRule}")
                                cm.setPrimaryClip(clip)
                                Toast.makeText(context, "Tip copied!", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }
    }

    // --- CUSTOM TIME PICKER DIALOG ---
    if (showCustomTimeDialog) {
        var hourInput by remember { mutableStateOf("6") }
        var minuteInput by remember { mutableStateOf("30") }
        var isAm by remember { mutableStateOf(true) }

        AlertDialog(
            onDismissRequest = { showCustomTimeDialog = false },
            title = {
                Text(
                    text = "⏰ Set Custom Notification Time",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhitePrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = "Choose your preferred time to receive daily Odisha exam advice & high-yield revision tips:",
                        fontSize = 12.sp,
                        color = TextMutedSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = hourInput,
                            onValueChange = { if (it.length <= 2 && it.all { c -> c.isDigit() }) hourInput = it },
                            modifier = Modifier.width(64.dp),
                            label = { Text("Hour") },
                            singleLine = true
                        )

                        Text(" : ", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextWhitePrimary)

                        OutlinedTextField(
                            value = minuteInput,
                            onValueChange = { if (it.length <= 2 && it.all { c -> c.isDigit() }) minuteInput = it },
                            modifier = Modifier.width(64.dp),
                            label = { Text("Min") },
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Row {
                            FilterChip(
                                selected = isAm,
                                onClick = { isAm = true },
                                label = { Text("AM") }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            FilterChip(
                                selected = !isAm,
                                onClick = { isAm = false },
                                label = { Text("PM") }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val h = hourInput.toIntOrNull() ?: 7
                        val m = minuteInput.toIntOrNull() ?: 0
                        val hr24 = if (isAm) {
                            if (h == 12) 0 else h
                        } else {
                            if (h == 12) 12 else h + 12
                        }

                        selectedPreset = DailyStudyTipsManager.DeliveryTimePreset.CUSTOM
                        DailyStudyTipsManager.setDeliveryTime(context, DailyStudyTipsManager.DeliveryTimePreset.CUSTOM, hr24, m)
                        nextScheduleText = DailyStudyTipsManager.getFormattedNextScheduledTime(context)
                        showCustomTimeDialog = false
                        Toast.makeText(context, "Custom notification time saved!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanGlow)
                ) {
                    Text("Save Time", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomTimeDialog = false }) {
                    Text("Cancel", color = TextMutedSecondary)
                }
            },
            containerColor = DeepIndigo
        )
    }
}

@Composable
fun StudyTipCard(
    tip: DailyStudyTip,
    isTodayFeatured: Boolean,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onSendNotification: () -> Unit,
    onAskAi: () -> Unit,
    onCopyTip: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("study_tip_card_${tip.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isTodayFeatured) DeepIndigo else AoeeCardBg
        ),
        border = BorderStroke(
            1.dp,
            if (isTodayFeatured) CyanGlow else AoeeCardBorder
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Category Badge + Exam Target + Bookmark & Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = if (isTodayFeatured) CyanGlow else GoldAccent.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = if (isTodayFeatured) "⭐ TODAY'S TIP" else tip.category.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isTodayFeatured) Color.Black else GoldAccent,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        color = DeepIndigo,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(0.5.dp, AoeeCardBorder)
                    ) {
                        Text(
                            text = tip.examTarget,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            maxLines = 1
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onSendNotification,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Send notification",
                            tint = CyanGlow,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) GoldAccent else TextMutedSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onCopyTip,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = TextMutedSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = tip.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhitePrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // English Advice
            Text(
                text = tip.englishAdvice,
                fontSize = 13.sp,
                color = TextMutedSecondary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Odia Advice Box
            Surface(
                color = AoeeNavyBg.copy(alpha = 0.7f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(0.5.dp, GoldAccent.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "📖 ",
                        fontSize = 12.sp
                    )
                    Text(
                        text = tip.odiaAdvice,
                        fontSize = 12.sp,
                        color = GoldAccent,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Golden Actionable Rule Box
            Surface(
                color = GoldAccent.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Action Rule: ${tip.actionableRule}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhitePrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Action: Ask Punyansu AI Mentor
            Button(
                onClick = onAskAi,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ask_ai_about_tip_${tip.id}"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isTodayFeatured) CyanGlow else DeepIndigo
                ),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, if (isTodayFeatured) CyanGlow else AoeeCardBorder)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = null,
                        tint = if (isTodayFeatured) Color.Black else CyanGlow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Ask Punyansu AI Mentor to Explain This Tip",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isTodayFeatured) Color.Black else TextWhitePrimary
                    )
                }
            }
        }
    }
}
