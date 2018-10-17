package sa.idc.driversapp.presentation.driverTasksList.view

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.recycler_view_item_driver_task.view.*
import sa.idc.driversapp.R
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.repositories.preferences.AppPreferences
import sa.idc.driversapp.util.DateFormats

class DriverTasksListItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun attach(task: DriverTask, onTaskClick: ((task: DriverTask) -> Unit)?) {
        view.tv_status.visibility = if (task.id == AppPreferences.instance.id_of_accepted_task)
            View.VISIBLE
        else
            View.GONE

        view.apply {
            tv_task_description.text = view.context.getString(
                    R.string.task_list_item_order_id,
                    task.order.id
            )

            tv_due_date.text = view.context.getString(
                    R.string.tasks_list_item_due_date,
                    DateFormats.defaultDateTime.format(task.order.dueDate)
            )
        }

        onTaskClick?.let { listener ->
            view.setOnClickListener { listener(task) }
        }
    }
}