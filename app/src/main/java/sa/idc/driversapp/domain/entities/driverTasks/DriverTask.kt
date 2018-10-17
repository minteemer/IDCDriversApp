package sa.idc.driversapp.domain.entities.driverTasks

data class DriverTask(
        val id: Int,
        val status: Status,
        val order: Order
) {
    enum class Status {
        Pending, InProgress, Complete
    }
}