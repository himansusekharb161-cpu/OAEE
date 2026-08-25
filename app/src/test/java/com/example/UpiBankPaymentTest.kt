package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.UserSessionManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class UpiBankPaymentTest {

    private lateinit var sessionManager: UserSessionManager

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        sessionManager = UserSessionManager(context)
    }

    @Test
    fun `verify default UCO Bank and UPI details for Himansu Sekhar`() {
        val details = sessionManager.getBankDetails()
        assertEquals("07413211037750", details.accountNumber)
        assertEquals("UCBA0000741", details.ifscCode)
        assertEquals("himansusekharb161@okaxis", details.upiId)
        assertEquals("Himansu Sekhar", details.payeeName)
        assertEquals("UCO Bank", details.bankName)
        assertEquals("Jaraka Branch, Jajpur, Odisha", details.bankBranch)
    }

    @Test
    fun `verify saving custom bank account number and UPI details`() {
        sessionManager.saveBankDetails(
            accountNumber = "05120110009876",
            ifscCode = "UCBA0000512",
            bankName = "UCO Bank",
            bankBranch = "Jaraka Branch, Jajpur",
            payeeName = "Himansu Sekhar",
            upiId = "himansusekharb161@okaxis"
        )

        val updated = sessionManager.getBankDetails()
        assertEquals("05120110009876", updated.accountNumber)
        assertEquals("UCBA0000512", updated.ifscCode)
        assertEquals("Himansu Sekhar", updated.payeeName)
        assertEquals("UCO Bank", updated.bankName)

        sessionManager.setPremiumUnlocked(
            isUnlocked = true,
            plan = "Pro CBT & AI Mentor All-Access",
            utr = "423589123456"
        )

        val profile = sessionManager.userProfile.value
        assertTrue(profile.isPremiumUnlocked)
        assertEquals("Pro CBT & AI Mentor All-Access", profile.premiumPlan)
        assertEquals("423589123456", profile.paymentUtr)
    }
}
