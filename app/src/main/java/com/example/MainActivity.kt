package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.AppDatabase
import com.example.data.local.SafetyLogEntity
import com.example.data.local.TestResultEntity
import com.example.data.local.UserSessionManager
import com.example.data.model.ExamStream
import com.example.data.repository.ExamRepository
import com.example.data.repository.PunyansuAiRepository
import com.example.ui.screens.*
import com.example.ui.theme.AoeeTheme
import kotlinx.coroutines.launch

import com.example.util.NotificationHelper

enum class Screen {
    AUTH,
    AUTH_LOGIN,
    HOME,
    AI_CHAT,
    MOCK_TEST,
    TEST_RESULT,
    PAST_ATTEMPTS,
    PYQ_BANK,
    PERFORMANCE,
    STUDY_TIMER,
    FLASHCARDS,
    STUDY_INFO,
    SECURITY,
    UPI_PAYMENT
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        NotificationHelper.createNotificationChannel(this)

        val db = AppDatabase.getDatabase(this)
        val sessionManager = UserSessionManager(this)
        val aiRepository = PunyansuAiRepository(db.safetyLogDao())
        val examRepository = ExamRepository()

        setContent {
            AoeeTheme {
                val userProfile by sessionManager.userProfile.collectAsStateWithLifecycle()
                var currentScreen by remember { mutableStateOf(if (userProfile.isAuthenticated) Screen.HOME else Screen.AUTH) }
                
                var activeTestResult by remember { mutableStateOf<TestResultEntity?>(null) }
                var emergencyAlertData by remember { mutableStateOf<Pair<String, String>?>(null) }
                
                val safetyLogs by db.safetyLogDao().getAllLogs().collectAsStateWithLifecycle(initialValue = emptyList())
                val testResults by db.testResultDao().getAllTestResults().collectAsStateWithLifecycle(initialValue = emptyList())
                val studySessions by db.studySessionDao().getAllSessions().collectAsStateWithLifecycle(initialValue = emptyList())
                val totalStudyMinutes by db.studySessionDao().getTotalStudyMinutes().collectAsStateWithLifecycle(initialValue = 0)

                val coroutineScope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Crossfade(targetState = currentScreen, label = "screen_transition") { screen ->
                            when (screen) {
                                Screen.AUTH -> {
                                    AuthScreen(
                                        onLoginSuccess = { gmail, name, stream ->
                                            sessionManager.saveGmailAuth(gmail, name, stream)
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Logged in via Gmail: $gmail")
                                            }
                                            currentScreen = Screen.HOME
                                        }
                                    )
                                }

                                Screen.AUTH_LOGIN -> {
                                    AuthLoginScreen(
                                        currentProfile = userProfile,
                                        onLoginSuccess = { gmail, name, stream, pin ->
                                            sessionManager.saveGmailAuth(gmail, name, stream)
                                            if (pin.isNotEmpty()) {
                                                sessionManager.updateAppLock(true, pin)
                                            }
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Candidate $name authenticated via Gmail ($gmail)")
                                            }
                                            currentScreen = Screen.HOME
                                        },
                                        onBack = { currentScreen = Screen.HOME }
                                    )
                                }

                                Screen.HOME -> {
                                    HomeScreen(
                                        userProfile = userProfile,
                                        onNavigateToChat = { currentScreen = Screen.AI_CHAT },
                                        onNavigateToMockTest = { currentScreen = Screen.MOCK_TEST },
                                        onNavigateToPastAttempts = { currentScreen = Screen.PAST_ATTEMPTS },
                                        onNavigateToPyqBank = { currentScreen = Screen.PYQ_BANK },
                                        onNavigateToPerformance = { currentScreen = Screen.PERFORMANCE },
                                        onNavigateToStudyTimer = { currentScreen = Screen.STUDY_TIMER },
                                        onNavigateToFlashcards = { currentScreen = Screen.FLASHCARDS },
                                        onNavigateToStudyInfo = { currentScreen = Screen.STUDY_INFO },
                                        onNavigateToSecurity = { currentScreen = Screen.SECURITY },
                                        onNavigateToAuth = { currentScreen = Screen.AUTH_LOGIN },
                                        onNavigateToUpiPayment = { currentScreen = Screen.UPI_PAYMENT },
                                        onTriggerNotification = {
                                            NotificationHelper.showNotification(
                                                this@MainActivity,
                                                "AOEE Focus Study Alert 🔔",
                                                "Daily practice reminder: Complete a 15-min CBT Mock Test for ${userProfile.selectedStream.displayName}!"
                                            )
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Push notification triggered on your device!")
                                            }
                                        },
                                        onTriggerSafetyTest = {
                                            emergencyAlertData = Pair(
                                                "Punyansu AI ku olta question (Safety Test Attempt)",
                                                "PUNYANSU-EMERGENCY-DELHI-POLICE-9821"
                                            )
                                        },
                                        onLogout = {
                                            sessionManager.logout()
                                            currentScreen = Screen.AUTH
                                        }
                                    )
                                }

                                Screen.AI_CHAT -> {
                                    AiChatScreen(
                                        repository = aiRepository,
                                        onBack = { currentScreen = Screen.HOME },
                                        onTriggerSafetyAlert = { promptText, alertCode ->
                                            emergencyAlertData = Pair(promptText, alertCode)
                                        }
                                    )
                                }

                                Screen.MOCK_TEST -> {
                                    val mockTests = examRepository.getMockTestsForStream(userProfile.selectedStream)
                                    val activeMockTest = mockTests.firstOrNull() ?: examRepository.getMockTestsForStream(ExamStream.ENGINEERING).first()

                                    MockTestScreen(
                                        mockTest = activeMockTest,
                                        onBack = { currentScreen = Screen.HOME },
                                        onFinishTest = { resultEntity ->
                                            activeTestResult = resultEntity
                                            coroutineScope.launch {
                                                db.testResultDao().insertTestResult(resultEntity)
                                            }
                                            currentScreen = Screen.TEST_RESULT
                                        }
                                    )
                                }

                                Screen.TEST_RESULT -> {
                                    activeTestResult?.let { result ->
                                        TestResultScreen(
                                            testResult = result,
                                            userProfile = userProfile,
                                            onBackToHome = { currentScreen = Screen.HOME },
                                            onTakeNewTest = { currentScreen = Screen.MOCK_TEST },
                                            onViewPastAttempts = { currentScreen = Screen.PAST_ATTEMPTS }
                                        )
                                    } ?: run {
                                        currentScreen = Screen.HOME
                                    }
                                }

                                Screen.PAST_ATTEMPTS -> {
                                    PastAttemptsScreen(
                                        userProfile = userProfile,
                                        testResults = testResults,
                                        onBack = { currentScreen = Screen.HOME },
                                        onTakeTest = { currentScreen = Screen.MOCK_TEST },
                                        onDeleteAttempt = { attemptId ->
                                            coroutineScope.launch {
                                                db.testResultDao().deleteTestResultById(attemptId)
                                                snackbarHostState.showSnackbar("Exam attempt removed from local storage")
                                            }
                                        },
                                        onClearAllAttempts = {
                                            coroutineScope.launch {
                                                db.testResultDao().clearAll()
                                                snackbarHostState.showSnackbar("All exam history cleared from Room database")
                                            }
                                        }
                                    )
                                }

                                Screen.PYQ_BANK -> {
                                    PyqBankScreen(
                                        examRepository = examRepository,
                                        isPremiumUnlocked = userProfile.isPremiumUnlocked,
                                        onNavigateToPayment = { currentScreen = Screen.UPI_PAYMENT },
                                        onBack = { currentScreen = Screen.HOME }
                                    )
                                }

                                Screen.PERFORMANCE -> {
                                    PerformanceTrackerScreen(
                                        userProfile = userProfile,
                                        testResults = testResults,
                                        totalStudyMinutes = totalStudyMinutes ?: 0,
                                        onBack = { currentScreen = Screen.HOME },
                                        onTakeTest = { currentScreen = Screen.MOCK_TEST },
                                        onNavigateToPastAttempts = { currentScreen = Screen.PAST_ATTEMPTS }
                                    )
                                }

                                Screen.STUDY_TIMER -> {
                                    StudyTimerScreen(
                                        sessions = studySessions,
                                        onSaveSession = { session ->
                                            coroutineScope.launch {
                                                db.studySessionDao().insertSession(session)
                                            }
                                        },
                                        onBack = { currentScreen = Screen.HOME }
                                    )
                                }

                                Screen.FLASHCARDS -> {
                                    FlashcardsScreen(
                                        flashcards = examRepository.getFlashcards(),
                                        onBack = { currentScreen = Screen.HOME }
                                    )
                                }

                                Screen.STUDY_INFO -> {
                                    StudyInfoScreen(
                                        topics = examRepository.getStudyInfoTopics(),
                                        onBack = { currentScreen = Screen.HOME }
                                    )
                                }

                                Screen.SECURITY -> {
                                    SecurityPrivacyScreen(
                                        userProfile = userProfile,
                                        safetyLogs = safetyLogs,
                                        onToggleAppLock = { enabled ->
                                            sessionManager.updateAppLock(enabled)
                                        },
                                        onClearLogs = {
                                            coroutineScope.launch {
                                                db.safetyLogDao().clearLogs()
                                            }
                                        },
                                        onBack = { currentScreen = Screen.HOME }
                                    )
                                }

                                Screen.UPI_PAYMENT -> {
                                    UpiPaymentScreen(
                                        userProfile = userProfile,
                                        sessionManager = sessionManager,
                                        onBack = { currentScreen = Screen.HOME },
                                        onPaymentSuccess = { planName ->
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("🎉 $planName unlocked! Direct Bank & UPI transfer confirmed.")
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        emergencyAlertData?.let { (prompt, code) ->
                            EmergencyPoliceAlertModal(
                                alertCode = code,
                                triggerPrompt = prompt,
                                onDismiss = { emergencyAlertData = null },
                                onCallDelhiPolice = {
                                    val callIntent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:01123461000")
                                    }
                                    try {
                                        startActivity(callIntent)
                                    } catch (e: Exception) {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Initiating Dial Action to Delhi Police Cyber Cell (+91 11 2346 1000)")
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

