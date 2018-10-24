package sa.idc.driversapp.data.network.account

import com.google.gson.annotations.SerializedName

data class LogInData(
        @SerializedName("email") val login: String,
        @SerializedName("password") val password: String,
        @SerializedName("firebase_token") val firebaseToken: String
)