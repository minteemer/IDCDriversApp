package sa.idc.driversapp.presentation.navigation.view

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
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
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import sa.idc.driversapp.R
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.presentation.navigation.presenter.NavigationPresenter
import sa.idc.driversapp.presentation.navigation.presenter.NavigationView

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

    override fun showTask(driverTask: DriverTask) {
        title = getString(R.string.task_navigation_title, driverTask.id)
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

    private fun addPolyline(route: DirectionsRoute) {
        PolylineOptions()
                .addAll(PolyUtil.decode(route.overviewPolyline.encodedPath))
                .color(ContextCompat.getColor(this, R.color.colorPrimary))
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