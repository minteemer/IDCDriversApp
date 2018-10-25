package sa.idc.driversapp.repositories.account

import io.reactivex.Single
import sa.idc.driversapp.data.network.ApiConstructor
import sa.idc.driversapp.data.network.entities.account.LogInRequestData
import sa.idc.driversapp.domain.interactors.account.AccountRepository

class AccountRepositoryImpl : AccountRepository {

    private val accountApi = ApiConstructor.accountApi

    override fun login(login: String, password: String, firebaseToken: String)
            : Single<Pair<AccountRepository.LoginResult, String>> =
            accountApi.login(LogInRequestData(login, password, firebaseToken))
                    .map { response ->
                        when (response.code()) {
                            in 200..299 -> response.body()?.let {
                                it.result?.let { result ->
                                    AccountRepository.LoginResult.Success to result.token
                                }
                            } ?: AccountRepository.LoginResult.ConnectionError to ""
                            401 -> AccountRepository.LoginResult.WrongPassword to ""
                            else -> AccountRepository.LoginResult.ConnectionError to ""
                        }
                    }

}
