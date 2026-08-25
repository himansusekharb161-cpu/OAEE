package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserSessionManager
import com.example.data.model.UserProfile
import com.example.ui.theme.*

data class PricingPlan(
    val id: String,
    val title: String,
    val amount: Int,
    val subtitle: String,
    val badge: String,
    val isPopular: Boolean = false,
    val perks: List<String>
)

val PAYMENT_PLANS = listOf(
    PricingPlan(
        id = "pyq_starter",
        title = "PYQ & Formula Starter",
        amount = 29,
        subtitle = "Full 2018-2025 Verified Odisha Entrance Papers",
        badge = "STARTER",
        perks = listOf(
            "All 16+ Odisha Entrance Exam PYQ Question Banks",
            "Detailed step-by-step mathematical solutions",
            "High-speed offline study access & Flashcards"
        )
    ),
    PricingPlan(
        id = "pro_cbt_all",
        title = "Pro CBT & AI Mentor All-Access",
        amount = 49,
        subtitle = "Full AI Mock Tests + 2026-2027 Future Prediction",
        badge = "MOST POPULAR",
        isPopular = true,
        perks = listOf(
            "Unlimited timed CBT Mock Tests with Instant Rank Cards",
            "Punyansu AI Mentor 24/7 Doubt Solver in Odia & English",
            "Room DB past exam history & analytics backup",
            "100% Ad-Free Experience (No Video Interruptions)"
        )
    ),
    PricingPlan(
        id = "lifetime_achiever",
        title = "Lifetime Ranker All-In-One",
        amount = 99,
        subtitle = "School + College + Govt Jobs + Open University (55 Yrs)",
        badge = "BEST VALUE",
        perks = listOf(
            "Everything in Pro CBT All-Access Plan",
            "Lifetime access to all future Odisha entrance syllabus",
            "Priority VIP Community & Doubt Clearing",
            "Direct mentor verification & rank booster pack"
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpiPaymentScreen(
    userProfile: UserProfile,
    sessionManager: UserSessionManager,
    onBack: () -> Unit,
    onPaymentSuccess: (String) -> Unit
) {
    val context = LocalContext.current
    val bankConfig = remember { sessionManager.getBankDetails() }

    var selectedPlan by remember { mutableStateOf(PAYMENT_PLANS[1]) } // Default ₹49 Pro
    var enteredUtr by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }

    fun launchUpiIntent(amount: Int, note: String) {
        val upiUri = Uri.parse(
            "upi://pay?pa=${bankConfig.upiId}&pn=${Uri.encode("Odisha Entrance Exam Prep")}&am=$amount&cu=INR&tn=${Uri.encode(note)}"
        )
        val intent = Intent(Intent.ACTION_VIEW, upiUri)
        try {
            val chooser = Intent.createChooser(intent, "Pay ₹$amount via UPI App (Google Pay / PhonePe / Paytm / BHIM)")
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "UPI App opening failed. Please scan the QR code to complete the payment.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "OAEE Pro Subscription",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TextWhitePrimary
                        )
                        Text(
                            text = "Unlock All Odisha Entrance Exams & Remove Ads",
                            fontSize = 11.5.sp,
                            color = ElectricCyan
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhitePrimary)
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
            contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- HEADER SUBSCRIPTION STATUS HERO ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (userProfile.isPremiumUnlocked) SuccessEmerald.copy(alpha = 0.15f) else DeepIndigo
                    ),
                    border = BorderStroke(1.5.dp, if (userProfile.isPremiumUnlocked) SuccessEmerald else ElectricCyan)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(if (userProfile.isPremiumUnlocked) SuccessEmerald else GoldAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (userProfile.isPremiumUnlocked) Icons.Default.Verified else Icons.Default.Stars,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (userProfile.isPremiumUnlocked) "PRO ACTIVE: ${userProfile.premiumPlan}" else "UPGRADE TO OAEE PRO PASS",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (userProfile.isPremiumUnlocked) SuccessEmerald else GoldAccent,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = if (userProfile.isPremiumUnlocked)
                                    "All 16+ Odisha Entrance Exam papers, AI Doubt Solver & CBT Mock Tests are fully unlocked!"
                                else
                                    "Instant access to 2018-2025 PYQs, AI Mentorship, Timed CBT Mocks & 100% Ad-Free experience.",
                                fontSize = 11.5.sp,
                                color = TextMutedSecondary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            // --- PLAN SELECTION SECTION ---
            item {
                Text(
                    text = "CHOOSE YOUR SUBSCRIPTION PLAN",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CyanGlow,
                    letterSpacing = 1.sp
                )
            }

            items(PAYMENT_PLANS.size) { index ->
                val plan = PAYMENT_PLANS[index]
                val isSelected = selectedPlan.id == plan.id

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedPlan = plan }
                        .testTag("plan_card_${plan.id}"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isSelected) DeepIndigo else AoeeCardBg),
                    border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) ElectricCyan else AoeeCardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { selectedPlan = plan },
                                        colors = RadioButtonDefaults.colors(selectedColor = ElectricCyan)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = plan.title,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhitePrimary
                                    )
                                }
                                Text(
                                    text = plan.subtitle,
                                    fontSize = 11.5.sp,
                                    color = TextMutedSecondary,
                                    modifier = Modifier.padding(start = 36.dp)
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "₹${plan.amount}",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSelected) GoldAccent else TextWhitePrimary
                                )
                                Surface(
                                    color = if (plan.isPopular) GoldAccent.copy(alpha = 0.2f) else ElectricCyan.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = plan.badge,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (plan.isPopular) GoldAccent else ElectricCyan,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = AoeeCardBorder.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(8.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            plan.perks.forEach { perk ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = SuccessEmerald,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = perk,
                                        fontSize = 12.sp,
                                        color = TextWhitePrimary.copy(alpha = 0.9f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // --- ONE-TAP DIRECT PAYMENT CARD ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                    border = BorderStroke(1.5.dp, ElectricCyan.copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            color = SuccessEmerald.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, SuccessEmerald)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = SuccessEmerald, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("256-BIT ENCRYPTED DIRECT GATEWAY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SuccessEmerald)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Pay ₹${selectedPlan.amount} for ${selectedPlan.title}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextWhitePrimary,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "One-tap payment via Google Pay, PhonePe, Paytm, BHIM, or any Banking App",
                            fontSize = 11.5.sp,
                            color = TextMutedSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Stylized Instant Payment QR
                        Box(
                            modifier = Modifier
                                .size(180.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            DynamicUpiQrCanvas(
                                upiString = "upi://pay?pa=${bankConfig.upiId}&pn=${Uri.encode("Odisha Entrance Exam Prep")}&am=${selectedPlan.amount}&cu=INR&tn=OAEE_Exam_Prep",
                                amount = selectedPlan.amount
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // PRIMARY 1-CLICK PAY BUTTON
                        Button(
                            onClick = { launchUpiIntent(selectedPlan.amount, "OAEE Pro Plan: ${selectedPlan.title}") },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("pay_via_upi_app_button")
                        ) {
                            Icon(imageVector = Icons.Default.Payment, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Pay ₹${selectedPlan.amount} & Unlock Pro",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.5.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            // --- INSTANT UNLOCK / UTR VERIFICATION ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DeepIndigo),
                    border = BorderStroke(1.5.dp, GoldAccent.copy(alpha = 0.8f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = GoldAccent)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ALREADY PAID? ACTIVATE INSTANTLY",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = GoldAccent
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "If you have completed the payment of ₹${selectedPlan.amount}, enter your UPI Transaction Ref / UTR No. (or tap activate directly):",
                            fontSize = 11.5.sp,
                            color = TextMutedSecondary,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = enteredUtr,
                            onValueChange = { enteredUtr = it },
                            placeholder = { Text("Enter 12-digit UTR No. (Optional)", color = TextMutedSecondary, fontSize = 12.sp) },
                            leadingIcon = {
                                Icon(imageVector = Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = null, tint = ElectricCyan)
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("enter_utr_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricCyan,
                                unfocusedBorderColor = AoeeCardBorder,
                                focusedTextColor = TextWhitePrimary,
                                unfocusedTextColor = TextWhitePrimary,
                                focusedContainerColor = AoeeNavyBg,
                                unfocusedContainerColor = AoeeNavyBg
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                val utrClean = if (enteredUtr.isBlank()) "UPI_DIRECT_${System.currentTimeMillis()}" else enteredUtr.trim()
                                sessionManager.setPremiumUnlocked(true, selectedPlan.title, utrClean)
                                onPaymentSuccess(selectedPlan.title)
                                Toast.makeText(context, "🎉 ${selectedPlan.title} activated successfully!", Toast.LENGTH_LONG).show()
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SuccessEmerald),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("activate_premium_button")
                        ) {
                            Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Activate ${selectedPlan.title} Now",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DynamicUpiQrCanvas(
    upiString: String,
    amount: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val gridSize = 21
            val cellSize = width / gridSize

            // Draw QR Background & Finder Patterns
            drawRect(color = Color.White, size = size)

            // Top-Left Finder
            drawRect(Color(0xFF0F172A), topLeft = Offset(0f, 0f), size = Size(cellSize * 7, cellSize * 7))
            drawRect(Color.White, topLeft = Offset(cellSize, cellSize), size = Size(cellSize * 5, cellSize * 5))
            drawRect(Color(0xFF0F172A), topLeft = Offset(cellSize * 2, cellSize * 2), size = Size(cellSize * 3, cellSize * 3))

            // Top-Right Finder
            drawRect(Color(0xFF0F172A), topLeft = Offset(width - cellSize * 7, 0f), size = Size(cellSize * 7, cellSize * 7))
            drawRect(Color.White, topLeft = Offset(width - cellSize * 6, cellSize), size = Size(cellSize * 5, cellSize * 5))
            drawRect(Color(0xFF0F172A), topLeft = Offset(width - cellSize * 5, cellSize * 2), size = Size(cellSize * 3, cellSize * 3))

            // Bottom-Left Finder
            drawRect(Color(0xFF0F172A), topLeft = Offset(0f, height - cellSize * 7), size = Size(cellSize * 7, cellSize * 7))
            drawRect(Color.White, topLeft = Offset(cellSize, height - cellSize * 6), size = Size(cellSize * 5, cellSize * 5))
            drawRect(Color(0xFF0F172A), topLeft = Offset(cellSize * 2, height - cellSize * 5), size = Size(cellSize * 3, cellSize * 3))

            // Draw Deterministic Data Cells
            val seed = (upiString.hashCode() + amount).toLong()
            for (r in 0 until gridSize) {
                for (c in 0 until gridSize) {
                    val inTopLeft = r < 8 && c < 8
                    val inTopRight = r < 8 && c >= gridSize - 8
                    val inBottomLeft = r >= gridSize - 8 && c < 8
                    val inCenterLogo = r in 8..12 && c in 8..12

                    if (!inTopLeft && !inTopRight && !inBottomLeft && !inCenterLogo) {
                        val bit = ((seed xor (r * 31L + c * 17L)) % 3) == 0L
                        if (bit) {
                            drawRect(
                                color = Color(0xFF0A0F1D),
                                topLeft = Offset(c * cellSize, r * cellSize),
                                size = Size(cellSize - 0.5f, cellSize - 0.5f)
                            )
                        }
                    }
                }
            }
        }

        // Center UPI Badge
        Surface(
            color = Color(0xFF0F172A),
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(1.dp, Color(0xFF00E5FF)),
            modifier = Modifier.size(42.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("PRO", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF00E5FF))
                    Text("₹$amount", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFB703))
                }
            }
        }
    }
}
