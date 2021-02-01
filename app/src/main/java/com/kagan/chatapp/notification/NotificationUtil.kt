package com.kagan.chatapp.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kagan.chatapp.R
import com.kagan.chatapp.ui.activities.LoginActivity
import javax.inject.Inject

class NotificationUtil
@Inject
constructor(
    private val context: Context,
    private val notificationManager: NotificationManager,
    private val notificationBuilder: NotificationCompat.Builder
) {


    init {
        createNotificationChannel()
    }

    companion object {
        const val CHANNEL_ID = "Received Message"
        const val notification_id = 1
    }

    private fun pendingIntent(): PendingIntent? {
        val intent = Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    private fun createNotification(): NotificationCompat.Builder {
        return notificationBuilder
            .setSmallIcon(R.drawable.ic_message)
            .setContentIntent(pendingIntent())
    }

    private fun createNotificationChannel() {
        Log.d("Broadcast", "createNotificationChannel: ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val important = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, important).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun show(contentTitle: String, contentText: String) {
        val builder = createNotification()
        builder.setContentTitle(contentTitle)
            .setContentText(contentText)

        with(NotificationManagerCompat.from(context)) {
            notify(notification_id, builder.build())
        }
    }
}