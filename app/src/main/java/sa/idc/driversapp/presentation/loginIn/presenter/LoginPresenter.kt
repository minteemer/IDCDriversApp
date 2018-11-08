package sa.idc.driversapp.presentation.loginIn.presenter

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import sa.idc.driversapp.domain.interactors.account.AccountInteractor
import sa.idc.driversapp.domain.interactors.account.AccountRepository

class LoginPresenter(private val view: LoginView) {
    private val interactor = AccountInteractor()

    private val disposables = CompositeDisposable()

    fun login(log: String, password: String) {
        view.startLogInProgress()
        interactor.login(log, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { view.stopLogInProgress() }
                .subscribe(
                        {
                            when (it) {
                                AccountRepository.LoginResult.Success -> view.openTaskList()
                                AccountRepository.LoginResult.WrongPassword -> view.showWrongPasswordMessage()
                                AccountRepository.LoginResult.ConnectionError -> view.showConnectionErrorMessage()
                                else -> view.showErrorMessage()
                            }
                        },
                        {
                            Log.e("LoginPresenter", "loginError", it)
                            view.showErrorMessage()
                        }
                )
                .also { disposables.add(it) }
    }

    fun destroy(){
        disposables.dispose()
    }
}
