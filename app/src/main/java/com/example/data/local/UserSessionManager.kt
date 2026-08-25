package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.ExamStream
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserSessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("aoee_user_session", Context.MODE_PRIVATE)

    private val _userProfile = MutableStateFlow(loadProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private fun loadProfile(): UserProfile {
        val email = prefs.getString("email", "himansusekharb161@gmail.com") ?: "himansusekharb161@gmail.com"
        val phone = prefs.getString("phone", "") ?: ""
        val name = prefs.getString("name", "Himansu Sekhar") ?: "Himansu Sekhar"
        val streamCode = prefs.getString("stream", ExamStream.ENGINEERING.name) ?: ExamStream.ENGINEERING.name
        val isAuth = prefs.getBoolean("is_auth", false)
        val appLock = prefs.getBoolean("app_lock", false)
        val pin = prefs.getString("app_pin", "") ?: ""
        val isPremium = prefs.getBoolean("is_premium", false)
        val plan = prefs.getString("premium_plan", "Free") ?: "Free"
        val utr = prefs.getString("payment_utr", "") ?: ""

        val stream = try {
            ExamStream.valueOf(streamCode)
        } catch (e: Exception) {
            ExamStream.ENGINEERING
        }

        return UserProfile(
            email = email,
            phoneNumber = phone,
            name = name,
            selectedStream = stream,
            isAuthenticated = isAuth,
            isAppLockEnabled = appLock,
            appLockPin = pin,
            isPremiumUnlocked = isPremium,
            premiumPlan = plan,
            paymentUtr = utr
        )
    }

    fun setPremiumUnlocked(isUnlocked: Boolean, plan: String = "Pro Plan", utr: String = "") {
        prefs.edit()
            .putBoolean("is_premium", isUnlocked)
            .putString("premium_plan", plan)
            .putString("payment_utr", utr)
            .apply()
        _userProfile.value = loadProfile()
    }

    data class BankAccountConfig(
        val accountNumber: String,
        val ifscCode: String,
        val bankName: String,
        val bankBranch: String,
        val payeeName: String,
        val upiId: String
    )

    fun getBankDetails(): BankAccountConfig {
        val accNo = prefs.getString("admin_account_number", "07413211037750") ?: "07413211037750"
        val ifsc = prefs.getString("admin_ifsc_code", "UCBA0000741") ?: "UCBA0000741"
        val bankName = prefs.getString("admin_bank_name", "UCO Bank") ?: "UCO Bank"
        val branch = prefs.getString("admin_bank_branch", "Jaraka Branch, Jajpur, Odisha") ?: "Jaraka Branch, Jajpur, Odisha"
        val payee = prefs.getString("admin_payee_name", "Himansu Sekhar") ?: "Himansu Sekhar"
        val upi = prefs.getString("admin_upi_id", "himansusekharb161@okaxis") ?: "himansusekharb161@okaxis"
        return BankAccountConfig(
            accountNumber = accNo,
            ifscCode = ifsc,
            bankName = bankName,
            bankBranch = branch,
            payeeName = payee,
            upiId = upi
        )
    }

    fun saveBankDetails(
        accountNumber: String,
        ifscCode: String,
        bankName: String,
        bankBranch: String,
        payeeName: String,
        upiId: String
    ) {
        prefs.edit()
            .putString("admin_account_number", accountNumber.trim())
            .putString("admin_ifsc_code", ifscCode.trim())
            .putString("admin_bank_name", bankName.trim())
            .putString("admin_bank_branch", bankBranch.trim())
            .putString("admin_payee_name", payeeName.trim())
            .putString("admin_upi_id", upiId.trim())
            .apply()
    }

    fun getUpiDetails(): Triple<String, String, String> {
        val details = getBankDetails()
        return Triple(details.upiId, details.payeeName, "${details.bankName} (${details.bankBranch})")
    }

    fun saveUpiDetails(upiId: String, payeeName: String, bankBranch: String) {
        prefs.edit()
            .putString("admin_upi_id", upiId)
            .putString("admin_payee_name", payeeName)
            .putString("admin_bank_branch", bankBranch)
            .apply()
    }

    fun saveAuth(emailOrPhone: String, name: String, stream: ExamStream) {
        val editor = prefs.edit()
        if (emailOrPhone.contains("@")) {
            editor.putString("email", emailOrPhone)
        } else {
            editor.putString("phone", emailOrPhone)
        }
        editor.putString("name", name)
            .putString("stream", stream.name)
            .putBoolean("is_auth", true)
            .apply()

        _userProfile.value = loadProfile()
    }

    fun saveGmailAuth(gmail: String, name: String, stream: ExamStream) {
        prefs.edit()
            .putString("email", gmail)
            .putString("name", name)
            .putString("stream", stream.name)
            .putBoolean("is_auth", true)
            .apply()

        _userProfile.value = loadProfile()
    }

    fun updateStream(stream: ExamStream) {
        prefs.edit().putString("stream", stream.name).apply()
        _userProfile.value = loadProfile()
    }

    fun updateAppLock(enabled: Boolean, pin: String = "") {
        prefs.edit()
            .putBoolean("app_lock", enabled)
            .putString("app_pin", pin)
            .apply()
        _userProfile.value = loadProfile()
    }

    fun logout() {
        prefs.edit().clear().apply()
        _userProfile.value = UserProfile()
    }
}
