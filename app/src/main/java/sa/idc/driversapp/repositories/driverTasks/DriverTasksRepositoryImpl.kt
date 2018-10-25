package sa.idc.driversapp.repositories.driverTasks

import com.pushtorefresh.storio3.sqlite.queries.Query
import io.reactivex.Single
import sa.idc.driversapp.data.db.DBHelper
import sa.idc.driversapp.data.db.tasks.TaskEntry
import sa.idc.driversapp.data.network.ApiConstructor
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.domain.interactors.driverTasks.DriverTasksInteractor
import sa.idc.driversapp.domain.interactors.driverTasks.DriverTasksRepository

class DriverTasksRepositoryImpl : DriverTasksRepository {

    private val dummyRepo = DummyDriverTasksRepository()

    private val tasksApi = ApiConstructor.tasksApi

    private val db = DBHelper.defaultStorIOBuilder.build()

    override fun getTasksList(): Single<List<DriverTask>> =
            tasksApi.getTasksList().map { response ->
                response.result?.map { it.toDomainEntity() } ?: throw response.resultError()
            }.flatMap { tasks ->
                db.put()
                        .objects(tasks.map { TaskEntry(it) })
                        .prepare()
                        .asRxCompletable()
                        .toSingleDefault(tasks)
            }

    fun getLocalTasksList() = db.get()
            .listOfObjects(TaskEntry::class.java)
            .withQuery(
                    Query.builder()
                            .table(TaskEntry.Table.NAME)
                            .build()
            )
            .prepare()
            .asRxSingle()

    override fun getTaskById(taskId: Long): Single<DriverTask?> =
            db.get()
                    .`object`(TaskEntry::class.java)
                    .withQuery(
                            Query.builder()
                                    .table(TaskEntry.Table.NAME)
                                    .where("${TaskEntry.Table.Columns.ID} = ?")
                                    .whereArgs(taskId)
                                    .build()
                    )
                    .prepare()
                    .asRxSingle()
                    .map { it.orNull()?.toDomainEntity() }


    override fun acceptTaskById(taskId: Long): Single<DriverTasksInteractor.AcceptanceResult> =
            dummyRepo.acceptTaskById(taskId)

    override fun finishTaskById(taskId: Long): Single<DriverTasksInteractor.FinishiingResult> =
            dummyRepo.finishTaskById(taskId)

}