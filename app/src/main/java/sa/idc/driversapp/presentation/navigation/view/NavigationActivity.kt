package sa.idc.driversapp.presentation.navigation.view

import android.content.Context
import android.content.Intent
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.google.maps.model.DirectionsRoute
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_task_navigation.*
import sa.idc.driversapp.R
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.presentation.navigation.presenter.NavigationPresenter
import sa.idc.driversapp.presentation.navigation.presenter.NavigationView
import sa.idc.driversapp.repositories.googleMaps.GoogleMapsRepository
import sa.idc.driversapp.repositories.preferences.AppPreferences
import sa.idc.driversapp.services.trackingData.TrackingDataService
import java.util.*
import java.util.concurrent.TimeUnit

class NavigationActivity : AppCompatActivity(), NavigationView {

    companion object {
        private const val TASK_ID_INTENT_FIELD = "task_id"

        fun start(context: Context, taskId: Long) {
            Intent(context, NavigationActivity::class.java).apply {
                putExtra(TASK_ID_INTENT_FIELD, taskId)
            }.also { context.startActivity(it) }
        }
    }

    private val presenter = NavigationPresenter(this)

    private lateinit var map: GoogleMap
    private val mapReadyListener = MapReadyListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_navigation)

        val id = intent.getLongExtra(TASK_ID_INTENT_FIELD, -1L)

        initMap()
        presenter.loadTask(id)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    private var task: DriverTask? = null


    override fun showTask(driverTask: DriverTask) {
        task = driverTask
        title = getString(R.string.task_navigation_title, driverTask.id)

        startTimer(driverTask.order.dueDate)
    }

    private fun startTimer(dueDate: Date) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .map {
                    getTimeLeftString(
                            (dueDate.time - System.currentTimeMillis())
                                    .coerceAtLeast(0)
                    )
                }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { tv_remaining_time.text = it }
                .also { disposables.add(it) }
    }

    private fun getTimeLeftString(millisLeft: Long): String {
        val daysLeft = TimeUnit.MILLISECONDS.toDays(millisLeft)
        val hoursLeft = TimeUnit.MILLISECONDS.toHours(millisLeft) - TimeUnit.DAYS.toHours(daysLeft)
        val minutesLeft = TimeUnit.MILLISECONDS.toMinutes(millisLeft) -
                TimeUnit.HOURS.toMinutes(hoursLeft) -
                TimeUnit.DAYS.toMinutes(daysLeft)

        return getString(R.string.remaining_time, daysLeft, hoursLeft, minutesLeft)
    }

    private var disposables = CompositeDisposable()

    override fun showRoute(directions: DirectionsRoute) {
        mapReadyListener.setDestination(directions)
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.task_navigation_map) as SupportMapFragment
        mapFragment.getMapAsync(mapReadyListener)

        Single.create(mapReadyListener)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { (map, route) ->
                    this.map = map

                    task?.let {
                        if (it.id != AppPreferences.instance.acceptedTaskId) {
                            showRouteToOrigin(it.order.origin)
                        }
                    }

                    try {
                        map.apply {
                            isMyLocationEnabled = true
                            uiSettings.isMyLocationButtonEnabled = true
                        }
                    } catch (e: SecurityException) {
                        Log.e("DriverTaskActivity", "Map initialising error", e)
                    }

                    addMarkersToMap(route)
                    addPolyline(route)
                }
                .also { disposables.add(it) }
    }

    private fun showRouteToOrigin(originLocation: Location) {
        TrackingDataService.currentLocationObservable
                .observeOn(Schedulers.io())
                .subscribe { currentLocation ->
                    GoogleMapsRepository().getPath(currentLocation, originLocation)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        it.routes.getOrNull(0)?.let { route ->
                                            addPolyline(route, colorResource = R.color.colorAccent)
                                        }
                                    },
                                    {
                                        Log.e("NavigationActivity", "Could not show path to origin", it)
                                    }
                            )
                            .also { disposables.add(it) }
                }
                .also { disposables.add(it) }
    }

    private fun addPolyline(route: DirectionsRoute, colorResource: Int = R.color.colorPrimary) {
        PolylineOptions()
                .addAll(route.legs[0].steps.flatMap { PolyUtil.decode(it.polyline.encodedPath) })
                .color(ContextCompat.getColor(this, colorResource))
                .also { map.addPolyline(it) }
    }

    private fun addMarkersToMap(route: DirectionsRoute) {
        route.legs?.getOrNull(0)?.apply {
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
        } ?: showDisplayRouteError()
    }

    private fun showDisplayRouteError() {
        Toast.makeText(
                this,
                "Could not show received path :(",
                Toast.LENGTH_SHORT
        ).show()
    }

    override fun close() {
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

    override fun onDestroy() {
        disposables.dispose()
        presenter.destroy()
        super.onDestroy()
    }
}
