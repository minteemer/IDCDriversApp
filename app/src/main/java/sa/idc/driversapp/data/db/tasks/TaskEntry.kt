package sa.idc.driversapp.data.db.tasks

import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.repositories.driverTasks.DummyDriverTasksRepository

@StorIOSQLiteType(table = TaskEntry.Table.NAME)
data class TaskEntry @StorIOSQLiteCreator constructor(
        @StorIOSQLiteColumn(key = true, name = Table.Columns.ID) val id: Int,
        @StorIOSQLiteColumn(name = Table.Columns.STATUS) val orderId: Int,
        @StorIOSQLiteColumn(name = Table.Columns.ORDER_ID) val status: String
) {
    object Table {
        const val NAME: String = "tasks"

        object Columns {
            const val ID = "_id"
            const val ORDER_ID = "order_id"
            const val STATUS = "status"
        }

        const val ON_CREATE: String = """
            CREATE TABLE $NAME (
                ${Columns.ID} INTEGER NOT NULL PRIMARY KEY,
                ${Columns.ORDER_ID} INTEGER NOT NULL,
                ${Columns.STATUS} STRING NOT NULL
            )
        """
    }

    constructor(task: DriverTask) : this(task.id, task.order.id, task.status.toString())

    fun toEntity() = DriverTask(
            id,
            DriverTask.Status.valueOf(status),
            DummyDriverTasksRepository().getOrderById(orderId)!!
    )
}