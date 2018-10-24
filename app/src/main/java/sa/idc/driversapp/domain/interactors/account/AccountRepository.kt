package sa.idc.driversapp.domain.interactors.account

import io.reactivex.Single

interface AccountRepository {

    enum class LoginResult {
        Success, WrongPassword, ConnectionError
    }

    /**
     * Try to login into account with given [login] and [password]
     * @return [Single] with [AccountRepository.LoginResult] and token,
     * if result is [AccountRepository.LoginResult.Success]
     */
    fun login(login: String, password: String): Single<Pair<LoginResult, String>>
}