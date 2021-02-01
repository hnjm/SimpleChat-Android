package com.kagan.chatapp.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.kagan.chatapp.notification.NotificationUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PushNotificationService : BroadcastReceiver() {
    @Inject lateinit var notificationUtils:NotificationUtil


    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("Broadcast", "onReceive: ")
        notificationUtils.show("name of sender", "Message description")
    }
}