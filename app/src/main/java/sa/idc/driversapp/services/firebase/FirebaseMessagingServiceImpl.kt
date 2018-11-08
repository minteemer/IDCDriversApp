package sa.idc.driversapp.services.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.schedulers.Schedulers
import sa.idc.driversapp.domain.entities.support.SupportChatMessage
import sa.idc.driversapp.domain.interactors.support.SupportInteractor
import sa.idc.driversapp.presentation.supportChat.view.SupportChatActivity
import sa.idc.driversapp.util.Notifier

class FirebaseMessagingServiceImpl : FirebaseMessagingService() {

    companion object {
        private const val LOG_TAG = "MessagingService"

        private const val TYPE_FIELD = "type"
    }

    private object Messages {
        object NewTask {
            const val TYPE_NAME = "task"

            object Fields {
                const val ID = "id"
            }
        }

        object SupportChatMessage {
            const val TYPE_NAME = "message"

            object Fields {
                const val OPERATOR_NAME = "operator_name"
                const val TEXT = "text"
                const val DATE = "posted_date"
            }
        }
    }


    private val supportInteractor: SupportInteractor by lazy { SupportInteractor() }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(LOG_TAG, "Firebase message ${message.messageId} received: ${message.data}")

        when (message.data[TYPE_FIELD]) {
            Messages.NewTask.TYPE_NAME -> handleNewTaskMessage(message)
            Messages.SupportChatMessage.TYPE_NAME -> handleSupportMessage(message)
            else -> Log.e(LOG_TAG, "Unknown message type!")
        }
    }

    private fun handleSupportMessage(message: RemoteMessage) {
        with(Messages.SupportChatMessage.Fields) {
            val operatorName = message.data[OPERATOR_NAME]
            val text = message.data[TEXT]
            val date = message.data[DATE]

            if (operatorName != null && text != null && date != null) {
                SupportChatMessage(operatorName, text, date.toLong()).let {
                    supportInteractor.saveReceivedMessage(it)
                            .subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.computation())
                            .subscribe(
                                    { Log.d(LOG_TAG, "$message saved") },
                                    { e -> Log.d(LOG_TAG, "Error while saving $message", e) }
                            )

                    if (!SupportChatActivity.isInForeground)
                        Notifier.sendNewSupportChatMessage(it)
                }
            }
        }
    }

    private fun handleNewTaskMessage(message: RemoteMessage) {
        message.data[Messages.NewTask.Fields.ID]?.toLongOrNull()?.let {
            Notifier.sendNewTaskNotification(it)
        }
    }
}