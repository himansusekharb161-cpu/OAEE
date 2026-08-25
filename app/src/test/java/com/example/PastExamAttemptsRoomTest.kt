package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.TestResultDao
import com.example.data.local.TestResultEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class PastExamAttemptsRoomTest {

    private lateinit var database: AppDatabase
    private lateinit var testResultDao: TestResultDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        testResultDao = database.testResultDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun `verify past exam attempts store and retrieve scores and dates via Room`() = runBlocking {
        val attempt1 = TestResultEntity(
            id = "attempt_1",
            testTitle = "OJEE Engineering CBT Full Mock Test 2026",
            streamCode = "ENGINEERING",
            totalQuestions = 20,
            correctAnswers = 16,
            wrongAnswers = 4,
            unattempted = 0,
            scorePercentage = 80.0f,
            timeSpentSeconds = 720,
            timestamp = 1771980000000L,
            aiFeedbackSummary = "Strong performance in Mechanics & Calculus."
        )

        val attempt2 = TestResultEntity(
            id = "attempt_2",
            testTitle = "Navodaya Class 6 Mental Ability Speed Test",
            streamCode = "NAVODAYA_CLASS_6",
            totalQuestions = 15,
            correctAnswers = 14,
            wrongAnswers = 1,
            unattempted = 0,
            scorePercentage = 93.33f,
            timeSpentSeconds = 480,
            timestamp = 1771985000000L,
            aiFeedbackSummary = "Exceptional visual reasoning speed."
        )

        testResultDao.insertTestResult(attempt1)
        testResultDao.insertTestResult(attempt2)

        val attempts = testResultDao.getAllTestResults().first()

        assertEquals(2, attempts.size)
        // Check order: newest first
        assertEquals("attempt_2", attempts[0].id)
        assertEquals(93.33f, attempts[0].scorePercentage, 0.01f)
        assertEquals(1771985000000L, attempts[0].timestamp)

        assertEquals("attempt_1", attempts[1].id)
        assertEquals(80.0f, attempts[1].scorePercentage, 0.01f)
        assertEquals(1771980000000L, attempts[1].timestamp)

        // Delete single attempt
        testResultDao.deleteTestResultById("attempt_1")
        val afterDelete = testResultDao.getAllTestResults().first()
        assertEquals(1, afterDelete.size)
        assertEquals("attempt_2", afterDelete[0].id)
    }
}
