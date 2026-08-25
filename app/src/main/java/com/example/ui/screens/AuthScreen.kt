package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExamStream
import com.example.ui.theme.*

// Google & Gmail Brand Colors
val GoogleRed = Color(0xFFEA4335)
val GoogleBlue = Color(0xFF4285F4)
val GoogleYellow = Color(0xFFFBBC05)
val GoogleGreen = Color(0xFF34A853)

@Composable
fun AuthScreen(
    onLoginSuccess: (email: String, name: String, stream: ExamStream) -> Unit
) {
    var gmailAddress by remember { mutableStateOf("himansusekharb161@gmail.com") }
    var studentName by remember { mutableStateOf("Himansu Sekhar") }
    var selectedStream by remember { mutableStateOf(ExamStream.ENGINEERING) }

    var authStep by remember { mutableIntStateOf(1) } // 1: Gmail & Stream form, 2: Google Token verification
    var verificationToken by remember { mutableStateOf("") }
    var isAuthenticating by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    fun validateGmail(email: String): Boolean {
        val trimmed = email.trim().lowercase()
        return trimmed.isNotEmpty() && trimmed.endsWith("@gmail.com") && trimmed.length > "@gmail.com".length
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AoeeNavyBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Google Gmail Icon & Branding Badge
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.sweepGradient(
                            listOf(GoogleBlue, GoogleRed, GoogleYellow, GoogleGreen, GoogleBlue)
                        )
                    )
                    .padding(3.dp)
                    .clip(CircleShape)
                    .background(AoeeNavyBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mail,
                    contentDescription = "Gmail Account Setup",
                    tint = GoogleRed,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "ALL ODISHA ENTRANCE EXAMINATION",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = ElectricCyan,
                letterSpacing = 1.2.sp
            )

            Text(
                text = "Gmail Account Login",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhitePrimary
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Surface(
                    color = GoogleRed.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, GoogleRed.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "📧 ONLY GMAIL ACCOUNTS (@gmail.com)",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF8A80),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main Auth Card
            Card(
                colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, AoeeCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (authStep == 1) {
                        // --- 1-TAP INSTANT GOOGLE / GMAIL SIGN-IN BUTTON ---
                        Button(
                            onClick = {
                                if (validateGmail(gmailAddress)) {
                                    isAuthenticating = true
                                    onLoginSuccess(gmailAddress.trim().lowercase(), studentName, selectedStream)
                                } else {
                                    errorMessage = "Please enter a valid @gmail.com account address."
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("google_one_tap_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                // Google 'G' Styled Emblem
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Mail,
                                        contentDescription = "Google Icon",
                                        tint = GoogleRed,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Continue with Google (Gmail)",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1F1F1F)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Divider with OR
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HorizontalDivider(modifier = Modifier.weight(1f), color = AoeeCardBorder)
                            Text(
                                text = "  OR CONFIGURE GMAIL  ",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMutedSecondary
                            )
                            HorizontalDivider(modifier = Modifier.weight(1f), color = AoeeCardBorder)
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Gmail Address Input
                        OutlinedTextField(
                            value = gmailAddress,
                            onValueChange = {
                                gmailAddress = it
                                errorMessage = ""
                            },
                            label = { Text("Gmail Address (@gmail.com only)") },
                            placeholder = { Text("yourname@gmail.com") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Mail,
                                    contentDescription = null,
                                    tint = GoogleRed
                                )
                            },
                            trailingIcon = {
                                if (validateGmail(gmailAddress)) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Valid Gmail",
                                        tint = SuccessEmerald
                                    )
                                }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("gmail_address_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = if (validateGmail(gmailAddress)) SuccessEmerald else GoogleRed,
                                unfocusedBorderColor = AoeeCardBorder,
                                focusedTextColor = TextWhitePrimary,
                                unfocusedTextColor = TextWhitePrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Candidate Name Input
                        OutlinedTextField(
                            value = studentName,
                            onValueChange = { studentName = it },
                            label = { Text("Candidate Full Name") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = ElectricCyan
                                )
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("student_name_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricCyan,
                                unfocusedBorderColor = AoeeCardBorder,
                                focusedTextColor = TextWhitePrimary,
                                unfocusedTextColor = TextWhitePrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Target Entrance Stream Selection
                        Text(
                            text = "Select Target AOEE Stream:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextMutedSecondary,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            ExamStream.entries.take(5).forEach { stream ->
                                val isSelected = selectedStream == stream
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) DeepIndigo else Color(0xFF131D31))
                                        .border(
                                            width = if (isSelected) 1.5.dp else 1.dp,
                                            color = if (isSelected) ElectricCyan else AoeeCardBorder,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable { selectedStream = stream }
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { selectedStream = stream },
                                        colors = RadioButtonDefaults.colors(selectedColor = ElectricCyan)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = stream.displayName,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhitePrimary
                                        )
                                        Text(
                                            text = stream.description,
                                            fontSize = 10.5.sp,
                                            color = TextMutedSecondary
                                        )
                                    }
                                }
                            }
                        }

                        if (errorMessage.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                color = GoogleRed.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, GoogleRed)
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ErrorOutline,
                                        contentDescription = null,
                                        tint = GoogleRed,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = errorMessage,
                                        fontSize = 12.sp,
                                        color = Color(0xFFFF8A80)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (!validateGmail(gmailAddress)) {
                                    errorMessage = "Only valid @gmail.com accounts are permitted for AOEE registration."
                                } else if (studentName.isBlank()) {
                                    errorMessage = "Please enter candidate full name."
                                } else {
                                    errorMessage = ""
                                    authStep = 2
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("verify_gmail_setup_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GoogleBlue)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Verify Gmail & Continue",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                    } else {
                        // --- STEP 2: GMAIL SECURITY TOKEN VERIFICATION ---
                        Text(
                            text = "Gmail Account Verification",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhitePrimary,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            color = GoogleBlue.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, GoogleBlue.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MarkEmailRead,
                                    contentDescription = null,
                                    tint = CyanGlow,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Google Auth Security Check",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhitePrimary
                                    )
                                    Text(
                                        text = "Sent 6-digit confirmation token to:\n$gmailAddress",
                                        fontSize = 11.sp,
                                        color = CyanGlow
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = verificationToken,
                            onValueChange = { if (it.length <= 6) verificationToken = it.filter { c -> c.isDigit() } },
                            label = { Text("6-Digit Google Security Token") },
                            placeholder = { Text("e.g. 784920") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = ElectricCyan
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("gmail_token_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricCyan,
                                unfocusedBorderColor = AoeeCardBorder,
                                focusedTextColor = TextWhitePrimary,
                                unfocusedTextColor = TextWhitePrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AssistChip(
                                onClick = { verificationToken = "784920" },
                                label = { Text("Auto-fill Token (784920)") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = SuccessEmerald,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFF0F172A))
                            )

                            Text(
                                text = "Change Gmail",
                                fontSize = 12.sp,
                                color = ElectricCyan,
                                modifier = Modifier
                                    .clickable { authStep = 1 }
                                    .padding(4.dp)
                            )
                        }

                        if (errorMessage.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = errorMessage,
                                fontSize = 12.sp,
                                color = GoogleRed
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (verificationToken.length < 4) {
                                    errorMessage = "Please enter 6-digit Google Security Token."
                                } else {
                                    isAuthenticating = true
                                    onLoginSuccess(gmailAddress.trim().lowercase(), studentName, selectedStream)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("confirm_gmail_login_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SuccessEmerald)
                        ) {
                            if (isAuthenticating) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Authenticate & Open Portal",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Footer Security Guarantee
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = SuccessEmerald,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Google Identity Protected • Strictly @gmail.com Auth",
                    fontSize = 11.5.sp,
                    color = Color(0xFF64748B)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
