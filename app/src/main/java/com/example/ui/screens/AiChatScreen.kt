package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ChatMessageEntity
import com.example.data.repository.PunyansuAiRepository
import com.example.data.repository.PunyansuChatResult
import com.example.ui.theme.*
import kotlinx.coroutines.launch

enum class ChatCategory(val title: String, val icon: String) {
    FEATURED("🌟 Featured", "🌟"),
    ODISHA_GK("🏛️ OAS & Odisha GK", "🏛️"),
    SCHOOL_NAVODAYA("🏫 Navodaya & OAV", "🏫"),
    TEACHER_PEDAGOGY("👩‍🏫 CT & B.Ed", "👩‍🏫"),
    OJEE_SCIENCE("🧪 OJEE Science", "🧪"),
    MATH_LOGIC("📐 Math & Logic", "📐"),
    GENERAL_SCIENCE("🚀 Science & Current Affairs", "🚀")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiChatScreen(
    repository: PunyansuAiRepository,
    onBack: () -> Unit,
    onTriggerSafetyAlert: (promptText: String, alertCode: String) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessageEntity(
                    sender = "PUNYANSU_AI",
                    text = "Namaskar! I am **Punyansu AI**, your authoritative AI mentor for All Odisha Entrance Examinations (AOEE).\n\n" +
                            "I can help you master concepts, solve step-by-step numericals, and explain topics across **OJEE, Navodaya, PSMSE, CT/B.Ed, OAS/IAS, Odisha GK, General Science, and Mathematics**.\n\n" +
                            "Choose a category below or ask any question in English or Odia!",
                    timestamp = System.currentTimeMillis()
                )
            )
        )
    }

    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(ChatCategory.FEATURED) }
    val listState = rememberLazyListState()

    val categoryPrompts = mapOf(
        ChatCategory.FEATURED to listOf(
            "Explain Kirchhoff's Voltage & Current Law",
            "Odisha History & Kalinga War facts for OAS",
            "Top 5 Math shortcuts for OJEE & DET",
            "Jean Piaget's 4 stages of Cognitive Development",
            "Navodaya JNVST Mental Ability tips & shortcuts",
            "Punyansu AI ku olta question (Test Police Alert)"
        ),
        ChatCategory.ODISHA_GK to listOf(
            "Explain Kalinga War (261 BC) and Ashoka's Major Rock Edicts",
            "Significance of Paika Rebellion 1817 and Buxi Jagabandhu",
            "Formation of Odisha state on 1 April 1936 (Utkal Divas)",
            "Chilika Lake & Similipal National Park biodiversity for AOEE",
            "Konark Sun Temple & Puri Jagannath Temple architecture"
        ),
        ChatCategory.SCHOOL_NAVODAYA to listOf(
            "Navodaya JNVST Mental Ability: Odd man out & figure patterns",
            "Pathani Samanta Mathematics Scholarship (PSMSE) number theory",
            "Odisha Adarsha Vidyalaya (OAV) entrance English & Math strategy",
            "Arithmetic shortcuts for fractions, LCM, HCF & percentages"
        ),
        ChatCategory.TEACHER_PEDAGOGY to listOf(
            "Jean Piaget vs Lev Vygotsky: ZPD & Scaffolding differences",
            "Lawrence Kohlberg's stages of Moral Development in children",
            "Right to Education (RTE) Act 2009 key provisions for CT/B.Ed",
            "NEP 2020 5+3+3+4 pedagogical structure explained",
            "Bloom's Taxonomy: Cognitive, Affective & Psychomotor domains"
        ),
        ChatCategory.OJEE_SCIENCE to listOf(
            "Explain Aldol Condensation vs Cannizzaro Reaction mechanisms",
            "Grignard Reagent preparations & alcohol synthesis",
            "Young's Double Slit Experiment (YDSE) fringe width formula",
            "Carnot Engine efficiency & Thermodynamics Laws for OJEE",
            "Hybridization shortcut formula for chemical bonding (sp, sp2, sp3)",
        ),
        ChatCategory.MATH_LOGIC to listOf(
            "Integration by Parts ILATE rule & calculus shortcuts",
            "Matrices & Determinant properties (|kA| = k^n |A|)",
            "Quadratic equation roots formula & discriminant rules",
            "Trigonometry identities & angle conversion formulas"
        ),
        ChatCategory.GENERAL_SCIENCE to listOf(
            "ISRO Chandrayaan-3, Aditya-L1 & Gaganyaan mission highlights",
            "Indian Constitution Fundamental Rights (Part III, Articles 12-35)",
            "Photosynthesis Light vs Dark (Calvin cycle) reactions",
            "DNA replication & Watson-Crick double helix structure"
        )
    )

    fun copyToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Punyansu AI Answer", text)
        clipboard.setPrimaryClip(clip)
        coroutineScope.launch {
            snackbarHostState.showSnackbar("Copied to clipboard 📋")
        }
    }

    fun sendMessage(textToSend: String) {
        if (textToSend.isBlank() || isLoading) return

        val userMsg = ChatMessageEntity(
            sender = "USER",
            text = textToSend,
            timestamp = System.currentTimeMillis()
        )
        val updatedHistory = messages + userMsg
        messages = updatedHistory
        inputText = ""
        isLoading = true

        coroutineScope.launch {
            listState.animateScrollToItem(messages.size - 1)

            when (val result = repository.generateAiChatResponse(textToSend, updatedHistory)) {
                is PunyansuChatResult.Success -> {
                    val aiMsg = ChatMessageEntity(
                        sender = "PUNYANSU_AI",
                        text = result.responseText,
                        timestamp = System.currentTimeMillis()
                    )
                    messages = messages + aiMsg
                    isLoading = false
                    listState.animateScrollToItem(messages.size - 1)
                }
                is PunyansuChatResult.SafetyAlertTriggered -> {
                    val aiAlertMsg = ChatMessageEntity(
                        sender = "PUNYANSU_AI",
                        text = result.message,
                        timestamp = System.currentTimeMillis(),
                        isSafetyAlert = true
                    )
                    messages = messages + aiAlertMsg
                    isLoading = false
                    listState.animateScrollToItem(messages.size - 1)

                    onTriggerSafetyAlert(result.promptText, result.alertCode)
                }
                is PunyansuChatResult.Error -> {
                    val errorMsg = ChatMessageEntity(
                        sender = "PUNYANSU_AI",
                        text = "Error: ${result.errorMessage}",
                        timestamp = System.currentTimeMillis()
                    )
                    messages = messages + errorMsg
                    isLoading = false
                    listState.animateScrollToItem(messages.size - 1)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(ElectricCyan, DeepIndigo))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Punyansu AI Model",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhitePrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(GoldAccent.copy(alpha = 0.2f))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "AOEE PRO",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldAccent
                                    )
                                }
                            }
                            Text(
                                text = "All Odisha Entrance & GK Tutor • Online",
                                fontSize = 11.sp,
                                color = SuccessEmerald
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextWhitePrimary)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        messages = listOf(
                            ChatMessageEntity(
                                sender = "PUNYANSU_AI",
                                text = "Chat refreshed! How can Punyansu AI assist your entrance preparation today?",
                                timestamp = System.currentTimeMillis()
                            )
                        )
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Conversation cleared")
                        }
                    }) {
                        Icon(imageVector = Icons.Outlined.DeleteOutline, contentDescription = "Clear Chat", tint = TextMutedSecondary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AoeeNavyBg)
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AoeeCardBg)
                    .navigationBarsPadding()
                    .padding(12.dp)
            ) {
                // Category Pills Tab
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(ChatCategory.values()) { cat ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat.title, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElectricCyan,
                                selectedLabelColor = Color.White,
                                containerColor = DeepIndigo.copy(alpha = 0.3f),
                                labelColor = TextMutedSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) ElectricCyan else AoeeCardBorder,
                                selectedBorderColor = ElectricCyan
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Suggestion Prompts for Selected Category
                val currentPrompts = categoryPrompts[selectedCategory] ?: emptyList()
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(currentPrompts) { prompt ->
                        AssistChip(
                            onClick = { sendMessage(prompt) },
                            label = { Text(prompt, fontSize = 11.sp, color = TextWhitePrimary) },
                            colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFF1E293B)),
                            border = BorderStroke(1.dp, CyanGlow.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Input Field
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Ask Punyansu AI any entrance doubt / GK...", fontSize = 13.sp, color = TextMutedSecondary) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field"),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF0F172A),
                            unfocusedContainerColor = Color(0xFF0F172A),
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = AoeeCardBorder,
                            focusedTextColor = TextWhitePrimary,
                            unfocusedTextColor = TextWhitePrimary
                        ),
                        maxLines = 4
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    FloatingActionButton(
                        onClick = { sendMessage(inputText) },
                        containerColor = ElectricCyan,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("send_message_button")
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
                        }
                    }
                }
            }
        },
        containerColor = AoeeNavyBg
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages) { message ->
                val isUser = message.sender == "USER"
                val isAlert = message.isSafetyAlert

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    if (!isUser) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (isAlert) PoliceRedAlert else ElectricCyan),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isAlert) Icons.Default.LocalPolice else Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Surface(
                        color = when {
                            isUser -> DeepIndigo
                            isAlert -> PoliceAlertBg
                            else -> AoeeCardBg
                        },
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 16.dp
                        ),
                        border = BorderStroke(
                            1.dp,
                            when {
                                isAlert -> PoliceAlertBorder
                                isUser -> Color(0xFF6366F1).copy(alpha = 0.5f)
                                else -> AoeeCardBorder
                            }
                        ),
                        modifier = Modifier.widthIn(max = 320.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            if (!isUser) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = if (isAlert) "🚨 Punyansu AI Emergency Alert" else "Punyansu AI Mentor",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isAlert) PoliceRedAlert else CyanGlow
                                        )
                                        if (!isAlert) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "• AI Model",
                                                fontSize = 9.sp,
                                                color = TextMutedSecondary
                                            )
                                        }
                                    }

                                    if (!isAlert) {
                                        IconButton(
                                            onClick = { copyToClipboard(message.text) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.ContentCopy,
                                                contentDescription = "Copy text",
                                                tint = TextMutedSecondary,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                            }

                            Text(
                                text = message.text,
                                fontSize = 13.5.sp,
                                color = if (isAlert) PoliceRedAlert else TextWhitePrimary,
                                lineHeight = 20.sp
                            )

                            // Interactive Quick Follow-up Buttons for AI responses (except alert)
                            if (!isUser && !isAlert && message.text.length > 50) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Divider(color = AoeeCardBorder.copy(alpha = 0.5f), thickness = 0.5.dp)
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    SuggestionButton(
                                        label = "📝 Practice MCQ",
                                        onClick = { sendMessage("Give me an AOEE entrance practice MCQ question based on this topic with explanation!") }
                                    )
                                    SuggestionButton(
                                        label = "🔍 Step-by-Step",
                                        onClick = { sendMessage("Can you break down the step-by-step formula and derivation for this?") }
                                    )
                                    SuggestionButton(
                                        label = "💡 Pro-Tip",
                                        onClick = { sendMessage("What is the top shortcut or memory trick for this in AOEE exams?") }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(ElectricCyan.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = ElectricCyan,
                                strokeWidth = 2.dp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = AoeeCardBg,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, AoeeCardBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Punyansu AI is synthesizing answer...",
                                    fontSize = 12.sp,
                                    color = CyanGlow
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
private fun SuggestionButton(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1E293B))
            .border(1.dp, CyanGlow.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = CyanGlow,
            fontWeight = FontWeight.Medium
        )
    }
}
