package sa.idc.driversapp.domain.entities.driverTasks

import java.util.*

data class DriverTask(
        val taskId: Int,
        val dueDate: Date,
        val address: String,
        val contact: String,
        val destinationLat: Double,
        val destinationLng: Double
)