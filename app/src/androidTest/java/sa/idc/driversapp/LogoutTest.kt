package sa.idc.driversapp

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.matcher.ViewMatchers.withId
import org.junit.Test
import sa.idc.driversapp.presentation.loginIn.view.LoginActivity

class LogoutTest{
    @Test
    fun logout(){
        onView(withId(R.menu.tasks_list_menu)).perform(click())
        onView(withId(R.id.log_out_item)).perform(click())
        intended(hasComponent(LoginActivity::class.java.getName()))

    }

}

