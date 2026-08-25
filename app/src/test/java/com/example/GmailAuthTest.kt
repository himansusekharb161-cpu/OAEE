package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.UserSessionManager
import com.example.data.model.ExamStream
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class GmailAuthTest {

    @Test
    fun `verify user session manager saves and loads gmail account`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val sessionManager = UserSessionManager(context)

        val testGmail = "himansusekharb161@gmail.com"
        val testName = "Himansu Sekhar"
        val testStream = ExamStream.ENGINEERING

        sessionManager.saveGmailAuth(testGmail, testName, testStream)

        val profile = sessionManager.userProfile.value
        assertEquals(testGmail, profile.email)
        assertEquals(testName, profile.name)
        assertEquals(testStream, profile.selectedStream)
        assertTrue(profile.isAuthenticated)
        assertTrue(profile.email.endsWith("@gmail.com"))
    }
}
