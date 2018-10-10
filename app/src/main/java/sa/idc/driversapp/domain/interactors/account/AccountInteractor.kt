package sa.idc.driversapp.domain.interactors.account

import sa.idc.driversapp.repositories.account.DummyAccountRepositoryImpl
import sa.idc.driversapp.repositories.preferences.AppPreferences

class AccountInteractor {

    enum class LoginResult {
        Success, WrongPassword, ConnectionError
    }

    private val preferences = AppPreferences.instance
    private val repository: AccountRepository = DummyAccountRepositoryImpl()

    fun login(login: String, password: String) = repository.login(login, password).map {
        val (loginResult, token) = it
        if (loginResult == LoginResult.Success) {
            preferences.login = login
            preferences.token = token
        }

        loginResult
    }

    fun isLoggedIn() = preferences.token != AppPreferences.DefaultValues.TOKEN
}