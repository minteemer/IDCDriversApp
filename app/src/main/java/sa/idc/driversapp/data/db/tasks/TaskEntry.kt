package sa.idc.driversapp.data.db.tasks

import android.database.Cursor
import com.pushtorefresh.storio3.sqlite.SQLiteTypeMapping
import com.pushtorefresh.storio3.sqlite.StorIOSQLite
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType
import com.pushtorefresh.storio3.sqlite.operations.delete.DeleteResult
import com.pushtorefresh.storio3.sqlite.operations.put.PutResult
import com.pushtorefresh.storio3.sqlite.queries.Query
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import java.lang.NullPointerException

@StorIOSQLiteType(table = TaskEntry.Table.NAME)
data class TaskEntry @StorIOSQLiteCreator constructor(
        @StorIOSQLiteColumn(key = true, name = Table.Columns.ID) val id: Long,
        @StorIOSQLiteColumn(name = Table.Columns.ORDER_ID) val orderId: Long,
        @StorIOSQLiteColumn(name = Table.Columns.STATUS) val status: String,
        @StorIOSQLiteColumn(name = Table.Columns.ROUTE_ID) val routeId: Int
) {

    companion object {
        val typeMapping: SQLiteTypeMapping<TaskEntry> by lazy {
            SQLiteTypeMapping.builder<TaskEntry>()
                    .putResolver(PutResolver())
                    .getResolver(GetResolver())
                    .deleteResolver(DeleteResolver())
                    .build()
        }
    }

    object Table {
        const val NAME: String = "tasks"

        object Columns {
            const val ID = "_id"
            const val ORDER_ID = "order_id"
            const val STATUS = "status"
            const val ROUTE_ID = "route_id"
        }

        const val ON_CREATE: String = """
            CREATE TABLE $NAME (
                ${Columns.ID} INTEGER NOT NULL PRIMARY KEY,
                ${Columns.ORDER_ID} INTEGER NOT NULL,
                ${Columns.STATUS} STRING NOT NULL,
                ${Columns.ROUTE_ID} INTEGER
            )
        """
    }

    var order: OrderEntry? = null

    constructor(task: DriverTask) : this(task.id, task.order.id, task.status.name, task.routeId) {
        order = OrderEntry(task.order)
    }

    fun toDomainEntity() = DriverTask(
            id,
            DriverTask.Status.valueOf(status),
            order?.toEntity() ?: throw NullPointerException("Order object is null"),
            routeId
    )


    class GetResolver : TaskEntryStorIOSQLiteGetResolver() {
        override fun mapFromCursor(storIOSQLite: StorIOSQLite, cursor: Cursor): TaskEntry {
            return super.mapFromCursor(storIOSQLite, cursor).apply {
                order = storIOSQLite.get()
                        .`object`(OrderEntry::class.java)
                        .withQuery(
                                Query.builder()
                                        .table(OrderEntry.Table.NAME)
                                        .where("${OrderEntry.Table.Columns.ID} = ?")
                                        .whereArgs(orderId)
                                        .build()
                        )
                        .prepare()
                        .executeAsBlocking()
            }
        }
    }

    class PutResolver : TaskEntryStorIOSQLitePutResolver() {
        override fun performPut(storIOSQLite: StorIOSQLite, task: TaskEntry): PutResult {
            task.order?.let {
                storIOSQLite.put()
                        .`object`(it)
                        .prepare()
                        .executeAsBlocking()
            }

            return super.performPut(storIOSQLite, task)
        }
    }

    class DeleteResolver : TaskEntryStorIOSQLiteDeleteResolver() {
        override fun performDelete(storIOSQLite: StorIOSQLite, task: TaskEntry): DeleteResult {
            task.order?.let {
                storIOSQLite.delete()
                        .`object`(it)
                        .prepare()
                        .executeAsBlocking()
            }

            return super.performDelete(storIOSQLite, task)
        }
    }
}