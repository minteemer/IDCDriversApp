package sa.idc.driversapp.data.network.entities.tasks

import com.google.gson.annotations.SerializedName

data class ServerOrder(
        @SerializedName("id") val id: Long,
        @SerializedName("status") val status: String,
        @SerializedName("origin") val origin: ServerLocation,
        @SerializedName("due_date") val dueDate: String,
        @SerializedName("destination") val destination: ServerLocation,
        @SerializedName("customer") val customer: Customer,
        @SerializedName("description") val description: Double,
        @SerializedName("weight") val weight: Double,
        @SerializedName("worth") val worth: Long
) {

    data class ServerLocation(
            @SerializedName("latitude", alternate = ["origin_latitude", "destination_latitude"])
            val latitude: Double,
            @SerializedName("longitude", alternate = ["origin_longitude", "destination_longitude"])
            val longitude: Double
    )

    data class Customer(
            @SerializedName("phone_number") val phoneNumber: String
    )
}