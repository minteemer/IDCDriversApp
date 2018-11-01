package sa.idc.driversapp.domain.entities.account

import com.google.gson.annotations.SerializedName

data class AccountData(
        @SerializedName("id") val id: Long,
        @SerializedName("email") val email: String,
        @SerializedName("name") val name: String
)