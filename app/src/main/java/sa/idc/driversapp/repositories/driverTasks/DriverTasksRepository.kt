package sa.idc.driversapp.repositories.driverTasks

import io.reactivex.Single
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask

interface DriverTasksRepository {
    /** @return list of all available tasks for current driver */
    fun getTasksList(): Single<List<DriverTask>>

    /** @return task with given [taskId], null if such task does not exist */
    fun getTaskById(taskId: Int): Single<DriverTask?>
}