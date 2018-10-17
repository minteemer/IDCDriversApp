package sa.idc.driversapp.repositories.driverTasks

import android.location.Location
import io.reactivex.Single
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.domain.entities.driverTasks.Order
import sa.idc.driversapp.domain.interactors.driverTasks.DriverTasksInteractor
import sa.idc.driversapp.domain.interactors.driverTasks.DriverTasksRepository
import java.util.*
import java.util.concurrent.TimeUnit

class DummyDriverTasksRepository : DriverTasksRepository {


    private val random = Random()

    private fun getRandomDate() =
            Date(System.currentTimeMillis() + random.nextInt(2000000000))

    private fun getLocation(lat: Double, lng: Double) = Location("").apply {
        latitude = lat
        longitude = lng
    }

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

    private val addresses = listOf(
            "Via Reggio Emilia, 29, 00198 Roma RM, Italy",
            "Via Flaminia, 359-355, 00196 Roma RM, Italy",
            "Via degli Olimpionici, 71, 00196 Roma RM, Italy",
            "Via di Villa Emiliani, 7-3, 00197 Roma RM, Italy",
            "Via Giovanni Battista Pergolesi, 9, 00198 Roma RM, Italy",
            "Viale dei Parioli, 16-36, 00197 Roma RM, Italy"
    )

    private val locations = listOf(
            Pair(41.911324, 12.502885),
            Pair(41.929333, 12.468440),
            Pair(41.935299, 12.475761),
            Pair(41.928231, 12.490546),
            Pair(41.916701, 12.492452),
            Pair(41.924071, 12.490870)
    )

    private fun <T> List<T>.random() = get(random.nextInt(size))

    private var i = 0
    private val tasks = addresses.asSequence()
            .map { address ->
                DriverTask(
                        i++,
                        DriverTask.Status.Pending,
                        Order(
                                1000 + i++,
                                getRandomDate(),
                                locations.random().let { getLocation(it.first, it.second) },
                                locations.random().let { getLocation(it.first, it.second) },
                                address,
                                Order.Status.PendingConfirmation,
                                10.5,
                                100_00,
                                "Test order description",
                                contacts.random()
                        )
                )
            }
            .sortedBy { it.order.dueDate }
            .toList()

    override fun acceptTaskById(taskId: Int): Single<DriverTasksInteractor.AcceptanceResult> =
            Single.just(DriverTasksInteractor.AcceptanceResult.Success).delay(1, TimeUnit.SECONDS)

    override fun finishTaskById(taskId: Int): Single<DriverTasksInteractor.FinishiingResult> =
            Single.just(DriverTasksInteractor.FinishiingResult.Success).delay(1, TimeUnit.SECONDS)

    override fun getTasksList(): Single<List<DriverTask>> =
            Single.just(tasks).delay(1, TimeUnit.SECONDS)

    override fun getTaskById(taskId: Int): Single<DriverTask?> =
            Single.just(tasks.firstOrNull { it.id == taskId })

    fun getOrderById(orderId: Int): Order? =
            tasks.firstOrNull { it.order.id == orderId }?.order
}