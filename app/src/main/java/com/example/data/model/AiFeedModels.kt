package com.example.data.model

import androidx.annotation.Keep

@Keep
data class AiQuestionTutorReport(
    val questionId: String,
    val subject: String,
    val questionText: String,
    val fullAiTutorResponse: String,
    val isRealTimeGemini: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

@Keep
data class AiExamDiagnosticReport(
    val examTitle: String,
    val scorePercentage: Float,
    val strengths: List<String>,
    val focusAreas: List<String>,
    val aiPersonalizedStrategy: String,
    val timestamp: Long = System.currentTimeMillis()
)
