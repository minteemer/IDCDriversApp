package sa.idc.driversapp.repositories.driverTasks

import io.reactivex.Single
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import java.util.*
import java.util.concurrent.TimeUnit

class DummyDriverTasksRepository : DriverTasksRepository {

    private fun getRandomDate() =
            Date(System.currentTimeMillis() + Random().nextInt(2000000000))

    private val tasks = listOf(
            DriverTask(0, getRandomDate(), "Ул. Пушкина, Дом Колотушкина"),
            DriverTask(1, getRandomDate(), "Университетская 1"),
            DriverTask(2, getRandomDate(), "пвыпывап"),
            DriverTask(3, getRandomDate(), "ФЫфвыфв фыв пвыуддльт")
    )


    override fun getTasksList(): Single<List<DriverTask>> =
            Single.just(tasks).delay(1, TimeUnit.SECONDS)

    override fun getTaskById(taskId: Int): Single<DriverTask?> =
            Single.just(tasks.getOrNull(taskId))

}