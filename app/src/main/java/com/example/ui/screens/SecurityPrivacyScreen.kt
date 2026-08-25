package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SafetyLogEntity
import com.example.data.model.UserProfile
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityPrivacyScreen(
    userProfile: UserProfile,
    safetyLogs: List<SafetyLogEntity>,
    onToggleAppLock: (enabled: Boolean) -> Unit,
    onClearLogs: () -> Unit,
    onBack: () -> Unit
) {
    var isLockEnabled by remember { mutableStateOf(userProfile.isAppLockEnabled) }
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy 🔏 & Security Dashboard", fontWeight = FontWeight.Bold, color = TextWhitePrimary) },
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
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DeepIndigo),
                    border = BorderStroke(1.dp, Color(0xFFD0BCFF))
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(ElectricCyan.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = ElectricCyan,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = "AOEE Privacy 🔏 & Data Security",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhitePrimary
                            )
                            Text(
                                text = "AES-256 Encrypted Local Room DB • Google Gmail Verified",
                                fontSize = 12.sp,
                                color = SuccessEmerald
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "APP SECURITY CONTROLS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ElectricCyan,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                    border = BorderStroke(1.dp, AoeeCardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = ElectricCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Biometric / PIN App Lock",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhitePrimary
                                    )
                                    Text(
                                        text = "Require PIN to open AOEE app",
                                        fontSize = 11.sp,
                                        color = TextMutedSecondary
                                    )
                                }
                            }

                            Switch(
                                checked = isLockEnabled,
                                onCheckedChange = {
                                    isLockEnabled = it
                                    onToggleAppLock(it)
                                },
                                colors = SwitchDefaults.colors(checkedThumbColor = ElectricCyan)
                            )
                        }

                        Divider(modifier = Modifier.padding(vertical = 12.dp), color = AoeeCardBorder)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Policy,
                                    contentDescription = null,
                                    tint = ElectricCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "AOEE Privacy & Security Policy",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhitePrimary
                                    )
                                    Text(
                                        text = "Read examination compliance policy",
                                        fontSize = 11.sp,
                                        color = TextMutedSecondary
                                    )
                                }
                            }

                            OutlinedButton(
                                onClick = { showPrivacyPolicyDialog = true },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Read", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EMERGENCY POLICE SAFETY LOGS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PoliceRedAlert,
                        letterSpacing = 1.sp
                    )

                    if (safetyLogs.isNotEmpty()) {
                        TextButton(onClick = onClearLogs) {
                            Text("Clear Logs", fontSize = 11.sp, color = TextMutedSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (safetyLogs.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                        border = BorderStroke(1.dp, AoeeCardBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = SuccessEmerald,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "No safety violations or 'Olta' question attempts recorded. System is 100% secure.",
                                fontSize = 12.sp,
                                color = TextWhitePrimary
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        safetyLogs.forEach { log ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = PoliceAlertBg),
                                border = BorderStroke(1.dp, PoliceAlertBorder)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "🚨 ALERT: " + log.alertCode,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = PoliceRedAlert
                                        )
                                        Text(
                                            text = "DELHI POLICE LOG",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = PoliceRedAlert
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Query: \"${log.triggerPrompt}\"",
                                        fontSize = 12.sp,
                                        color = TextWhitePrimary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Status: ${log.status}",
                                        fontSize = 10.sp,
                                        color = TextMutedSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showPrivacyPolicyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyPolicyDialog = false },
            title = {
                Text(text = "AOEE Privacy 🔏 & Safety Terms", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "1. Student Data Confidentiality: Gmail accounts used during authentication are protected via Google Cloud Identity and local AES-256 encrypted Room database storage.\n\n" +
                            "2. Punyansu AI Safety Protocol: Any malicious, illegal paper-leak, or reverse ('Olta') question attempts aimed at corrupting exam integrity automatically trigger an Emergency Security Signal to India / Delhi Police Cyber Cell (+91 11 2346 1000) and AOEE Exam Board.\n\n" +
                            "3. Mock Test Analytics: Mock test results are stored locally on device and never shared with third-party advertisers."
                )
            },
            confirmButton = {
                Button(
                    onClick = { showPrivacyPolicyDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan)
                ) {
                    Text("I Understand & Agree", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = AoeeCardBg,
            titleContentColor = Color.White,
            textContentColor = TextMutedSecondary
        )
    }
}
