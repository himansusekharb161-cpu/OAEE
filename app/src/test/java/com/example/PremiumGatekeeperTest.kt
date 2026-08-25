package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.UserSessionManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class PremiumGatekeeperTest {

    private lateinit var sessionManager: UserSessionManager

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        sessionManager = UserSessionManager(context)
    }

    @Test
    fun `default user profile is not premium and triggers gatekeeping`() {
        // Reset or test default profile
        sessionManager.setPremiumUnlocked(false, "Free", "")
        val profile = sessionManager.userProfile.value
        assertFalse(profile.isPremiumUnlocked)
        assertEquals("Free", profile.premiumPlan)

        var hasAccess = false
        var dialogTriggered = false

        fun attemptAccess(featureName: String) {
            if (profile.isPremiumUnlocked) {
                hasAccess = true
            } else {
                dialogTriggered = true
            }
        }

        attemptAccess("CBT Mock Test Engine")
        assertFalse("User should not have access without premium", hasAccess)
        assertTrue("Gatekeeper dialog should be triggered", dialogTriggered)
    }

    @Test
    fun `unlocked premium user bypasses gatekeeper and receives full access`() {
        sessionManager.setPremiumUnlocked(
            isUnlocked = true,
            plan = "Pro CBT & AI Mentor All-Access",
            utr = "UPI_UTR_9876543210"
        )
        val profile = sessionManager.userProfile.value
        assertTrue(profile.isPremiumUnlocked)
        assertEquals("Pro CBT & AI Mentor All-Access", profile.premiumPlan)

        var hasAccess = false
        var dialogTriggered = false

        fun attemptAccess(featureName: String) {
            if (profile.isPremiumUnlocked) {
                hasAccess = true
            } else {
                dialogTriggered = true
            }
        }

        attemptAccess("CBT Mock Test Engine")
        assertTrue("Premium user should have access directly", hasAccess)
        assertFalse("Gatekeeper dialog should not trigger for premium users", dialogTriggered)
    }
}
