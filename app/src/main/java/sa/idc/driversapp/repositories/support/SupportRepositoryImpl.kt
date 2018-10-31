package sa.idc.driversapp.repositories.support

import io.reactivex.Completable
import io.reactivex.Single
import sa.idc.driversapp.IDCDriversApp
import sa.idc.driversapp.R
import sa.idc.driversapp.domain.entities.support.SupportChatMessage
import sa.idc.driversapp.domain.interactors.support.SupportRepository
import java.util.concurrent.TimeUnit

class SupportRepositoryImpl : SupportRepository {
    override fun saveReceivedMessage(message: SupportChatMessage): Completable =
            Completable.complete()

    override fun sendMessage(message: String): Single<SupportChatMessage> =
            Single.just(SupportChatMessage("TEst Name", message, 123))
                    .delay(1, TimeUnit.SECONDS)

    override fun getSavedMessages(): Single<List<SupportChatMessage>> =
            Single.just(
                    listOf(
                            SupportChatMessage("Eugene", "Hello", 123),
                            SupportChatMessage("Operator 1", "Hello!", 123),
                            SupportChatMessage("Eugene", "It was nice to talk to you. See you later!", 123)
                    )
            )

    override fun getSupportOperatorNumber(): Single<String> =
            Single.just(IDCDriversApp.instance.getString(R.string.default_support_operator_number))
}