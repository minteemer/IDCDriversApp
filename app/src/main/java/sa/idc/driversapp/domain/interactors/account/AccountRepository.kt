package sa.idc.driversapp.domain.interactors.account

import io.reactivex.Single

interface AccountRepository {

    /**
     * Try to login into account with given [login] and [password]
     * @return [Single] with [AccountInteractor.LoginResult] and token,
     * if result is [AccountInteractor.LoginResult.Success]
     */
    fun login(login: String, password: String): Single<Pair<AccountInteractor.LoginResult, String>>
}