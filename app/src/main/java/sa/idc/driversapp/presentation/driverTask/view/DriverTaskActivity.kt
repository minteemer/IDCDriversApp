package sa.idc.driversapp.presentation.driverTask.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.google.maps.model.DirectionsResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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

    override fun showAcceptedMessage(){
        Toast.makeText(
                this,
                "Task is accepted",
                Toast.LENGTH_SHORT
        ).show()
    }
    override fun setStatus(taskId: Int) {
        if (preferences.acceptedTaskId == taskId) {
            accept_finish_button.text = getString(R.string.finish_task_button)
            accept_finish_button.visibility =View.VISIBLE
            tv_already_have.visibility =View.INVISIBLE
            accept_finish_button.setBackgroundColor(ContextCompat.getColor(
                    this,
                    R.color.colorAccentDark
            ))
            accept_finish_button.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(R.string.finish_question)
                builder.setPositiveButton(R.string.yes, DialogInterface.OnClickListener { dialog, id ->
                    presenter.finishTask(taskId)
                })
                builder.setNegativeButton(R.string.no, DialogInterface.OnClickListener { dialog, id ->

                })
                val dialog = builder.create()
                dialog.show()

            }
        } else {
            if (preferences.acceptedTaskId==AppPreferences.Default.ID_OF_ACCEPTED_TASK) {
                accept_finish_button.apply {
                    text = getString(R.string.accept_task_button)
                    visibility =View.VISIBLE
                }
                accept_finish_button.setBackgroundColor(ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                ))

                tv_already_have.visibility =View.INVISIBLE
                accept_finish_button.setOnClickListener {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage(R.string.question_accept)
                    builder.setPositiveButton(R.string.yes, DialogInterface.OnClickListener { dialog, id ->
                        presenter.acceptTask(taskId)
                    })
                    builder.setNegativeButton(R.string.no, DialogInterface.OnClickListener { dialog, id ->

                    })
                    val dialog = builder.create()
                    dialog.show()


                }
            }else{
                accept_finish_button.visibility = View.INVISIBLE
                tv_already_have.visibility =View.VISIBLE
            }
        }
    }

    override fun showTask(driverTask: DriverTask) {
        tv_contacts_field.text = driverTask.order.customerContacts
        tv_due_date_field.text = DateFormats.defaultDateTime.format(driverTask.order.dueDate)
        tv_description.text = driverTask.order.description
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

    override fun showRoute(directions: DirectionsResult) {
        mapReadyListener.setDestination(directions)
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.task_description_map) as SupportMapFragment
        mapFragment.getMapAsync(mapReadyListener)

        Single.create(mapReadyListener)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { (map, directions) ->
                    this.map = map

                    try {
                        map.apply {
                            isMyLocationEnabled = true
                            uiSettings.isMyLocationButtonEnabled = true
                        }
                    } catch (e: SecurityException) {
                        Log.e("DriverTaskActivity", "Map initialising error", e)
                    }

                    addMarkersToMap(directions)
                    addPolyline(directions)
                }
                .also { disposables.add(it) }
    }

    private fun addPolyline(results: DirectionsResult) {
        PolylineOptions()
                .addAll(PolyUtil.decode(results.routes[0].overviewPolyline.encodedPath))
                .color(ContextCompat.getColor(this, R.color.colorPrimary))
                .also { map.addPolyline(it) }
    }

    private fun addMarkersToMap(results: DirectionsResult) {
        results.routes[0].legs[0].apply {
            val origin = LatLng(startLocation.lat, startLocation.lng)
            val destination = LatLng(endLocation.lat, endLocation.lng)

            MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .position(origin)
                    .title(startAddress)
                    .let { map.addMarker(it) }

            MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .position(destination)
                    .title(endAddress)
                    .snippet("Time: ${duration.humanReadable}, Distance: ${distance.humanReadable}")
                    .let { map.addMarker(it) }

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 14f))
        }
    }

    override fun onDestroy() {
        disposables.dispose()
        presenter.destroy()
        super.onDestroy()
    }
}