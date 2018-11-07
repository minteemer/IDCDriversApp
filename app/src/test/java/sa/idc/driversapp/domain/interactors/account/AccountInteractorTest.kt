package sa.idc.driversapp.domain.interactors.account

import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class AccountInteractorTest {
    @Test
    fun loginIsCorrect() {
        val login = "Admin"
        val pass = "Admin"
        val fbToken = "firebaseToken"
        val accountRepository = TestAccountRepository(login, pass, fbToken)
        val preferences = TestAccountPreferences.instance
        val interactor = AccountInteractor(accountRepository, preferences)
        val result = interactor.login(login, pass).blockingGet()
        println(result)
        assertEquals(result, AccountRepository.LoginResult.Success)
    }


}
