package sa.idc.driversapp.domain.interactors.account

import io.reactivex.Single
import sa.idc.driversapp.repositories.account.AccountRepositoryImpl
import sa.idc.driversapp.repositories.preferences.AppPreferences

class AccountInteractor {

    private val preferences = AppPreferences.instance
    private val repository: AccountRepository = AccountRepositoryImpl()

    fun login(login: String, password: String): Single<AccountRepository.LoginResult> =
            repository.login(login, password).map {
                val (loginResult, token) = it
                if (loginResult == AccountRepository.LoginResult.Success) {
                    preferences.login = login
                    preferences.token = token
                }

                loginResult
            }

}