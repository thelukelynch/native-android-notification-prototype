package io.lukelynch.nativenotificationprototype

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class FCMMessage(val title: String?, val body: String?)
class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        val title: String?
        val body: String?

        // If the message contains a notification payload, show it
        if (remoteMessage.notification != null) {
            title = remoteMessage.notification!!.title
            body = remoteMessage.notification!!.body
        } else {
            // Otherwise, handle data payload
            title = remoteMessage.data["title"]
            body = remoteMessage.data["body"]
        }

        // Only show system notification if app is in background
        if (!isAppInForeground()) {
            sendNotification(title, body)
        }

        // Always update the in-app dialog
        messageStateFlow.value = FCMMessage(title, body)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
        // TODO: send token to app server if required
    }

    private fun sendNotification(title: String?, body: String?) {
        val channelId = "fcm_notifications"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Firebase Messages",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableVibration(true)
            channel.enableLights(true)
            channel.setSound(soundUri, android.media.AudioAttributes.Builder().setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION).build())
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title ?: "Notification")
            .setContentText(body ?: "")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSound(soundUri)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        Log.d(TAG, "Notification sent: $title - $body")
    }

    companion object {
        private const val TAG = "MyFcmService"
        private val messageStateFlow = MutableStateFlow<FCMMessage?>(null)
        private var appInForeground = false

        fun getMessageFlow(): StateFlow<FCMMessage?> = messageStateFlow
        fun clearMessage() {
            messageStateFlow.value = null
        }
        fun setAppInForeground(inForeground: Boolean) {
            appInForeground = inForeground
        }
        fun isAppInForeground(): Boolean = appInForeground
    }
}
