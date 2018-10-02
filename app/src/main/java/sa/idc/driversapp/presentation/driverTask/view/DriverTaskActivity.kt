package sa.idc.driversapp.presentation.driverTask.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import sa.idc.driversapp.R
import sa.idc.driversapp.presentation.driverTask.presenter.DriverTaskPresenter
import sa.idc.driversapp.presentation.driverTask.presenter.DriverTaskView

class DriverTaskActivity : AppCompatActivity(), DriverTaskView {

    private val presenter = DriverTaskPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_task)
    }
}
