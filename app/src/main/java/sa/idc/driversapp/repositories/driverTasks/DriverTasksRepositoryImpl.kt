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

    private val tasksApi = ApiConstructor.tasksApi

    private val db = DBHelper.defaultStorIOBuilder.build()

    override fun refreshTasks(): Single<List<DriverTask>> =
            tasksApi.getTasksList().map { response ->
                response.result?.map { it.toDomainEntity() } ?: throw response.resultError()
            }.flatMap { tasks ->
                getCachedTasks()
                        // Deleted removed on server tasks
                        .flatMapCompletable { oldTasks ->
                            db.delete()
                                    .objects(
                                            oldTasks.filter { oldTask ->
                                                tasks.none { it.id == oldTask.id }
                                            }.map { TaskEntry(it) }
                                    )
                                    .prepare()
                                    .asRxCompletable()
                        }
                        // Save new tasks
                        .andThen(
                                db.put()
                                        .objects(tasks.map { TaskEntry(it) })
                                        .prepare()
                                        .asRxCompletable()
                        )
                        // Return received tasks
                        .toSingleDefault(tasks)
            }

    override fun getCachedTasks(): Single<List<DriverTask>> = db.get()
            .listOfObjects(TaskEntry::class.java)
            .withQuery(
                    Query.builder()
                            .table(TaskEntry.Table.NAME)
                            .build()
            )
            .prepare()
            .asRxSingle()
            .map { result -> result.map { it.toDomainEntity() } }

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
            tasksApi.activateTask(taskId).map {
                if (it.isSuccessful)
                    DriverTasksInteractor.AcceptanceResult.Success
                else
                    DriverTasksInteractor.AcceptanceResult.ConnectionError
            }

    override fun finishTaskById(taskId: Long): Single<DriverTasksInteractor.FinishingResult> =
            tasksApi.completeTask(taskId).map {
                if (it.isSuccessful)
                    DriverTasksInteractor.FinishingResult.Success
                else
                    DriverTasksInteractor.FinishingResult.ConnectionError
            }


}