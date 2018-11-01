package sa.idc.driversapp.data.network.api

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import sa.idc.driversapp.data.network.entities.ResultWrapper
import sa.idc.driversapp.data.network.entities.support.ServerSupportChatMessage

interface SupportAPI {

    @POST("driver/messages")
    fun sendChatMessage(@Query("text") message: String): Single<Response<ResultWrapper<String>>>

    @GET("driver/messages")
    fun getChatMessages(): Single<Response<ResultWrapper<List<ServerSupportChatMessage>>>>
}