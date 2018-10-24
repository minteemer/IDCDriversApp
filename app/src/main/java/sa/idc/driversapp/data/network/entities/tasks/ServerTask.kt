package sa.idc.driversapp.data.network.entities.tasks

import com.google.gson.annotations.SerializedName

data class ServerTask(
        @SerializedName("id") val id: Long,
        @SerializedName("order") val order: ServerOrder,
        @SerializedName("status") val status: String
)