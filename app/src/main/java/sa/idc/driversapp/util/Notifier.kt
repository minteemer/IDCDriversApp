package sa.idc.driversapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import sa.idc.driversapp.IDCDriversApp
import sa.idc.driversapp.R
import sa.idc.driversapp.domain.entities.support.SupportChatMessage
import sa.idc.driversapp.presentation.driverTasksList.view.DriverTasksListActivity
import sa.idc.driversapp.presentation.supportChat.view.SupportChatActivity

object Notifier {

    private val appContext: Context by lazy { IDCDriversApp.instance }

    private val notificationManager
            by lazy { appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    private object Channels {
        const val NEW_TASKS = "new_tasks_notifications"
        const val NEW_SUPPORT_MESSAGE = "new_support_message_notification"
    }

    fun sendNewTaskNotification(taskId: Long) {
        val intent = Intent(appContext, DriverTasksListActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
                appContext,
                System.currentTimeMillis().toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = appContext.getString(R.string.new_tasks_assigned_channel_id)

        val notificationBuilder = NotificationCompat.Builder(appContext, channelId)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
                .setContentTitle(appContext.getString(R.string.new_task_notification_title))
                .setContentText(appContext.getString(R.string.new_task_notification_text))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                    channelId,
                    appContext.getString(R.string.new_tasks_assigned_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
            ).let { notificationManager.createNotificationChannel(it) }
        }

        notificationManager.notify(
                Channels.NEW_TASKS,
                1,
                notificationBuilder.build()
        )

    }

    fun sendNewSupportChatMessage(message: SupportChatMessage) {
        val intent = Intent(appContext, SupportChatActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
                appContext,
                System.currentTimeMillis().toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = appContext.getString(R.string.new_support_chat_message_channel_id)

        val notificationBuilder = NotificationCompat.Builder(appContext, channelId)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentTitle(message.name)
                .setContentText(message.text)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                    channelId,
                    appContext.getString(R.string.new_support_chat_message_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
            ).let { notificationManager.createNotificationChannel(it) }
        }

        notificationManager.notify(
                Channels.NEW_SUPPORT_MESSAGE,
                1,
                notificationBuilder.build()
        )
    }
}