package sa.idc.driversapp.repositories.account

import io.reactivex.Single
import sa.idc.driversapp.domain.interactors.account.AccountInteractor
import sa.idc.driversapp.domain.interactors.account.AccountRepository

class DummyAccountRepositoryImpl : AccountRepository {
    companion object {
        const val LOGIN = "test_account"
        const val PASSWORD = "qwerty123"

        const val CONN_ERROR = "connection_error"
    }

    override fun login(login: String, password: String)
            : Single<Pair<AccountInteractor.LoginResult, String>> =
            when (login) {
                LOGIN ->
                    if (PASSWORD == password)
                        AccountInteractor.LoginResult.Success
                    else
                        AccountInteractor.LoginResult.WrongPassword
                CONN_ERROR -> AccountInteractor.LoginResult.WrongPassword
                else -> AccountInteractor.LoginResult.WrongPassword
            }.let { Single.just(it to "test-token") }

}
