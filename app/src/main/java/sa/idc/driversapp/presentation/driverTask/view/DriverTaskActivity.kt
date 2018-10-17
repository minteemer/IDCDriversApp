package sa.idc.driversapp.presentation.driverTask.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceActivity
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_driver_task.*
import sa.idc.driversapp.R
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.presentation.driverTask.presenter.DriverTaskPresenter
import sa.idc.driversapp.presentation.driverTask.presenter.DriverTaskView
import sa.idc.driversapp.presentation.driverTasksList.view.DriverTasksListActivity
import sa.idc.driversapp.repositories.preferences.AppPreferences
import sa.idc.driversapp.util.DateFormats

class DriverTaskActivity : AppCompatActivity(), DriverTaskView {

    companion object {
        private const val TASK_ID_INTENT_FIELD = "task_id"

        fun start(context: Context, taskId: Int) {
            Intent(context, DriverTaskActivity::class.java).apply {
                putExtra(TASK_ID_INTENT_FIELD, taskId)
            }.also { context.startActivity(it) }
        }
    }

    private val preferences = AppPreferences.instance
    private val presenter = DriverTaskPresenter(this)

    private lateinit var map: GoogleMap
    private val mapReadyListener = MapReadyListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_task)
        setTitle(R.string.driver_task_activity_title)

        val id = intent.getIntExtra(TASK_ID_INTENT_FIELD, -1)

        initMap()
        setStatus(id)
        presenter.loadTask(id)
    }

    override fun setStatus(taskId: Int) {
        if (preferences.id_of_accepted_task == taskId) {
            accept_finish_button.text = getString(R.string.finish_task_button)
            accept_finish_button.visibility =View.VISIBLE
            tv_already_have.visibility =View.INVISIBLE
            accept_finish_button.setBackgroundColor(Color.parseColor("#FFD700"))
            accept_finish_button.setOnClickListener {
                presenter.finishTask(taskId)
            }
        } else {
            if (preferences.id_of_accepted_task==AppPreferences.DefaultValues.ID_OF_ACCEPTED_TASK) {
                accept_finish_button.text = getString(R.string.accept_task_button)
                accept_finish_button.visibility =View.VISIBLE
                accept_finish_button.setBackgroundColor(Color.parseColor("#9ACD32"))
                tv_already_have.visibility =View.INVISIBLE
                accept_finish_button.setOnClickListener {
                    presenter.acceptTask(taskId)
                }
            }else{
                accept_finish_button.visibility = View.INVISIBLE
                tv_already_have.visibility =View.VISIBLE
            }
        }
    }
    override fun showTask(driverTask: DriverTask) {
        //tv_origin_address.text = driverTask.order.destinationAddress
        //tv_destination_address.text = driverTask.order.destinationAddress
        tv_contacts_field.text = driverTask.order.customerContacts
        tv_due_date_field.text = DateFormats.defaultDateTime.format(driverTask.order.dueDate)
        tv_description.text = driverTask.order.description
        mapReadyListener.setDestination(
                driverTask.order.destination.let { LatLng(it.latitude, it.longitude) }
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

    private var mapReadyDisposable: Disposable? = null

    private fun initMap() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.task_description_map) as SupportMapFragment

        mapFragment.getMapAsync(mapReadyListener)

        mapReadyDisposable = Single.create(mapReadyListener)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { (map, location) ->
                    this.map = map.apply {
                        addMarker(MarkerOptions().position(location).title("Destination"))
                        moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14f))
                    }
                }
    }

    override fun onDestroy() {
        mapReadyDisposable?.dispose()
        presenter.destroy()
        super.onDestroy()
    }
}
