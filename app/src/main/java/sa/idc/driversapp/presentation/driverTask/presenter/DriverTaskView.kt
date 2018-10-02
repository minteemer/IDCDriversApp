package sa.idc.driversapp.presentation.driverTask.presenter

import sa.idc.driversapp.domain.entities.driverTasks.DriverTask

interface DriverTaskView {
    fun loadTask(driverTask: DriverTask)
    fun showGetTaskError()
}