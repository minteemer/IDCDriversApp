package sa.idc.driversapp.services.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import sa.idc.driversapp.R
import sa.idc.driversapp.presentation.driverTasksList.view.DriverTasksListActivity

class FirebaseMessagingServiceImpl : FirebaseMessagingService() {

    companion object {
        private const val LOG_TAG = "MessagingService"
    }

    object NewTasksNotifications {
        const val CHANNEL_NAME = "new_tasks_notifications"
    }

    private val notificationManager
            by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(LOG_TAG, "Firebase message ${message.messageId} received: ${message.data}")

        sendNewTaskNotification(message)
    }


    private fun sendNewTaskNotification(message: RemoteMessage) {

        val intent = Intent(applicationContext, DriverTasksListActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent =
                PendingIntent.getActivity(
                        applicationContext,
                        System.currentTimeMillis().toInt(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                )

        val channelId = getString(R.string.new_tasks_assigned_channel_id)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(getString(R.string.new_task_notification_title))
                .setContentText(getString(R.string.new_task_notification_text))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    channelId,
                    getString(R.string.new_tasks_assigned_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(
                NewTasksNotifications.CHANNEL_NAME,
                1,
                notificationBuilder.build()
        )

    }
}