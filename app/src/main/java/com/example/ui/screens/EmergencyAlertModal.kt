package com.example.ui.screens

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.PoliceRedAlert
import com.example.ui.theme.PoliceSirenBlue

@Composable
fun EmergencyPoliceAlertModal(
    alertCode: String,
    triggerPrompt: String,
    onDismiss: () -> Unit,
    onCallDelhiPolice: () -> Unit
) {
    // Pulsing siren animation
    val infiniteTransition = rememberInfiniteTransition(label = "siren")
    val sirenScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    val colorToggle by infiniteTransition.animateColor(
        initialValue = PoliceRedAlert,
        targetValue = PoliceSirenBlue,
        animationSpec = infiniteRepeatable(
            animation = tween(300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color"
    )

    Dialog(
        onDismissRequest = { /* Force acknowledge */ },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(3.dp, colorToggle, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF090D16)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Siren Icon Badge
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .scale(sirenScale)
                        .background(color = colorToggle.copy(alpha = 0.2f), shape = CircleShape)
                        .border(2.dp, colorToggle, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalPolice,
                        contentDescription = "Delhi Police Alert",
                        tint = colorToggle,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "🚨 PUNYANSU AI SAFETY PROTOCOL",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorToggle,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "EMERGENCY POLICE ALERT",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B2E)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "OFFENSE DETECTED:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PoliceRedAlert
                        )
                        Text(
                            text = "\"Olta / Reverse / Illegal Exam Question Attempt\"",
                            fontSize = 13.sp,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Query: \"$triggerPrompt\"",
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Alert Ref: $alertCode",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF06B6D4)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Punyansu AI has logged this session fingerprint and dispatched an Emergency Cyber Protocol Notification to Delhi Police Headquarters & AOEE Control Board.",
                    fontSize = 12.sp,
                    color = Color(0xFFCBD5E1),
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onCallDelhiPolice,
                    colors = ButtonDefaults.buttonColors(containerColor = PoliceRedAlert),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call Police",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Call Delhi Police (+91 11 2346 1000)",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.LightGray)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Acknowledge",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Acknowledge & Return to Fair Study Mode")
                }
            }
        }
    }
}
