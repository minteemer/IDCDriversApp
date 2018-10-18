package sa.idc.driversapp.data.network.api

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import sa.idc.driversapp.data.network.entities.ResultWrapper
import sa.idc.driversapp.data.network.entities.account.LogInResponse

interface AccountAPI {

    @FormUrlEncoded
    @POST("drivers/login")
    fun login(
            @Field("email") login: String,
            @Field("password") password: String
    ): Single<Response<ResultWrapper<LogInResponse>>>

}