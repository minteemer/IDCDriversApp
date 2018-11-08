package sa.idc.driversapp

import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.CoreMatchers.anything
import org.junit.Test
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.presentation.driverTasksList.view.DriverTasksListActivity

class DriverTaskListTest{
    @Test
    fun selectDriverTask(){
        onData(anything()).inAdapterView(withId(R.id.rv_driver_tasks_list))
                .atPosition(0).perform(click());
        Intents.intended(IntentMatchers.hasComponent(DriverTask::class.java.getName()))
    }

}

