package sa.idc.driversapp.domain.interactors.support

import io.reactivex.Completable
import io.reactivex.Single
import sa.idc.driversapp.domain.entities.support.SupportChatMessage

class SupportInteractor(private val repository: SupportRepository) {

    fun getSupportOperatorNumber() = repository.getSupportOperatorNumber()

    fun sendMessage(message: String) = Completable.complete()

    fun getChatMessages() = Single.just(
            listOf(
                    SupportChatMessage("Eugene", "Hello", 123),
                    SupportChatMessage("Operator 1", "Hello!", 123),
                    SupportChatMessage("Eugene", "It was nice to talk to you. See you later!", 123)
            )
    )
}