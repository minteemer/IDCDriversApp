package sa.idc.driversapp.data.network.entities.support

import com.google.gson.annotations.SerializedName
import sa.idc.driversapp.domain.entities.support.SupportChatMessage

data class ServerSupportChatMessage(
        @SerializedName("driver") val driver: Driver,
        @SerializedName("operator") val operator: Operator,
        @SerializedName("posted_date") val timestamp: Long,
        @SerializedName("text") val text: String,
        @SerializedName("is_driver_initiator") val isSentByDriver: Boolean
) {
    data class Driver(
            @SerializedName("id") val id: Long,
            @SerializedName("name") val name: String
    )

    data class Operator(
            @SerializedName("id") val id: Long,
            @SerializedName("name") val name: String
    )

    fun toDomainEntity() = SupportChatMessage(
            if (isSentByDriver) driver.name else operator.name,
            text,
            timestamp
    )
}
