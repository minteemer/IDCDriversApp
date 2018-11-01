package sa.idc.driversapp.repositories.support

import android.util.Log
import com.pushtorefresh.storio3.sqlite.queries.Query
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.HttpException
import sa.idc.driversapp.IDCDriversApp
import sa.idc.driversapp.R
import sa.idc.driversapp.data.db.DBHelper
import sa.idc.driversapp.data.db.support.SupportChatMessageEntry
import sa.idc.driversapp.data.network.ApiConstructor
import sa.idc.driversapp.data.network.api.SupportAPI
import sa.idc.driversapp.domain.entities.support.SupportChatMessage
import sa.idc.driversapp.domain.interactors.support.SupportRepository
import sa.idc.driversapp.repositories.preferences.AppPreferences

class SupportRepositoryImpl : SupportRepository {

    private val db = DBHelper.defaultStorIOBuilder.build()

    private val supportApi: SupportAPI = ApiConstructor.supportApi

    override fun saveReceivedMessage(message: SupportChatMessage): Completable = db.put()
            .`object`(SupportChatMessageEntry(message))
            .prepare()
            .asRxCompletable()

    override fun getSavedMessages(): Single<List<SupportChatMessage>> =
            supportApi.getChatMessages().map { response ->
                response.takeIf { it.isSuccessful }?.body()?.result
                        ?.map { it.toDomainEntity() }
                        ?.reversed()
                        ?: throw HttpException(response)
            }.onErrorResumeNext { e ->
                Log.e("SupportRepositoryImpl", "Error while getting messages", e)
                db.get()
                        .listOfObjects(SupportChatMessageEntry::class.java)
                        .withQuery(
                                Query.builder()
                                        .table(SupportChatMessageEntry.Table.NAME)
                                        .build()
                        )
                        .prepare()
                        .asRxSingle()
                        .map { result -> result.map { it.toDomainEntity() } }
            }

    override fun sendMessage(text: String): Single<SupportChatMessage> =
            supportApi.sendChatMessage(text).map {
                if (it.isSuccessful)
                    SupportChatMessage(
                            AppPreferences.instance.driverName,
                            text,
                            System.currentTimeMillis()
                    )
                else
                    throw HttpException(it)
            }


    override fun getSupportOperatorNumber(): Single<String> =
            Single.just(IDCDriversApp.instance.getString(R.string.default_support_operator_number))
}