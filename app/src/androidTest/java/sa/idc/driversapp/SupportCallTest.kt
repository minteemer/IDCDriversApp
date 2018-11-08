package sa.idc.driversapp

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.matcher.ViewMatchers
import org.junit.Test
import sa.idc.driversapp.presentation.loginIn.view.LoginActivity

class SupportCallTest{
    @Test
    fun callSupport(){
        Espresso.onView(ViewMatchers.withId(R.id.call_support_item)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(LoginActivity::class.java.getName()))
    }

}