package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudyInfoTopic
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyInfoScreen(
    topics: List<StudyInfoTopic>,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Study Information & Guidelines 📖", fontWeight = FontWeight.Bold, color = TextWhitePrimary) },
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
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(ElectricCyan.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(28.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Official Entrance Portal Guide",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhitePrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Covers Class 1 to 12 & Adult OSOU Education (Up to 55 Yrs)",
                                fontSize = 12.sp,
                                color = CyanGlow
                            )
                        }
                    }
                }
            }

            items(topics) { topic ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = AoeeCardBg),
                    border = BorderStroke(1.dp, AoeeCardBorder)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = ElectricCyan.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = topic.category.uppercase(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ElectricCyan,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Text(
                                text = topic.targetGroup,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextMutedSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = topic.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhitePrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = topic.content,
                            fontSize = 13.sp,
                            color = TextWhitePrimary,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}
