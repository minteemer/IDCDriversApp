package sa.idc.driversapp.repositories.support

import com.pushtorefresh.storio3.sqlite.queries.Query
import io.reactivex.Completable
import io.reactivex.Single
import sa.idc.driversapp.IDCDriversApp
import sa.idc.driversapp.R
import sa.idc.driversapp.data.db.DBHelper
import sa.idc.driversapp.data.db.support.SupportChatMessageEntry
import sa.idc.driversapp.domain.entities.support.SupportChatMessage
import sa.idc.driversapp.domain.interactors.support.SupportRepository
import java.util.concurrent.TimeUnit

class SupportRepositoryImpl : SupportRepository {

    private val db = DBHelper.defaultStorIOBuilder.build()

    override fun saveReceivedMessage(message: SupportChatMessage): Completable = db.put()
            .`object`(SupportChatMessageEntry(message))
            .prepare()
            .asRxCompletable()

    override fun getSavedMessages(): Single<List<SupportChatMessage>> = db.get()
            .listOfObjects(SupportChatMessageEntry::class.java)
            .withQuery(
                    Query.builder()
                            .table(SupportChatMessageEntry.Table.NAME)
                            .build()
            )
            .prepare()
            .asRxSingle()
            .map { result -> result.map { it.toDomainEntity() } }
            .flatMap { messages ->
                if (messages.isEmpty()) {
                    listOf(
                            SupportChatMessage("Eugene", "Hello", 123),
                            SupportChatMessage("Operator 1", "Hello!", 123),
                            SupportChatMessage("Eugene", "It was nice to talk to you. See you later!", 123)
                    ).forEach { saveReceivedMessage(it).blockingGet() }
                    getSavedMessages()
                } else {
                    Single.just(messages)
                }
            }

    // TODO: implement sending to server
    override fun sendMessage(message: String): Single<SupportChatMessage> =
            Single.just(SupportChatMessage("TEst Name", message, 123))
                    .delay(1, TimeUnit.SECONDS)


    override fun getSupportOperatorNumber(): Single<String> =
            Single.just(IDCDriversApp.instance.getString(R.string.default_support_operator_number))
}