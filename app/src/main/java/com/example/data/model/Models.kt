package com.example.data.model

import androidx.annotation.Keep

@Keep
enum class ExamStream(val displayName: String, val code: String, val description: String, val ageGroup: String) {
    SCHOOL_FOUNDATION("Class 1-10 Foundation & Olympiad", "SCHOOL_CLASS_1_10", "Primary Talent Search, NRTS, NMMS & Board Exams", "Ages 6-16"),
    NAVODAYA_ENTRANCE("JNVST Navodaya Entrance (Class 6 & 9)", "JNVST_NAVODAYA", "Mental Ability, Arithmetic, English & Odia Language", "Class 5th & 8th Students"),
    PATHANI_SAMANTA("Pathani Samanta Math Scholarship (PSMSE)", "PSMSE_MATH", "Advanced Math Aptitude, Logic & Numerical Reasoning", "Class 6th & 9th Students"),
    OAV_ENTRANCE("Odisha Adarsha Vidyalaya Entrance (OAV Class 6 & 9)", "OAV_ENTRANCE", "English, Odia, Mathematics, Science & Social Studies", "Class 5th & 8th Students"),
    SECONDARY_CHSE("Class 11-12 CHSE / CBSE Entrance", "CHSE_CLASS_11_12", "Physics, Chemistry, Math, Biology, Commerce, Arts", "Ages 16-18"),
    CT_ENTRANCE("Odisha CT / D.El.Ed Teacher Entrance", "CT_DEL_ED", "Child Development, Pedagogy, Science, Social Studies, Odia", "Ages 17-30"),
    ENGINEERING("OJEE B.Tech / Engineering", "OJEE_ENG", "Physics, Chemistry, Mathematics", "Ages 17-25"),
    OJEE_ALL("OJEE All Streams (B.Tech, B.Pharm, MCA, MBA, M.Tech)", "OJEE_ALL", "Engineering, Management, Pharmacy & Computer Applications", "Ages 17-35"),
    DIPLOMA_DET("Odisha Diploma / DET", "DET_DIPLOMA", "Physics, Chemistry, Math, General Ability", "Ages 15-22"),
    MEDICAL_PHARMA("OJEE B.Pharm / Paramedical", "OJEE_MED", "Physics, Chemistry, Biology/Math", "Ages 17-25"),
    GRADUATION_CPET("Odisha CPET / PG Entrance", "CPET_DEGREE", "UG Subject Specialization, General Awareness, Reasoning", "Graduates (Ages 20-35)"),
    TEACHER_OTET("Odisha B.Ed / OTET / OSSTET", "BED_OTET", "Child Development, Pedagogy, Odia, English, GK", "Graduates (Ages 21-45)"),
    OAS_IAS_CIVIL("OAS / IAS Civil Services Entrance (OPSC / UPSC)", "OAS_IAS_CIVIL", "General Studies, Odisha History, Polity, CSAT & Ethics", "Graduates (Ages 21-38)"),
    ADULT_CONTINUING_ED("OSOU Adult & Continuing Ed (Up to 55 Yrs)", "ADULT_OSOU_55", "OSOU Open University, Skill Certifications & Distance Learning", "Adults (Up to 55 Yrs)"),
    ODISHA_ALL_ENTRANCE("Odisha All Govt & Technical Entrances (OSSC, OSSSC)", "ODISHA_ALL_GOVT", "Odisha GK, General English, Arithmetic, Reasoning", "Ages 18-42"),
    ITI_POLYTECHNIC("Odisha ITI & Polytechnic", "ITI_POLY", "Basic Science, Numerical Aptitude, Odia/English", "Ages 14-30")
}

@Keep
data class Question(
    val id: String,
    val subject: String,
    val questionText: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String,
    val tag: String, // e.g., "PAST 2023", "PAST 2021", "FUTURE 2026 AI PREDICTED", "FUTURE 2027 HIGH PROBABILITY"
    val difficulty: String = "Medium" // Easy, Medium, Hard
)

@Keep
data class MockTest(
    val id: String,
    val title: String,
    val stream: ExamStream,
    val durationMinutes: Int,
    val questions: List<Question>,
    val description: String,
    val isAiGenerated: Boolean = true
)

@Keep
data class Flashcard(
    val id: String,
    val subject: String,
    val questionOrTopic: String,
    val answerOrConcept: String,
    val tag: String,
    val level: String = "All Streams"
)

@Keep
data class StudyInfoTopic(
    val id: String,
    val title: String,
    val category: String, // Exam Schedule, Syllabus, Eligibility & Age Criteria, Counseling
    val content: String,
    val targetGroup: String
)

@Keep
data class UserProfile(
    val email: String = "himansusekharb161@gmail.com",
    val phoneNumber: String = "",
    val name: String = "Odisha Student",
    val selectedStream: ExamStream = ExamStream.ENGINEERING,
    val isAuthenticated: Boolean = false,
    val isAppLockEnabled: Boolean = false,
    val appLockPin: String = "",
    val isPremiumUnlocked: Boolean = false,
    val premiumPlan: String = "Free",
    val paymentUtr: String = "",
    val createdTimestamp: Long = System.currentTimeMillis()
)

@Keep
data class SafetyLog(
    val id: String,
    val timestamp: Long,
    val promptText: String,
    val alertCode: String,
    val actionTaken: String
)

