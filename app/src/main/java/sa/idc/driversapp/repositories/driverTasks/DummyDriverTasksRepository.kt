package sa.idc.driversapp.repositories.driverTasks

import io.reactivex.Single
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import java.util.*
import java.util.concurrent.TimeUnit

class DummyDriverTasksRepository : DriverTasksRepository {

    private fun getRandomDate() =
            Date(System.currentTimeMillis() + Random().nextInt(2000000000))

    private val tasks = listOf(
            "Grant Avenue 28" to "8(964)891-66-16",
            "Folsom Street. 1" to "8(943)435-75-70",
            "Grant Avenue 11" to "8(964)113-38-86",
            "Hyde Street 42" to "8(956)469-38-73",
            "Market Street" to "8(903)847-29-15",
            "Park Presidio Boulevard 21" to "8(916)345-80-20",
            "Portola Drive 55" to "8(972)201-75-90",
            "Kearny Street 23" to "8(948)175-38-51",
            "Polk Street 73" to "8(956)469-38-73",
            "Stockton Street 81" to "8(926)286-84-43",
            "Third Street 12" to "8(956)469-38-73"
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