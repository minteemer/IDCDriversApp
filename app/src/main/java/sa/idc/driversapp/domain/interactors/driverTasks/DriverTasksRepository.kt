package sa.idc.driversapp.domain.interactors.driverTasks

import io.reactivex.Single
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask

interface DriverTasksRepository {
    /** @return list of all available tasks for current driver */
    fun refreshTasks(): Single<List<DriverTask>>

    /** @return task with given [taskId], null if such task does not exist */
    fun getTaskById(taskId: Long): Single<DriverTask?>

    /**@return status of accepting the task using [taskId] */
    fun acceptTaskById(taskId:Long): Single<DriverTasksInteractor.AcceptanceResult>

    /**@return status of finishing the task using [taskID]*/
    fun finishTaskById(taskId: Long): Single<DriverTasksInteractor.FinishiingResult>
}