package sa.idc.driversapp.presentation.loginIn.presenter;

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import sa.idc.driversapp.domain.interactors.account.AccountInteractor

class LoginPresenter(private val view: LoginView) {
    private val interactor = AccountInteractor()

    private val disposables = CompositeDisposable()

    fun login(log: String, password: String) {
        interactor.login(log, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            when (it) {
                                AccountInteractor.LoginResult.Success -> view.openTaskList()
                                AccountInteractor.LoginResult.WrongPassword -> view.showWrongPasswordMessage()
                                AccountInteractor.LoginResult.ConnectionError -> view.showConnectionErrorMessage()
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
