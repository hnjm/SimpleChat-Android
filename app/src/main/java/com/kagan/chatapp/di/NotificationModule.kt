package com.kagan.chatapp.di

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.kagan.chatapp.notification.NotificationUtil
import com.kagan.chatapp.notification.NotificationUtil.Companion.CHANNEL_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Singleton
    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Singleton
    @Provides
    fun provideNotificationCompat(@ApplicationContext context: Context): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
    }

    @Singleton
    @Provides
    fun provideNotificationUtil(
        @ApplicationContext context: Context,
        notificationManager: NotificationManager,
        notificationBuilder: NotificationCompat.Builder
    ): NotificationUtil {
        return NotificationUtil(context, notificationManager, notificationBuilder)
    }
}