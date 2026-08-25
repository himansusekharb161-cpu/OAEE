package com.example.data.model

import androidx.annotation.Keep

@Keep
data class SubjectWeightage(
    val subjectName: String,
    val questionCount: Int,
    val marks: Int,
    val weightagePercent: Int,
    val highYieldTopics: List<String>
)

@Keep
data class SyllabusModule(
    val moduleName: String,
    val subject: String,
    val topics: List<String>,
    val estimatedHours: Int,
    val aiPriority: String // "CRITICAL", "HIGH", "MODERATE"
)

@Keep
data class PatternSection(
    val sectionName: String,
    val questionCount: Int,
    val marksPerQuestion: Int,
    val negativeMarking: String,
    val recommendedTimeMins: Int
)

@Keep
data class CutoffStat(
    val category: String, // "General / UR", "SEBC / OBC", "SC", "ST", "PwD"
    val previousYearCutoff: String,
    val safeTargetScore: String
)

@Keep
data class TimelineEvent(
    val stage: String,
    val timeline: String,
    val status: String // "Upcoming", "Active", "Completed"
)

@Keep
data class ExamCategoryDetail(
    val stream: ExamStream,
    val tier: String,
    val conductingBody: String,
    val shortCode: String,
    val fullTitle: String,
    val odiaTitle: String,
    val tagLine: String,
    val examDurationMinutes: Int,
    val totalQuestions: Int,
    val totalMarks: Int,
    val markingScheme: String,
    val examMode: String,
    val examLanguage: String,
    val ageCriteria: String,
    val qualificationSummary: String,
    val eligibilityRules: List<String>,
    val subjects: List<SubjectWeightage>,
    val syllabusModules: List<SyllabusModule>,
    val patternSections: List<PatternSection>,
    val cutoffStats: List<CutoffStat>,
    val aiPredictions2026: List<String>,
    val counselingRoadmap: List<TimelineEvent>,
    val keyFormulas: List<Pair<String, String>> // Topic to formula/concept
)
