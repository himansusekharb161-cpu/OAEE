package com.example.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.R
import com.example.data.local.UserSessionManager
import com.example.data.model.DailyStudyTip
import com.example.data.model.ExamStream
import com.example.data.repository.DailyStudyTipsProvider
import com.example.receiver.StudyTipsAlarmReceiver
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DailyStudyTipsManager {

    private const val PREFS_NAME = "aoee_daily_study_tips_prefs"
    private const val KEY_NOTIFS_ENABLED = "key_notifs_enabled"
    private const val KEY_DELIVERY_HOUR = "key_delivery_hour"
    private const val KEY_DELIVERY_MINUTE = "key_delivery_minute"
    private const val KEY_BOOKMARKED_TIPS = "key_bookmarked_tips"
    private const val KEY_TIME_PRESET = "key_time_preset"

    const val CHANNEL_ID = "aoee_daily_study_tips"
    const val CHANNEL_NAME = "Odisha Exam Daily Study Tips"
    const val CHANNEL_DESC = "Daily high-yield study advice, exam hacks, formulas and strategy for Odisha competitive entrance exams."
    const val NOTIFICATION_ID_BASE = 8800

    enum class DeliveryTimePreset(val label: String, val odiaLabel: String, val hour: Int, val minute: Int, val emoji: String) {
        MORNING("Morning Mindset (7:00 AM)", "ପ୍ରଭାତ ଅଭ୍ୟାସ (୭:୦୦ AM)", 7, 0, "🌅"),
        AFTERNOON("Afternoon Speed Drill (2:00 PM)", "ଦ୍ୱିପ୍ରହର ଟେଷ୍ଟ (୨:୦୦ PM)", 14, 0, "⚡"),
        EVENING("Nightly High-Yield Review (8:00 PM)", "ସନ୍ଧ୍ୟା ପୁନରାବୃତ୍ତି (୮:୦୦ PM)", 20, 0, "🌙"),
        CUSTOM("Custom Time", "ନିଜ ପସନ୍ଦ ସମୟ", 7, 0, "⏰")
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isNotificationsEnabled(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_NOTIFS_ENABLED, true)
    }

    fun setNotificationsEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_NOTIFS_ENABLED, enabled).apply()
        if (enabled) {
            scheduleDailyTipAlarm(context)
        } else {
            cancelDailyTipAlarm(context)
        }
    }

    fun getDeliveryHour(context: Context): Int {
        return getPrefs(context).getInt(KEY_DELIVERY_HOUR, 7)
    }

    fun getDeliveryMinute(context: Context): Int {
        return getPrefs(context).getInt(KEY_DELIVERY_MINUTE, 0)
    }

    fun getDeliveryTimePreset(context: Context): DeliveryTimePreset {
        val name = getPrefs(context).getString(KEY_TIME_PRESET, DeliveryTimePreset.MORNING.name)
        return try {
            DeliveryTimePreset.valueOf(name ?: DeliveryTimePreset.MORNING.name)
        } catch (e: Exception) {
            DeliveryTimePreset.MORNING
        }
    }

    fun setDeliveryTime(context: Context, preset: DeliveryTimePreset, customHour: Int = 7, customMinute: Int = 0) {
        val hour = if (preset == DeliveryTimePreset.CUSTOM) customHour else preset.hour
        val minute = if (preset == DeliveryTimePreset.CUSTOM) customMinute else preset.minute

        getPrefs(context).edit()
            .putString(KEY_TIME_PRESET, preset.name)
            .putInt(KEY_DELIVERY_HOUR, hour)
            .putInt(KEY_DELIVERY_MINUTE, minute)
            .apply()

        if (isNotificationsEnabled(context)) {
            scheduleDailyTipAlarm(context)
        }
    }

    fun getBookmarkedTipIds(context: Context): Set<String> {
        return getPrefs(context).getStringSet(KEY_BOOKMARKED_TIPS, emptySet()) ?: emptySet()
    }

    fun toggleBookmark(context: Context, tipId: String): Boolean {
        val current = getBookmarkedTipIds(context).toMutableSet()
        val isNowBookmarked = if (current.contains(tipId)) {
            current.remove(tipId)
            false
        } else {
            current.add(tipId)
            true
        }
        getPrefs(context).edit().putStringSet(KEY_BOOKMARKED_TIPS, current).apply()
        return isNowBookmarked
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
                enableVibration(true)
                setShowBadge(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleDailyTipAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, StudyTipsAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            9001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val hour = getDeliveryHour(context)
        val minute = getDeliveryMinute(context)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // If the time already passed today, schedule for tomorrow
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            // Inexact fallback if exact alarm permission isn't granted
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancelDailyTipAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, StudyTipsAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            9001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun showDailyTipNotification(
        context: Context,
        tip: DailyStudyTip,
        notificationId: Int = NOTIFICATION_ID_BASE + (System.currentTimeMillis() % 1000).toInt()
    ) {
        createNotificationChannel(context)

        // Intent to open app
        val contentIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("navigate_to", "daily_study_tips")
            putExtra("tip_id", tip.id)
        }

        val contentPendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val bigText = buildString {
            append("🎯 [${tip.examTarget}]\n\n")
            append("💡 ${tip.englishAdvice}\n\n")
            append("📖 ${tip.odiaAdvice}\n\n")
            append("⚡ Rule: ${tip.actionableRule}")
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("💡 Odisha Daily Study Tip: ${tip.title}")
            .setContentText("${tip.englishAdvice} • ${tip.actionableRule}")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(bigText)
                    .setBigContentTitle("💡 Odisha Study Tip: ${tip.title}")
                    .setSummaryText(tip.category)
            )
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)

        try {
            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, builder.build())
            }
        } catch (e: SecurityException) {
            // POST_NOTIFICATIONS missing on Android 13+ if user revoked
        }
    }

    fun triggerTestNotification(context: Context, stream: ExamStream? = null) {
        val tip = DailyStudyTipsProvider.getTipOfTheDay(stream)
        showDailyTipNotification(context, tip, NOTIFICATION_ID_BASE + 99)
    }

    fun getFormattedNextScheduledTime(context: Context): String {
        val hour = getDeliveryHour(context)
        val minute = getDeliveryMinute(context)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val sdf = SimpleDateFormat("EEEE, hh:mm a", Locale.getDefault())
        return sdf.format(calendar.time)
    }
}
