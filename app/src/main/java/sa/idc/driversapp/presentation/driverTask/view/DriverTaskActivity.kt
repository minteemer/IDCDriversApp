package sa.idc.driversapp.presentation.driverTask.view

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_driver_task.*
import sa.idc.driversapp.R
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.util.DateFormats
import sa.idc.driversapp.presentation.driverTask.presenter.DriverTaskPresenter
import sa.idc.driversapp.presentation.driverTask.presenter.DriverTaskView

class DriverTaskActivity : AppCompatActivity(), DriverTaskView {

    companion object {
        private const val TASK_ID_INTENT_FIELD = "task_id"

        fun start(context: Context, taskId: Int) {
            Intent(context, DriverTaskActivity::class.java).apply {
                putExtra(TASK_ID_INTENT_FIELD, taskId)
            }.also { context.startActivity(it) }
        }
    }

    private val presenter = DriverTaskPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_task)
        setTitle(R.string.driver_task_activity_title)

        val id = intent.getIntExtra(TASK_ID_INTENT_FIELD, -1)

        presenter.loadTask(id)
    }

    override fun loadTask(driverTask: DriverTask) {
        tv_address_field.text = driverTask.address
        tv_contacts_field.text = driverTask.contact
        tv_due_date_field.text = DateFormats.defaultDateTime.format(driverTask.dueDate)
    }

    override fun showGetTaskError() {
        Toast.makeText(
                this,
                getString(R.string.taskError),
                Toast.LENGTH_SHORT
        ).show()
    }

    override fun close() {
        finish()
    }
}
