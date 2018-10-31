package sa.idc.driversapp.presentation.driverTask.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_driver_task.*
import sa.idc.driversapp.R
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.presentation.driverTask.presenter.DriverTaskPresenter
import sa.idc.driversapp.presentation.driverTask.presenter.DriverTaskView
import sa.idc.driversapp.presentation.driverTasksList.view.DriverTasksListActivity
import sa.idc.driversapp.presentation.navigation.view.NavigationActivity
import sa.idc.driversapp.repositories.preferences.AppPreferences
import sa.idc.driversapp.util.DateFormats

class DriverTaskActivity : AppCompatActivity(), DriverTaskView {

    companion object {
        private const val TASK_ID_INTENT_FIELD = "task_id"

        fun start(context: Context, taskId: Long) {
            Intent(context, DriverTaskActivity::class.java).apply {
                putExtra(TASK_ID_INTENT_FIELD, taskId)
            }.also { context.startActivity(it) }
        }
    }

    private val preferences = AppPreferences.instance
    private val presenter = DriverTaskPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_task)
        setTitle(R.string.driver_task_activity_title)

        val id = intent.getLongExtra(TASK_ID_INTENT_FIELD, -1L)

        setStatus(id)

        presenter.loadTask(id)
        open_map_btn.setOnClickListener {
            NavigationActivity.start(this,id)
        }
    }

    override fun showAcceptedMessage() {
        Toast.makeText(
                this,
                "Task is accepted",
                Toast.LENGTH_SHORT
        ).show()
    }

    override fun setStatus(taskId: Long) {
        val context = this
        if (preferences.acceptedTaskId == taskId) {
            tv_already_have.visibility = View.GONE

            accept_finish_button.apply {
                text = getString(R.string.finish_task_button)
                visibility = View.VISIBLE
                setBackgroundColor(ContextCompat.getColor(
                        context,
                        R.color.colorAccentDark
                ))
                setOnClickListener {
                    AlertDialog.Builder(context)
                            .setTitle(R.string.finish_question)
                            .setPositiveButton(R.string.yes) { _, _ ->
                                presenter.finishTask(taskId)
                            }
                            .setNegativeButton(R.string.no) { _, _ -> }
                            .create()
                            .show()
                }
            }
        } else {
            if (preferences.acceptedTaskId == AppPreferences.Default.ID_OF_ACCEPTED_TASK) {
                accept_finish_button.apply {
                    text = getString(R.string.accept_task_button)
                    visibility = View.VISIBLE
                    setBackgroundColor(ContextCompat.getColor(
                            context,
                            R.color.colorPrimary
                    ))
                    setOnClickListener {
                        AlertDialog.Builder(context)
                                .setTitle(R.string.question_accept)
                                .setPositiveButton(R.string.yes) { _, _ ->
                                    presenter.acceptTask(taskId)
                                }
                                .setNegativeButton(R.string.no) { _, _ -> }
                                .create()
                                .show()
                    }
                }

                tv_already_have.visibility = View.GONE
            } else {
                accept_finish_button.visibility = View.GONE
                tv_already_have.visibility = View.VISIBLE
            }
        }
    }

    override fun showTask(driverTask: DriverTask) {
        tv_contacts_field.text = driverTask.order.customerPhoneNumber
        tv_due_date_field.text = DateFormats.defaultDateTime.format(driverTask.order.dueDate)
        tv_description_field.text = driverTask.order.description
        tv_customer_email_field.text = this.getString(
                R.string.customer_email_field,
                driverTask.order.customerEmail
        )
        tv_destination_address_field.text = this.getString(
                R.string.destination_address,
                driverTask.order.destinationAddress
        )
        tv_origin_address_field.text = this.getString(
                R.string.original_address,
                driverTask.order.originAddress
        )
        tv_customer_name_field.text = this.getString(
                R.string.customer_name_field,
                driverTask.order.customerName
        )

    }

    override fun finishTask() {
        Toast.makeText(
                this,
                getString(R.string.message_after_finishing),
                Toast.LENGTH_SHORT
        ).show()
        DriverTasksListActivity.start(this)
        finish()
    }

    override fun showConnectionError() {
        Toast.makeText(
                this,
                getString(R.string.driver_task_connection_error),
                Toast.LENGTH_SHORT
        ).show()
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

    private var disposables = CompositeDisposable()


    override fun onDestroy() {
        disposables.dispose()
        presenter.destroy()
        super.onDestroy()
    }
}