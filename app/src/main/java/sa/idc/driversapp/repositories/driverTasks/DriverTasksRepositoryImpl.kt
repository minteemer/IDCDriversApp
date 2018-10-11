package sa.idc.driversapp.repositories.driverTasks

import com.pushtorefresh.storio3.sqlite.queries.Query
import io.reactivex.Single
import sa.idc.driversapp.data.db.DBHelper
import sa.idc.driversapp.data.db.tasks.TaskEntry
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.domain.interactors.driverTasks.DriverTasksRepository

class DriverTasksRepositoryImpl : DriverTasksRepository {

    private val dummyRepo = DummyDriverTasksRepository()

    private val db = DBHelper.defaultStorIOBuilder.build()

    override fun getTasksList(): Single<List<DriverTask>> =
            dummyRepo.getTasksList()
                    .flatMap { tasks ->
                        db.put()
                                .objects(tasks.map { TaskEntry(it) })
                                .prepare()
                                .asRxCompletable()
                                .toSingleDefault(tasks)
                    }

    override fun getTaskById(taskId: Int): Single<DriverTask?> =
            dummyRepo.getTaskById(taskId)
                    .onErrorResumeNext { _ ->
                        db.get()
                                .`object`(TaskEntry::class.java)
                                .withQuery(
                                        Query.builder()
                                                .table(TaskEntry.Table.NAME)
                                                .where("id = ${TaskEntry.Table.Columns.ID}")
                                                .whereArgs(taskId)
                                                .build()
                                )
                                .prepare()
                                .asRxSingle()
                                .map { it.orNull()?.toEntity() }
                    }
}