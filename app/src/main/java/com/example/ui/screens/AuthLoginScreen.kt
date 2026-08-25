package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExamStream
import com.example.data.model.UserProfile
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthLoginScreen(
    currentProfile: UserProfile,
    onLoginSuccess: (String, String, ExamStream, String) -> Unit,
    onBack: () -> Unit
) {
    var gmailInput by remember { mutableStateOf(currentProfile.email.ifEmpty { "himansusekharb161@gmail.com" }) }
    var nameInput by remember { mutableStateOf(currentProfile.name.ifEmpty { "Himansu Sekhar" }) }
    var selectedStream by remember { mutableStateOf(currentProfile.selectedStream) }
    var pinInput by remember { mutableStateOf("1234") }
    var streamDropdownExpanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun validateGmail(email: String): Boolean {
        val trimmed = email.trim().lowercase()
        return trimmed.isNotEmpty() && trimmed.endsWith("@gmail.com") && trimmed.length > "@gmail.com".length
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Candidate Gmail Auth 🔐", fontWeight = FontWeight.Bold, color = TextWhitePrimary) },
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
                    border = BorderStroke(1.5.dp, ElectricCyan)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEA4335).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mail,
                                contentDescription = null,
                                tint = Color(0xFFEA4335),
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "AOEE GOOGLE ACCOUNT PORTAL",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextWhitePrimary,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = "Strictly @gmail.com authentication for entrance ranks & session sync.",
                            fontSize = 12.sp,
                            color = CyanGlow,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                    border = BorderStroke(1.dp, AoeeCardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(text = "CANDIDATE GMAIL CREDENTIALS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)

                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text("Full Name") },
                            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = ElectricCyan) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = gmailInput,
                            onValueChange = { gmailInput = it },
                            label = { Text("Gmail Address (@gmail.com)") },
                            placeholder = { Text("yourname@gmail.com") },
                            leadingIcon = { Icon(imageVector = Icons.Default.Mail, contentDescription = null, tint = Color(0xFFEA4335)) },
                            trailingIcon = {
                                if (validateGmail(gmailInput)) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Verified Gmail",
                                        tint = SuccessEmerald
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        // Entrance Exam Stream Selection Dropdown
                        ExposedDropdownMenuBox(
                            expanded = streamDropdownExpanded,
                            onExpandedChange = { streamDropdownExpanded = !streamDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedStream.displayName,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Target Entrance Exam Stream") },
                                leadingIcon = { Icon(imageVector = Icons.Default.School, contentDescription = null, tint = ElectricCyan) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = streamDropdownExpanded) },
                                modifier = Modifier
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            )

                            ExposedDropdownMenu(
                                expanded = streamDropdownExpanded,
                                onDismissRequest = { streamDropdownExpanded = false }
                            ) {
                                ExamStream.entries.forEach { stream ->
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text(stream.displayName, fontWeight = FontWeight.Bold)
                                                Text(stream.description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            }
                                        },
                                        onClick = {
                                            selectedStream = stream
                                            streamDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = pinInput,
                            onValueChange = { if (it.length <= 6) pinInput = it },
                            label = { Text("Security Passcode / PIN (4-6 Digits)") },
                            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = ElectricCyan) },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        if (errorMessage != null) {
                            Text(text = errorMessage!!, color = PoliceRedAlert, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                if (!validateGmail(gmailInput)) {
                                    errorMessage = "Only valid @gmail.com accounts are permitted for AOEE authentication."
                                } else if (nameInput.isBlank()) {
                                    errorMessage = "Please enter valid Candidate Name."
                                } else if (pinInput.length < 4) {
                                    errorMessage = "PIN must be at least 4 digits."
                                } else {
                                    errorMessage = null
                                    onLoginSuccess(gmailInput.trim().lowercase(), nameInput, selectedStream, pinInput)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SuccessEmerald)
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sign In with Gmail & Save Session", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
