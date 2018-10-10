package sa.idc.driversapp.domain.interactors.account

import sa.idc.driversapp.repositories.account.DummyAccountRepositoryImpl

class AccountInteractor {

    enum class LoginResult {
        Success, WrongPassword, ConnectionError
    }

    private val repository: AccountRepository = DummyAccountRepositoryImpl()

    fun login(login: String, password: String) = repository.login(login, password)
}