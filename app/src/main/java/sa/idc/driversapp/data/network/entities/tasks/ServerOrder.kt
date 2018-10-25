package sa.idc.driversapp.data.network.entities.tasks

import android.location.Location
import com.google.gson.annotations.SerializedName
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.domain.entities.driverTasks.Order
import java.util.*

data class ServerOrder(
        @SerializedName("id") val id: Long,
        @SerializedName("status") val status: String,
        @SerializedName("origin") val origin: ServerLocation,
        @SerializedName("due_date") val dueDate: Long,
        @SerializedName("destination") val destination: ServerLocation,
        @SerializedName("customer") val customer: Customer,
        @SerializedName("description") val description: String,
        @SerializedName("weight") val weight: Double,
        @SerializedName("worth") val worth: Long,
        @SerializedName("creation_date") val creation_date: Long
) {
    data class ServerLocation(
            @SerializedName("latitude", alternate = ["origin_latitude", "destination_latitude"])
            val latitude: Double,
            @SerializedName("longitude", alternate = ["origin_longitude", "destination_longitude"])
            val longitude: Double,
            @SerializedName("full_address", alternate = ["origin_full_address", "destination_full_address"])
            val full_address: String,
            @SerializedName("short_address", alternate = ["origin_short_address", "destination_short_address"])
            val short_address: String
    ) {
        fun toLocation() = Location("").apply {
            latitude = latitude
            longitude = longitude
        }
    }

    data class Customer(
            @SerializedName("phone_number") val phoneNumber: String?
    )

    fun toDomainEntity(): Order = Order(
            id, Date(dueDate * 1000),
            origin.toLocation(), destination.toLocation(),
            when (status) {
                "DELIVERED" -> Order.Status.Delivered
                "IN_PROGRESS" -> Order.Status.InProgress
                "PENDING_CONFIRMATION" -> Order.Status.PendingConfirmation
                "REJECTED" -> Order.Status.Rejected
                else -> throw IllegalArgumentException("Unknown order status")
            },
            weight, worth, description, customer.phoneNumber ?: "Unknown"
    )


}