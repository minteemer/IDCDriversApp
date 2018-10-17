package sa.idc.driversapp.domain.interactors.driverTasks

import io.reactivex.Single
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask

interface DriverTasksRepository {
    /** @return list of all available tasks for current driver */
    fun getTasksList(): Single<List<DriverTask>>

    /** @return task with given [taskId], null if such task does not exist */
    fun getTaskById(taskId: Int): Single<DriverTask?>

    /**@return status of accepting the task using [taskId] */
    fun acceptTaskById(taskId:Int): Single<DriverTasksInteractor.AcceptanceResult>

    /**@return status of finishing the task using [taskID]*/
    fun finishTaskById(taskId: Int):Single<DriverTasksInteractor.FinishiingResult>
}