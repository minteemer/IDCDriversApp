package sa.idc.driversapp.presentation.driverTasksList.view

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_driver_tasks_list.*
import sa.idc.driversapp.R
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.presentation.driverTask.view.DriverTaskActivity
import sa.idc.driversapp.presentation.driverTasksList.presenter.DriverTasksListPresenter
import sa.idc.driversapp.presentation.driverTasksList.presenter.DriverTasksListView
import android.view.MenuItem
import android.view.View
import sa.idc.driversapp.presentation.loginIn.view.LoginActivity
import sa.idc.driversapp.presentation.supportChat.view.SupportChatActivity
import sa.idc.driversapp.util.AppPermissions


class DriverTasksListActivity : AppCompatActivity(), DriverTasksListView {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, DriverTasksListActivity::class.java))
        }
    }

    private val presenter = DriverTasksListPresenter(this)

    private val tasksList = ArrayList<DriverTask>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_tasks_list)
        setTitle(R.string.driver_tasks_list_title)

        initTasksRecycler()

        swipe_to_refresh_tasks_list.apply {
            setColorSchemeResources(R.color.colorAccent)
            setOnRefreshListener { presenter.refreshTasks() }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.refreshTasks()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tasks_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.support_chat_item -> {
                presenter.connectWithSupport()
                true
            }
            R.id.log_out_item -> {
                presenter.logOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initTasksRecycler() {
        rv_driver_tasks_list.layoutManager = LinearLayoutManager(this)
        rv_driver_tasks_list.adapter = DriverTasksListAdapter(tasksList) {
            presenter.selectTask(it)
        }
    }

    override fun startSupportChatActivity() {
        SupportChatActivity.start(this)
    }


    override fun showTasksList(tasks: List<DriverTask>) {
        if (tasks.isEmpty()){
            tv_no_tasks.visibility = View.VISIBLE
            iv_no_tasks.visibility = View.VISIBLE
        }else{
            tv_no_tasks.visibility = View.GONE
            iv_no_tasks.visibility = View.GONE
        }
        tasksList.apply {
            clear()
            addAll(tasks)
        }
        rv_driver_tasks_list.adapter?.notifyDataSetChanged()
    }

    override fun showGetTasksError() {
        Toast.makeText(
                this,
                getString(R.string.get_driver_tasks_list_error),
                Toast.LENGTH_SHORT
        ).show()
    }

    override fun startTasksRefresh() {
        swipe_to_refresh_tasks_list.isRefreshing = true
    }

    override fun finishTasksRefresh() {
        swipe_to_refresh_tasks_list.isRefreshing = false
    }

    override fun openTask(taskId: Long) {
        DriverTaskActivity.start(this, taskId)
    }

    override fun logOut() {
        LoginActivity.start(this)
        finish()
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }
}
