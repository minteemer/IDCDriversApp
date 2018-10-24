package sa.idc.driversapp.services.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingServiceImpl : FirebaseMessagingService() {

    companion object {
        private const val LOG_TAG = "FBMessangingService"
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(LOG_TAG, message.data.toString())
    }
}