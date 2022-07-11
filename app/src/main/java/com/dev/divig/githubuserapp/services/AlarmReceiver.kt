package com.dev.divig.githubuserapp.services

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.dev.divig.githubuserapp.R
import com.dev.divig.githubuserapp.view.MainActivity
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(EXTRA_TITLE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val bigMessage = intent.getStringExtra(EXTRA_BIG_MESSAGE)
        setNotification(context, title, message, bigMessage)
    }

    private fun setNotification(
        context: Context,
        title: String?,
        message: String?,
        bigMessage: String?
    ) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, 0,
            intent, PendingIntent.FLAG_ONE_SHOT
        )

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val vibrate = longArrayOf(delay, delay, delay, delay, delay)
        val style = NotificationCompat.BigTextStyle().bigText(bigMessage).setBigContentTitle(title)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setVibrate(vibrate)
            .setSound(notificationSound)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(style)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(ID_ALARM, notification)
    }


    fun setDailyReminder(
        context: Context,
        title: String,
        message: String,
        bigMessage: String,
        status: String
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(EXTRA_TITLE, title)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_BIG_MESSAGE, bigMessage)

        val timeArray =
            SET_TIME.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ID_ALARM, intent, PendingIntent.FLAG_ONE_SHOT
        )
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        showToast(context, status)
    }

    fun cancelAlarm(context: Context?, status: String) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_ALARM, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        showToast(context, status)
    }

    private fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_BIG_MESSAGE = "big_message"
        const val channelId = "Channel_1"
        const val channelName = "DailyReminder channel"
        const val delay: Long = 1000

        private const val ID_ALARM = 101
        private const val SET_TIME = "09:00"
    }
}
