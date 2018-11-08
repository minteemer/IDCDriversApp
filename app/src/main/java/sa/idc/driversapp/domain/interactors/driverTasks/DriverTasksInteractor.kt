package sa.idc.driversapp.domain.interactors.driverTasks

import android.util.Log
import com.google.maps.model.DirectionsRoute
import io.reactivex.Single
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.repositories.driverTasks.DriverTasksRepositoryImpl
import sa.idc.driversapp.repositories.googleMaps.GoogleMapsRepository
import sa.idc.driversapp.repositories.preferences.AppPreferences

class DriverTasksInteractor {

    private val driverTasksRepository: DriverTasksRepository = DriverTasksRepositoryImpl()

    private val googleMapsRepository: GoogleMapsRepository by lazy { GoogleMapsRepository() }


    fun refreshTasks(): Single<List<DriverTask>> = driverTasksRepository.refreshTasks()

    fun getCachedTasks(): Single<List<DriverTask>> = driverTasksRepository.getCachedTasks()

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

    fun getRoute(task: DriverTask): Single<DirectionsRoute?> = googleMapsRepository
            .getPath(task.order.origin, task.order.destination)
            .map { result ->
                Log.d("TasksInteractor", "route: ${result.routes.getOrNull(task.routeId)}")
                result.routes.getOrElse(task.routeId) {
                    Log.e("TasksInteractor", "Could not find path with id ${task.routeId}, returning default path")
                    result.routes.getOrNull(0)
                }
            }

}