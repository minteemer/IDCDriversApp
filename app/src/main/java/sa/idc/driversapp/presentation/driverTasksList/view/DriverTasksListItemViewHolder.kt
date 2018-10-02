package sa.idc.driversapp.presentation.driverTasksList.view

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.recycler_view_item_driver_task.view.*
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import java.text.SimpleDateFormat

class DriverTasksListItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val dateFormat = SimpleDateFormat.getDateTimeInstance()

    fun attach(task: DriverTask, onTaskClick: ((task: DriverTask) -> Unit)?) {
        view.apply {
            tv_destination.text = task.address
            tv_due_date.text = dateFormat.format(task.dueDate)
        }

        onTaskClick?.let { listener ->
            view.setOnClickListener { listener(task) }
        }
    }
}