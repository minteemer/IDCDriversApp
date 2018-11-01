package sa.idc.driversapp.domain.interactors.account

import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import sa.idc.driversapp.repositories.account.AccountRepositoryImpl
import sa.idc.driversapp.repositories.preferences.AppPreferences

class AccountInteractor {

    private val preferences = AppPreferences.instance
    private val repository: AccountRepository = AccountRepositoryImpl()

    fun login(login: String, password: String): Single<AccountRepository.LoginResult> =
            getFirebaseToken().flatMap { firebaseToken ->
                repository.login(login, password, firebaseToken).map {
                    val (loginResult, token) = it
                    if (loginResult == AccountRepository.LoginResult.Success) {
                        preferences.login = login
                        preferences.token = token
                    }

                    loginResult
                }.flatMap { loginResult ->
                    repository.getAccountData().map { accountData ->
                        preferences.apply {
                            driverId = accountData.id
                            driverName = accountData.name
                        }

                        loginResult
                    }
                }
            }

    private fun getFirebaseToken() = Single.create<String> {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { result ->
            it.onSuccess(result.token)
        }
    }.observeOn(Schedulers.io())

    fun logout() {
        preferences.apply {
            login = AppPreferences.Default.LOGIN
            token = AppPreferences.Default.TOKEN
        }
    }

}