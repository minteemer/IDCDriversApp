package sa.idc.driversapp.domain.interactors.driverTasks

import io.reactivex.Single
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.repositories.driverTasks.DummyDriverTasksRepository
import sa.idc.driversapp.repositories.preferences.AppPreferences

class DriverTasksInteractor {

    private val driverTasksRepository: DriverTasksRepository = DummyDriverTasksRepository()

    fun getTasksList(): Single<List<DriverTask>> = driverTasksRepository.getTasksList()

    fun getTaskByID(taskId: Int): Single<DriverTask?> = driverTasksRepository.getTaskById(taskId)

    private val preferences = AppPreferences.instance


    enum class AcceptanceResult {
        Success, ConnectionError
    }

    fun acceptTaskById(taskId: Int) = driverTasksRepository.acceptTaskById(taskId).map { accepted ->
        if (accepted == AcceptanceResult.Success) {
            preferences.acceptedTaskId = taskId
        }
        accepted
    }

    enum class FinishiingResult {
        Success, ConnectionError
    }

    fun finishTaskById(taskID: Int) = driverTasksRepository.finishTaskById(taskID).map { finished ->
        if (finished == FinishiingResult.Success) {
            preferences.acceptedTaskId = AppPreferences.DefaultValues.ID_OF_ACCEPTED_TASK
        }
        finished
    }

}