package sa.idc.driversapp.domain.interactors.support

import io.reactivex.Completable
import io.reactivex.Single
import sa.idc.driversapp.domain.entities.support.SupportChatMessage

interface SupportRepository {
    fun getSupportOperatorNumber(): Single<String>

    fun saveReceivedMessage(message: SupportChatMessage): Completable

    fun sendMessage(message: String): Single<SupportChatMessage>

    fun getSavedMessages(): Single<List<SupportChatMessage>>
}