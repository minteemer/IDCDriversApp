package sa.idc.driversapp.repositories.account

import io.reactivex.Single
import sa.idc.driversapp.data.network.ApiConstructor
import sa.idc.driversapp.data.network.api.AccountAPI
import sa.idc.driversapp.domain.interactors.account.AccountInteractor
import sa.idc.driversapp.domain.interactors.account.AccountRepository

class AccountRepositoryImpl : AccountRepository {

    private val accountApi = ApiConstructor.defaultRetrofit.create(AccountAPI::class.java)

    override fun login(login: String, password: String)
            : Single<Pair<AccountRepository.LoginResult, String>> =
            accountApi.login(login, password)
                    .map {response ->
                        when(response.code()){
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
