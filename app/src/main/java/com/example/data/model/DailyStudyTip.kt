package com.example.data.model

data class DailyStudyTip(
    val id: String,
    val title: String,
    val category: String,
    val examTarget: String,
    val englishAdvice: String,
    val odiaAdvice: String,
    val actionableRule: String,
    val targetStream: ExamStream? = null,
    val tags: List<String> = emptyList()
)
