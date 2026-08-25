package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.*

/**
 * Gatekeeping Dialog that checks for 'Premium' status flag in user profile.
 * If user is not Premium, triggers this dialog prompting them to navigate to the payment screen.
 */
@Composable
fun PremiumGatekeeperDialog(
    featureName: String,
    onDismiss: () -> Unit,
    onNavigateToPayment: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .testTag("premium_gatekeeper_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
            border = BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(ElectricCyan, GoldAccent)))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Crown / Star Pro Badge
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(GoldAccent, ElectricCyan)))
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(DeepIndigo),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Stars,
                        contentDescription = "Premium Pro",
                        tint = GoldAccent,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Title
                Text(
                    text = "Unlock OAEE Pro Access ⭐",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextWhitePrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Feature Specific Gate Context
                Surface(
                    color = DeepIndigo,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "🔒 Premium Feature: $featureName",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "This feature requires an active OAEE Pro Subscription pass. Upgrade now to unlock full access across all entrance exams!",
                    fontSize = 12.sp,
                    color = TextMutedSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Perks Summary Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = AoeeNavyBg,
                    border = BorderStroke(1.dp, AoeeCardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PerkItem("All 16+ Odisha Entrance Exam PYQs (2018-2025)")
                        PerkItem("Detailed Step-by-Step AI Solutions & Formulas")
                        PerkItem("Unlimited Timed CBT Mock Tests with Rank Card")
                        PerkItem("Punyansu AI 24/7 Doubt Solver in Odia & English")
                        PerkItem("100% Ad-Free Experience (Starting at ₹29)")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Primary CTA: Navigate to Payment Screen
                Button(
                    onClick = {
                        onDismiss()
                        onNavigateToPayment()
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("unlock_full_access_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Payment,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Unlock Full Access (From ₹29)",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Dismiss / Secondary Button
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("gatekeeper_dismiss_button")
                ) {
                    Text(
                        text = "Maybe Later",
                        fontSize = 12.sp,
                        color = TextMutedSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun PerkItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = SuccessEmerald,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 11.5.sp,
            color = TextWhitePrimary.copy(alpha = 0.9f)
        )
    }
}
