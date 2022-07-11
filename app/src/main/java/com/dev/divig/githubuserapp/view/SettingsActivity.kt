package com.dev.divig.githubuserapp.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.dev.divig.githubuserapp.BuildConfig
import com.dev.divig.githubuserapp.R
import com.dev.divig.githubuserapp.services.AlarmReceiver
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : AppCompatActivity() {
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var key: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        alarmReceiver = AlarmReceiver()
        sharedPreferences = getSharedPreferences(TITLE_PREF, Context.MODE_PRIVATE)

        key = getString(R.string.key_daily_reminder)
        dailyReminder.isChecked = sharedPreferences.getBoolean(key, false)
        dailyReminder.setOnCheckedChangeListener { _, newValue ->
            if (newValue) {
                val title = getString(R.string.daily_reminder_title)
                val message = getString(R.string.notification_content)
                val bigMessage = getString(R.string.notification_big_content)
                val status = getString(R.string.daily_reminder_active)
                alarmReceiver.setDailyReminder(this, title, message, bigMessage, status)
            } else {
                val status = getString(R.string.daily_reminder_cancelled)
                alarmReceiver.cancelAlarm(this, status)
            }
            saveChangePreference(newValue)
        }
    }

    private fun saveChangePreference(value: Boolean) {
        sharedPreferences.edit {
            putBoolean(key, value)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val TITLE_PREF = BuildConfig.APPLICATION_ID + "_preference"
    }
}