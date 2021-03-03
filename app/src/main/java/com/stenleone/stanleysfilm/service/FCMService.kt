package com.stenleone.stanleysfilm.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.stenleone.stanleysfilm.R
import com.stenleone.stanleysfilm.ui.activity.MainActivity
import kotlin.random.Random

class FCMService : FirebaseMessagingService() {

    private val TITLE = "title"
    private val BODY = "body"
    private val CHANEL_ID = "chanel_id"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        showNotification(this, remoteMessage)
    }

    override fun onNewToken(token: String) {
        Log.v("112233", token)
        super.onNewToken(token)
    }

    private fun showNotification(context: Context, remoteMessage: RemoteMessage) {

        val channelId = context.getString(R.string.notification_channel_id)
        val channelName = context.getString(R.string.notification_channel_name)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }

        val title = remoteMessage.data[TITLE]
        val body = remoteMessage.data[BODY]
        val chanelId = remoteMessage.data[CHANEL_ID]

        val intent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context,
            Random.nextInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builtNotification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(remoteMessage.notification?.body))
            .setColor(ContextCompat.getColor(context, R.color.green_main))
            .setSmallIcon(R.drawable.app_logo)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setWhen(System.currentTimeMillis())
            .setShowWhen(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setVibrate(longArrayOf(200, 200, 200, 200))
            .setAutoCancel(true)
            .build()
        notificationManager.notify(Random.nextInt(), builtNotification)
    }

}