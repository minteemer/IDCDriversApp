package sa.idc.driversapp.domain.entities.driverTasks

data class DriverTask(
        val id: Long,
        val status: Status,
        val order: Order,
        val routeId: Int
) {
    enum class Status {
        Pending, InProgress, Complete
    }
}