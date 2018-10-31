package sa.idc.driversapp.domain.interactors.support

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import sa.idc.driversapp.domain.entities.support.SupportChatMessage

class SupportInteractor(private val repository: SupportRepository) {

    companion object {
        private val newMessagesBehaviorSubject: BehaviorSubject<SupportChatMessage> =
                BehaviorSubject.create<SupportChatMessage>()

        val newMessagesObservable: Observable<SupportChatMessage> =
                newMessagesBehaviorSubject
    }

    fun getSupportOperatorNumber() = repository.getSupportOperatorNumber()

    fun sendMessage(message: String) = repository.sendMessage(message)
            .flatMap { saveReceivedMessage(it).toSingleDefault(it) }

    fun saveReceivedMessage(message: SupportChatMessage) =
            repository.saveReceivedMessage(message)
                    .doOnComplete { newMessagesBehaviorSubject.onNext(message) }

    fun getSavedMessages() = repository.getSavedMessages()
}