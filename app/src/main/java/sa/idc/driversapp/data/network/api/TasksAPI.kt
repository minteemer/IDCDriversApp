package sa.idc.driversapp.data.network.api

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import sa.idc.driversapp.data.network.entities.ResultWrapper
import sa.idc.driversapp.data.network.entities.tasks.ServerTask

interface TasksAPI {
    companion object {
        const val BASE_PATH = "driver/tasks"
    }

    @GET(BASE_PATH)
    fun getTasksList(): Single<ResultWrapper<List<ServerTask>>>

    @POST("$BASE_PATH/{task_id}/activate")
    fun activateTask(@Path("task_id") taskId: Long): Single<Response<ResultWrapper<String>>>

    @POST("$BASE_PATH/{task_id}/complete")
    fun completeTask(@Path("task_id") taskId: Long): Single<Response<ResultWrapper<String>>>

}