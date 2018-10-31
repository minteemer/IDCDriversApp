package sa.idc.driversapp.presentation.supportChat.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import sa.idc.driversapp.domain.interactors.support.SupportInteractor
import sa.idc.driversapp.repositories.support.SupportRepositoryImpl

class SupportChatPresenter(private val view: SupportChatView) {

    private val supportInteractor = SupportInteractor(SupportRepositoryImpl())

    private val disposables = CompositeDisposable()

    fun getMessages(){
        supportInteractor.getChatMessages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { messages ->
                    view.showMessages(messages)
                }
                .also { disposables.add(it) }
    }

    fun destroy(){
        disposables.dispose()
    }
}