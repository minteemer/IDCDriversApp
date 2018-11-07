package sa.idc.driversapp

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import sa.idc.driversapp.presentation.driverTasksList.view.DriverTasksListActivity
import sa.idc.driversapp.presentation.loginIn.view.LoginActivity

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LoginTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.idc.driversapp", appContext.packageName)
    }
    @Test
    fun loginActivityTest(){

        onView(withId(R.id.et_login_field)).perform(typeText("eb@idc.com"));
        onView(withId(R.id.ed_password_field)).perform(typeText("qwe123"));
        onView(withId(R.id.login_btn)).perform(click());
        intended(hasComponent(DriverTasksListActivity::class.java.getName()))
    }
    @Test
    fun logout(){
        onView(withId(R.id.log_out_item)).perform(click())
        intended(hasComponent(LoginActivity::class.java.getName()))

    }

}
