package sa.idc.driversapp.presentation.driverTasksList.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import sa.idc.driversapp.R
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask

class DriverTasksListAdapter(
        private val tasks: List<DriverTask>,
        private val onTaskClick: ((task: DriverTask) -> Unit)?
) : RecyclerView.Adapter<DriverTasksListItemViewHolder>() {

    override fun getItemCount(): Int = tasks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            DriverTasksListItemViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.recycler_view_item_driver_task,
                            parent,
                            false
                    )
            )

    override fun onBindViewHolder(holder: DriverTasksListItemViewHolder, position: Int) {
        holder.attach(tasks[position], onTaskClick)
    }
}