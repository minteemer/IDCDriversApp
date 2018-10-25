package sa.idc.driversapp.data.network

import com.facebook.stetho.okhttp3.StethoInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import sa.idc.driversapp.IDCDriversApp
import sa.idc.driversapp.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import sa.idc.driversapp.data.network.api.AccountAPI
import sa.idc.driversapp.data.network.api.TasksAPI
import sa.idc.driversapp.repositories.preferences.AppPreferences
import java.util.concurrent.TimeUnit


object ApiConstructor {

    val tasksApi: TasksAPI by lazy { defaultRetrofit.create(TasksAPI::class.java) }

    val accountApi: AccountAPI by lazy { defaultRetrofit.create(AccountAPI::class.java) }


    private val defaultRetrofit: Retrofit by lazy {
        defaultRetrofitBuilder
                .client(defaultOkHttpBuilder.build())
                .build()
    }

    private val defaultRetrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
                .baseUrl(IDCDriversApp.instance.getString(R.string.idc_server_base_url))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())

    }

    private val defaultOkHttpBuilder: OkHttpClient.Builder by lazy {
        OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(StethoInterceptor())
                .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                )
                .addInterceptor { chain ->
                    chain.request().newBuilder()
                            .addHeader("Authorization", AppPreferences.instance.token)
                            .build()
                            .let { chain.proceed(it) }
                }
    }

}