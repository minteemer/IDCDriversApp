package sa.idc.driversapp.data.network.api

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import sa.idc.driversapp.data.network.account.LogInData
import sa.idc.driversapp.data.network.entities.ResultWrapper
import sa.idc.driversapp.data.network.entities.account.LogInResponse

interface AccountAPI {

    @POST("driver/login")
    fun login(@Body logInData: LogInData): Single<Response<ResultWrapper<LogInResponse>>>

}