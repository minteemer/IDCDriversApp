package sa.idc.driversapp.domain.interactors.support

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import sa.idc.driversapp.domain.entities.support.SupportChatMessage
import sa.idc.driversapp.repositories.support.SupportRepositoryImpl

class SupportInteractor(private val repository: SupportRepository = SupportRepositoryImpl()) {

    companion object {
        private val newMessagesBehaviorSubject: PublishSubject<SupportChatMessage> =
                PublishSubject.create<SupportChatMessage>()

        val newMessagesObservable: Observable<SupportChatMessage> =
                newMessagesBehaviorSubject
    }

    fun getSupportOperatorNumber() = repository.getSupportOperatorNumber()

    fun sendMessage(message: String): Single<SupportChatMessage> =
            repository.sendMessage(message.trim())
                    .flatMap { saveReceivedMessage(it).toSingleDefault(it) }

    fun saveReceivedMessage(message: SupportChatMessage): Completable =
            repository.saveReceivedMessage(message)
                    .doOnComplete { newMessagesBehaviorSubject.onNext(message) }

    fun getSavedMessages(): Single<List<SupportChatMessage>> =
            repository.getSavedMessages()
}