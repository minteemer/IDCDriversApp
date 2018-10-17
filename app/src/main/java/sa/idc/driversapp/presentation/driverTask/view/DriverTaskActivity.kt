package sa.idc.driversapp.presentation.driverTask.view

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_driver_task.*
import sa.idc.driversapp.R
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.util.DateFormats
import sa.idc.driversapp.presentation.driverTask.presenter.DriverTaskPresenter
import sa.idc.driversapp.presentation.driverTask.presenter.DriverTaskView
import android.R.attr.y
import android.R.attr.x
import android.graphics.*
import com.google.android.gms.maps.Projection
import com.google.android.gms.maps.model.JointType.ROUND
import android.graphics.Paint.Join
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.Polyline
import android.R.attr.mode
import android.util.Log
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

import sa.idc.driversapp.repositories.preferences.AppPreferences

class DriverTaskActivity : AppCompatActivity(), DriverTaskView {

    companion object {
        private const val TASK_ID_INTENT_FIELD = "task_id"

        fun start(context: Context, taskId: Int) {
            Intent(context, DriverTaskActivity::class.java).apply {
                putExtra(TASK_ID_INTENT_FIELD, taskId)
            }.also { context.startActivity(it) }
        }
    }

    private val presenter = DriverTaskPresenter(this)

    private lateinit var map: GoogleMap
    private val mapReadyListener = MapReadyListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_task)
        setTitle(R.string.driver_task_activity_title)

        val id = intent.getIntExtra(TASK_ID_INTENT_FIELD, -1)

        initMap()

        presenter.loadTask(id)
    }

    override fun showTask(driverTask: DriverTask) {
        tv_address_field.text = driverTask.order.destinationAddress
        tv_contacts_field.text = driverTask.order.customerContacts
        tv_due_date_field.text = DateFormats.defaultDateTime.format(driverTask.order.dueDate)

        mapReadyListener.setDestination(
                driverTask.order.destination.let { LatLng(it.latitude, it.longitude) }
        )
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
        val l2 = LatLng(55.7514319, 48.754850399999995)
        val l1 = LatLng(55.7514319,48.754850399999995)

        val request = "https://maps.googleapis.com/maps/api/directions/json?"+
                "origin=${l1.latitude},${l1.longitude}"+
                "&destination=${l2.latitude},${l2.longitude}"+
                "&key=${getString(R.string.google_maps_key)}"
        Log.d("DTActivityRequest",request)
        mapFragment.getMapAsync(mapReadyListener)

        mapReadyDisposable = Single.create(mapReadyListener)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { (map, location) ->
                    this.map = map.apply {
                        var result:String =""
                        val obj = URL(request)
                        Single.create<String> {emmiter ->
                            var output =""
                            with(obj.openConnection() as HttpURLConnection) {
                                requestMethod = "GET"
                                BufferedReader(InputStreamReader(inputStream)).use {
                                    val response = StringBuffer()
                                    var inputLine = it.readLine()
                                    while (inputLine != null) {
                                        response.append(inputLine)
                                        inputLine = it.readLine()
                                        output+=inputLine+"\n"
                                    }


                                }
                            }
                            Log.d("OutputActivity",output)
                            val jsonObj = JSONObject(output.substring(output.indexOf("{"), output.lastIndexOf("}") + 1))
                            val path = jsonObj.getString("points")
                            Log.d("pathAc",path)

                            emmiter.onSuccess(path)
                        }
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        {
                                            result = it;
                                           // Log.d("TDActivityResponse", result)
                                            addMarker(MarkerOptions().position(location).title("Destination"))
                                            moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14f))

                                        },
                                        {}
                                )




                    }
                }
    }

    override fun onDestroy() {
        mapReadyDisposable?.dispose()
        presenter.destroy()
        super.onDestroy()
    }
}
