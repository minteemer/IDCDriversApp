package sa.idc.driversapp.repositories.account

import io.reactivex.Single
import sa.idc.driversapp.domain.interactors.account.AccountInteractor
import sa.idc.driversapp.domain.interactors.account.AccountRepository

class DummyAccountRepositoryImpl : AccountRepository {
    companion object {
        const val LOGIN = "test"
        const val PASSWORD = "qwe"

        const val CONN_ERROR = "connection_error"
    }

    override fun login(login: String, password: String)
            : Single<Pair<AccountRepository.LoginResult, String>> =
            when (login) {
                LOGIN ->
                    if (PASSWORD == password)
                        AccountRepository.LoginResult.Success
                    else
                        AccountRepository.LoginResult.WrongPassword
                CONN_ERROR -> AccountRepository.LoginResult.ConnectionError
                else -> AccountRepository.LoginResult.WrongPassword
            }.let { Single.just(it to "test-token") }

}
