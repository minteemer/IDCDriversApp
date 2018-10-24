package sa.idc.driversapp.domain.interactors.account

import io.reactivex.Single

interface AccountRepository {

    enum class LoginResult {
        Success, WrongPassword, ConnectionError
    }

    /**
     * Try to login into account with given [login] and [password],
     * sends [firebaseToken] of the device to the server.
     * @return [Single] with [AccountRepository.LoginResult] and token,
     * if result is [AccountRepository.LoginResult.Success]
     */
    fun login(
            login: String,
            password: String,
            firebaseToken: String
    ): Single<Pair<LoginResult, String>>
}