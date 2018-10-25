package sa.idc.driversapp.data.network.entities.tasks

import com.google.gson.annotations.SerializedName
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask

data class ServerTask(
        @SerializedName("id") val id: Long,
        @SerializedName("order") val order: ServerOrder,
        @SerializedName("status") val status: String
) {

    fun toDomainEntity(): DriverTask =
            DriverTask(
                    id,
                    when (status) {
                        "COMPLETE" -> DriverTask.Status.Complete
                        "IN_PROGRESS" -> DriverTask.Status.InProgress
                        "PENDING" -> DriverTask.Status.Pending
                        else -> throw IllegalArgumentException("Unknown task status")
                    },
                    order.toDomainEntity()
            )
}