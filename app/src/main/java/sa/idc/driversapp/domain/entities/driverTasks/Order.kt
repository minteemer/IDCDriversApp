package sa.idc.driversapp.domain.entities.driverTasks

import android.location.Location
import java.util.*

data class Order(
        val id: Long,
        val dueDate: Date,
        val origin: Location,
        val destination: Location,
        val status: Status,
        val weight: Double,
        val worth: Long,
        val description: String,
        val customerPhoneNumber: String,
        val customerEmail: String,
        val customerName: String,
        val destinationAddress: String,
        val originAddress: String
) {
    enum class Status {
        PendingConfirmation, Rejected, InProgress, Delivered
    }
}