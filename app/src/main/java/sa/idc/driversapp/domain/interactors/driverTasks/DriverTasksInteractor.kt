package sa.idc.driversapp.domain.interactors.driverTasks

import io.reactivex.Single
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.repositories.driverTasks.DriverTasksRepository
import sa.idc.driversapp.repositories.driverTasks.DummyDriverTasksRepository

class DriverTasksInteractor {

    private val driverTasksRepository: DriverTasksRepository = DummyDriverTasksRepository()

    fun getTasksList(): Single<List<DriverTask>> = driverTasksRepository.getTasksList()

    fun getTaskByID(taskId: Int): Single<DriverTask> = driverTasksRepository.getTaskById(taskId)
}