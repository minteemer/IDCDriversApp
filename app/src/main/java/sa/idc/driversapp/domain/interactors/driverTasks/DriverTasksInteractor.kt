package sa.idc.driversapp.domain.interactors.driverTasks

import io.reactivex.Single
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.repositories.driverTasks.DriverTasksRepositoryImpl
import sa.idc.driversapp.repositories.preferences.AppPreferences

class DriverTasksInteractor {

    private val driverTasksRepository: DriverTasksRepository = DriverTasksRepositoryImpl()

    fun refreshTasks(): Single<List<DriverTask>> = driverTasksRepository.refreshTasks()

    fun getTaskByID(taskId: Long): Single<DriverTask?> = driverTasksRepository.getTaskById(taskId)

    private val preferences = AppPreferences.instance


    enum class AcceptanceResult {
        Success, ConnectionError
    }

    fun acceptTaskById(taskId: Long): Single<AcceptanceResult> =
            driverTasksRepository.acceptTaskById(taskId).map { accepted ->
                if (accepted == AcceptanceResult.Success) {
                    preferences.acceptedTaskId = taskId
                }
                accepted
            }

    enum class FinishingResult {
        Success, ConnectionError
    }

    fun finishTaskById(taskID: Long): Single<FinishingResult> = driverTasksRepository.finishTaskById(taskID).map { finished ->
        if (finished == FinishingResult.Success) {
            preferences.acceptedTaskId = AppPreferences.Default.ID_OF_ACCEPTED_TASK
        }
        finished
    }

}