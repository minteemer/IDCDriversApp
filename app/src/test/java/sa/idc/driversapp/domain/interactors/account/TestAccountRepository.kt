package sa.idc.driversapp.domain.interactors.account

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.HttpException
import sa.idc.driversapp.data.network.ApiConstructor
import sa.idc.driversapp.domain.entities.account.AccountData
import java.util.concurrent.TimeUnit

class TestAccountRepository(
        private val log: String = "log",
        private val pass: String = "pass",
        private val fbToken: String = "fbToken"
) : AccountRepository {

    override fun login(login: String, password: String, firebaseToken: String)
            : Single<Pair<AccountRepository.LoginResult, String>> {
        if (login.equals(log) && password.equals(pass) && firebaseToken.equals(fbToken)) {
            return Single.just(Pair(AccountRepository.LoginResult.Success, "token")).delay(1, TimeUnit.SECONDS)
        } else {
            return Single.just(Pair(AccountRepository.LoginResult.WrongPassword, "")).delay(1, TimeUnit.SECONDS)
        }
    }


    override fun getAccountData(): Single<AccountData> =
            Single.just(AccountData(1,"eb@idc.com","Bobrov"))

    override fun logout(): Completable = Completable.complete()
}
