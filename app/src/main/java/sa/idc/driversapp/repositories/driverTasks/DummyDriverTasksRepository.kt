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


    private val random = Random(123)

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

    private val descriptions = listOf(
            "Teddy bear",
            "Giant red spinner",
            "IPhone 11",
            "Elbrus CPU",
            "Xiaomi powerbank"
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

    private var i = 2234
    private val tasks = descriptions.asSequence()
            .map { description ->
                val originId = random.nextInt(locations.size)
                val destinationId = (originId + 1) % locations.size
                DriverTask(
                        i++,
                        DriverTask.Status.Pending,
                        Order(
                                1000 + i++,
                                getRandomDate(),
                                locations[originId].let { getLocation(it.first, it.second) },
                                locations[destinationId].let { getLocation(it.first, it.second) },
                                Order.Status.PendingConfirmation,
                                10.5,
                                100_00,
                                description,
                                contacts.random()
                        )
                )
            }
            .plus(
                    DriverTask(
                            i++,
                            DriverTask.Status.Pending,
                            Order(
                                    3012,
                                    Date(System.currentTimeMillis() + 1000),
                                    getLocation(55.753380, 48.741618),
                                    getLocation(55.746784, 48.743807),
                                    Order.Status.PendingConfirmation,
                                    10.5,
                                    100_00,
                                    "Box of cookies",
                                    contacts.random()
                            )
                    )
            )
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