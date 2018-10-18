package sa.idc.driversapp.domain.entities.driverTasks

import android.location.Location
import java.util.*

data class Order(
        val id: Int,
        val dueDate: Date,
        val origin: Location,
        val destination: Location,
        val status: Status,
        val weight: Double,
        val worth: Long,
        val description: String,
        val customerContacts: String
) {
    enum class Status {
        PendingConfirmation, Rejected, InProgress, Delivered
    }
}