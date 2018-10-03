package sa.idc.driversapp.repositories.driverTasks

import io.reactivex.Single
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import java.util.*
import java.util.concurrent.TimeUnit

class DummyDriverTasksRepository : DriverTasksRepository {

    private fun getRandomDate() =
            Date(System.currentTimeMillis() + Random().nextInt(2000000000))

    private val tasks = listOf(
            "Ул. Пушкина, Дом Колотушкина" to "+7777777777",
            "Университетская 1" to "+7777777777",
            "пвыпывап" to "+7777777777",
            "ФЫфвыфв фыв пвыуддльт" to "+7777777777"
    ).asSequence()
            .mapIndexed { i, (address, contacts) ->
                DriverTask(i, getRandomDate(), address, contacts)
            }
            .sortedBy { it.dueDate }
            .toList()

    override fun getTasksList(): Single<List<DriverTask>> =
            Single.just(tasks).delay(1, TimeUnit.SECONDS)

    override fun getTaskById(taskId: Int): Single<DriverTask?> =
            Single.just(tasks.firstOrNull { it.taskId == taskId })

}