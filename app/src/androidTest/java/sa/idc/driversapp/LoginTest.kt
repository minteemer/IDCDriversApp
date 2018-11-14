package sa.idc.driversapp

import android.content.ComponentName
import android.support.test.InstrumentationRegistry.getTargetContext
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sa.idc.driversapp.presentation.driverTasksList.view.DriverTasksListActivity
import sa.idc.driversapp.presentation.loginIn.view.LoginActivity
import android.support.test.espresso.Espresso




/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LoginTest {


    @Rule
    @JvmField
    public var mLoginActivityActivityTestRule = IntentsTestRule(LoginActivity::class.java)


    @Test
    fun loginActivityTest(){

        onView(withId(R.id.et_login_field)).perform(typeText("eb@idc.com"));
        onView(withId(R.id.ed_password_field)).perform(typeText("qwe123"));
        onView(withId(R.id.login_btn)).perform(click());
        Thread.sleep(1  )
        intended(hasComponent(ComponentName(getTargetContext(), DriverTasksListActivity::class.java)))
    }



}

