package sa.idc.driversapp.presentation.driverTask.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_driver_task.*
import sa.idc.driversapp.R
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.presentation.driverTask.presenter.DriverTaskPresenter
import sa.idc.driversapp.presentation.driverTask.presenter.DriverTaskView
import sa.idc.driversapp.repositories.driverTasks.DriverTasksRepository
import sa.idc.driversapp.repositories.driverTasks.DummyDriverTasksRepository

class DriverTaskActivity : AppCompatActivity(), DriverTaskView {
    override fun loadTask(driverTask: DriverTask) {
        address.text = driverTask.address
        contacts.text = driverTask.contact
        date.text = driverTask.dueDate.toString()


    }


    private val presenter = DriverTaskPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_tasks_list)
        val id = intent.getIntExtra("ID_OF_TASK",0)
        presenter.loadTask(id)

    }

    override fun showGetTaskError() {
        Toast.makeText(
                this,
                getString(R.string.taskError),
                Toast.LENGTH_SHORT
        ).show()
    }


}
