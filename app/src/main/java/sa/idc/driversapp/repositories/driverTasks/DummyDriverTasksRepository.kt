package sa.idc.driversapp.repositories.driverTasks

import io.reactivex.Single
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import java.util.*
import java.util.concurrent.TimeUnit

class DummyDriverTasksRepository : DriverTasksRepository {

    private fun getRandomDate() =
            Date(System.currentTimeMillis() + Random().nextInt(2000000000))

    private val contacts = listOf(
            "8(964)891-66-16",
            "8(943)435-75-70",
            "8(964)113-38-86",
            "8(956)469-38-73",
            "8(903)847-29-15",
            "8(916)345-80-20",
            "8(972)201-75-90",
            "8(948)175-38-51",
            "8(956)469-38-73",
            "8(926)286-84-43",
            "8 (932) 604-21-46"
    )

    private val locations = listOf(
            "Via Reggio Emilia, 29, 00198 Roma RM, Italy" to Pair(41.911324, 12.502885),
            "Via Flaminia, 359-355, 00196 Roma RM, Italy" to Pair(41.929333, 12.468440),
            "Via degli Olimpionici, 71, 00196 Roma RM, Italy" to Pair(41.935299, 12.475761),
            "Via di Villa Emiliani, 7-3, 00197 Roma RM, Italy" to Pair(41.928231, 12.490546),
            "Via Giovanni Battista Pergolesi, 9, 00198 Roma RM, Italy" to Pair(41.916701, 12.492452),
            "Viale dei Parioli, 16-36, 00197 Roma RM, Italy" to Pair(41.924071, 12.490870)
    )

    private var i = 0
    private val tasks = locations.zip(contacts) { location, contact ->
        DriverTask(
                i++,
                getRandomDate(),
                location.first,
                contact,
                location.second.first,
                location.second.second
        )
    }.sortedBy { it.dueDate }

    override fun getTasksList(): Single<List<DriverTask>> =
            Single.just(tasks).delay(1, TimeUnit.SECONDS)

    override fun getTaskById(taskId: Int): Single<DriverTask?> =
            Single.just(tasks.firstOrNull { it.taskId == taskId })

}