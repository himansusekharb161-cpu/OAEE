package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.util.DailyStudyTipsManager

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON"
        ) {
            if (DailyStudyTipsManager.isNotificationsEnabled(context)) {
                DailyStudyTipsManager.scheduleDailyTipAlarm(context)
            }
        }
    }
}
