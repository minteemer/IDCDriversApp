package sa.idc.driversapp.presentation.driverTasksList.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
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
import sa.idc.driversapp.presentation.loginIn.view.LoginActivity


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

        presenter.refreshTasks()
    }
    override fun callSupportNumber(number: String) {
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    123)
        } else {
            startActivity(Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:$number")))
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.tasks_list_menu, menu)
        return true
    }
    fun callToOperator(){
        presenter.callToOperator()

    }
    private fun logOut(){
        presenter.logOut()
        LoginActivity.start(this);
        finish()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.getItemId()) {
            R.id.call_support_item -> {
                callToOperator()
                return true
            }
            R.id.log_out_item -> {
                logOut()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun initTasksRecycler() {
        rv_driver_tasks_list.layoutManager = LinearLayoutManager(this)
        rv_driver_tasks_list.adapter = DriverTasksListAdapter(tasksList) {
            presenter.selectTask(it)
        }
    }


    override fun showTasksList(tasks: List<DriverTask>) {
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

    override fun openTask(taskId: Int) {
        DriverTaskActivity.start(this, taskId)
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }
}
