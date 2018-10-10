package sa.idc.driversapp.services.trackingData

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import sa.idc.driversapp.domain.interactors.tracking.TrackingInteractor

class TrackingDataService : Service() {

    companion object {
        const val DATA_SENDING_INTERVAL_MILLS = 30
        const val LOG_TAG = "LocationService"
    }

    private val trackingInteractor = TrackingInteractor()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val saveLocationDisposables = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        initLocationLog()
    }

    private fun initLocationLog() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { sendLocation(it) }
        }
    }

    private fun sendLocation(location: Location?) {
        location?.let {_ ->
            trackingInteractor.saveLocation(location)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .subscribe(
                            { /* Location saved */ },
                            { Log.e(LOG_TAG, "Error while saving tracking data", it)}
                    )
                    .also { saveLocationDisposables.add(it) }
        }
    }

    override fun onDestroy() {
        saveLocationDisposables.dispose()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    override fun onBind(intent: Intent): IBinder? = null
}
