package sa.idc.driversapp.data.network.entities.account

import com.google.gson.annotations.SerializedName

data class LogInResponse (
        @SerializedName("auth_token") val token: String,
        @SerializedName("userId") val userId: Long
)