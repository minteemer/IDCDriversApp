package sa.idc.driversapp.data.network.entities

import com.google.gson.annotations.SerializedName

class ResultWrapper<T>(
        @SerializedName("error") val error: String?,
        @SerializedName("result") val result: T?
)