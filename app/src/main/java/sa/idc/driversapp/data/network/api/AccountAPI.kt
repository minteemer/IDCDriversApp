package sa.idc.driversapp.data.network.api

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import sa.idc.driversapp.data.network.entities.account.LogInRequestData
import sa.idc.driversapp.data.network.entities.ResultWrapper
import sa.idc.driversapp.data.network.entities.account.LogInResponse

interface AccountAPI {

    @POST("driver/login")
    fun login(@Body logInData: LogInRequestData): Single<Response<ResultWrapper<LogInResponse>>>

}