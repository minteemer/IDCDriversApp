package sa.idc.driversapp.data.network.entities.account

import android.location.Location
import com.google.gson.annotations.SerializedName

data class DriverLocationData(
        @SerializedName("latitude") val latitude: Double,
        @SerializedName("longitude") val longitude: Double
) {
    constructor(location: Location) : this(location.latitude, location.longitude)
}