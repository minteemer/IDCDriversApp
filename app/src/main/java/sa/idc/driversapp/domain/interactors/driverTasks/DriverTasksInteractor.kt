package sa.idc.driversapp.domain.interactors.driverTasks

import io.reactivex.Single
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.repositories.driverTasks.DummyDriverTasksRepository
import sa.idc.driversapp.repositories.preferences.AppPreferences

class DriverTasksInteractor {

    private val driverTasksRepository: DriverTasksRepository = DummyDriverTasksRepository()

    fun getTasksList(): Single<List<DriverTask>> = driverTasksRepository.getTasksList()

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

    enum class FinishiingResult {
        Success, ConnectionError
    }

    fun finishTaskById(taskID: Long) = driverTasksRepository.finishTaskById(taskID).map { finished ->
        if (finished == FinishiingResult.Success) {
            preferences.acceptedTaskId = AppPreferences.Default.ID_OF_ACCEPTED_TASK
        }
        finished
    }

}