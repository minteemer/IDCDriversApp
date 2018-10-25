package sa.idc.driversapp.data.network.entities

import com.google.gson.annotations.SerializedName
import java.lang.Exception

class ResultWrapper<T>(
        @SerializedName("error") val error: String?,
        @SerializedName("result") val result: T?
) {

    class ResultError(error: String?): Exception(error)

    fun resultError() = ResultError(error)
}