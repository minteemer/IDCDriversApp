package sa.idc.driversapp.presentation.driverTasksList.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import sa.idc.driversapp.R
import sa.idc.driversapp.presentation.driverTasksList.presenter.DriverTasksListPresenter
import sa.idc.driversapp.presentation.driverTasksList.presenter.DriverTasksListView

class DriverTasksListActivity : AppCompatActivity(), DriverTasksListView {

    private val presenter = DriverTasksListPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_tasks_list)
    }
}
