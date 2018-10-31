package sa.idc.driversapp.presentation.supportChat.presenter

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import sa.idc.driversapp.domain.interactors.support.SupportInteractor
import sa.idc.driversapp.repositories.support.SupportRepositoryImpl

class SupportChatPresenter(private val view: SupportChatView) {

    private val supportInteractor = SupportInteractor(SupportRepositoryImpl())

    private val disposables = CompositeDisposable()

    fun getMessages() {
        supportInteractor.getSavedMessages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { messages ->
                    view.showMessages(messages)

                    SupportInteractor.newMessagesObservable
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                view.addNewMessage(it)
                            }
                }
                .also { disposables.add(it) }
    }

    fun sendMessage(message: String) {
        view.startSendMessageProgress()
        supportInteractor.sendMessage(message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view.onMessageSent() },
                        {
                            view.onMessageError()
                            Log.e("SupportChatPresenter", "Send message error", it)
                        }
                )
                .also { disposables.add(it) }
    }

    fun destroy() {
        disposables.dispose()
    }
}