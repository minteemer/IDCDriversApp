package sa.idc.driversapp.data.network.api

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import sa.idc.driversapp.data.network.entities.account.LogInRequestData
import sa.idc.driversapp.data.network.entities.ResultWrapper
import sa.idc.driversapp.data.network.entities.account.DriverLocationData
import sa.idc.driversapp.data.network.entities.account.LogInResponse
import sa.idc.driversapp.domain.entities.account.AccountData

interface AccountAPI {

    @POST("driver/login")
    fun login(@Body logInData: LogInRequestData): Single<Response<ResultWrapper<LogInResponse>>>

    @POST("driver/location")
    fun sendTrackingData(@Body location: DriverLocationData): Single<Response<ResultWrapper<String>>>

    @GET("driver")
    fun getAccountData(): Single<Response<ResultWrapper<AccountData>>>
}