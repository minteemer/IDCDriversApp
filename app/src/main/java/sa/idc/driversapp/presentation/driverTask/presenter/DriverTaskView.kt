package sa.idc.driversapp.presentation.driverTask.presenter

import com.google.maps.model.DirectionsResult
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask

interface DriverTaskView {
    /** Show user task data */
    fun showTask(driverTask: DriverTask)

    /** Tell user that error occurred while getting task data */
    fun showGetTaskError()

    /** Close the activity */
    fun close()

    /** Show given route on the map */
    fun showRoute(directions: DirectionsResult)

    /**Show connection error message*/
    fun showConnectionError()

    /**Set status to the task in view*/
    fun setStatus(taskId: Int)

    /**Finish task after acknowledgment from server*/
    fun finishTask()

    /** Tell user that task was successfully accepted */
    fun showAcceptedMessage()
}