package sa.idc.driversapp.presentation.driverTasksList.presenter

import sa.idc.driversapp.domain.entities.driverTasks.DriverTask

interface DriverTasksListView {
    /** Display given [tasks] to user */
    fun showTasksList(tasks: List<DriverTask>)

    /** Tell user that something went wrong during loading list of tasks */
    fun showGetTasksError()

    /** Show user detailed information about a task  with given [taskId] */
    fun openTask(taskId: Long)

    /** Start task refreshing animation */
    fun startTasksRefresh()

    /** Finish task refreshing animation */
    fun finishTasksRefresh()
}