package sa.idc.driversapp.presentation.navigation.presenter

import com.google.maps.model.DirectionsRoute
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask

interface NavigationView {

    /** Show user task data */
    fun showTask(driverTask: DriverTask)

    /** Tell user that error occurred while getting task data */
    fun showGetTaskError()

    /** Close the activity */
    fun close()

    /** Show given route on the map */
    fun showRoute(directions: DirectionsRoute)

    /**Show connection error message*/
    fun showConnectionError()
}