package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.local.UserSessionManager
import com.example.data.repository.DailyStudyTipsProvider
import com.example.util.DailyStudyTipsManager

class StudyTipsAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val sessionManager = UserSessionManager(context)
        val userProfile = sessionManager.userProfile.value
        val stream = userProfile.selectedStream

        // Check if daily tips notification is enabled
        if (DailyStudyTipsManager.isNotificationsEnabled(context)) {
            val tip = DailyStudyTipsProvider.getTipOfTheDay(stream)
            DailyStudyTipsManager.showDailyTipNotification(context, tip)

            // Reschedule next day's alarm
            DailyStudyTipsManager.scheduleDailyTipAlarm(context)
        }
    }
}
