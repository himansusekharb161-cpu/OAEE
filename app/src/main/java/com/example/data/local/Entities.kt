package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "test_results")
data class TestResultEntity(
    @PrimaryKey val id: String,
    val testTitle: String,
    val streamCode: String,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val unattempted: Int,
    val scorePercentage: Float,
    val timeSpentSeconds: Long,
    val timestamp: Long,
    val aiFeedbackSummary: String
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // "USER" or "PUNYANSU_AI"
    val text: String,
    val timestamp: Long,
    val isSafetyAlert: Boolean = false
)

@Entity(tableName = "safety_logs")
data class SafetyLogEntity(
    @PrimaryKey val id: String,
    val timestamp: Long,
    val triggerPrompt: String,
    val alertCode: String,
    val status: String
)

@Entity(tableName = "study_sessions")
data class StudySessionEntity(
    @PrimaryKey val id: String,
    val subject: String,
    val durationMinutes: Int,
    val timestamp: Long,
    val sessionType: String = "Pomodoro Focus"
)

