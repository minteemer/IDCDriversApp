package sa.idc.driversapp.services.trackingData

import android.app.Service
import android.content.Context
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
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import sa.idc.driversapp.domain.interactors.tracking.TrackingInteractor

class TrackingDataService : Service() {

    companion object {
        const val DATA_SENDING_INTERVAL_MILLS = 30_000
        const val LOG_TAG = "LocationService"

        fun start(context: Context) {
            context.startService(Intent(context, TrackingDataService::class.java))
        }
    }

    private val trackingInteractor = TrackingInteractor()

    private val saveLocationDisposables = CompositeDisposable()
    private var sendDataDisposable: Disposable? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var lastSentDataTime = -1

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        initLocationLogging()
        sendData()
    }

    fun initLocationLogging() {
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
        } else {
            stopSelf()
        }
    }

    private fun sendLocation(location: Location?) {
        if (location != null && System.currentTimeMillis() - lastSentDataTime > DATA_SENDING_INTERVAL_MILLS) {
            trackingInteractor.saveLocation(location)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .subscribe(
                            { sendData() },
                            { Log.e(LOG_TAG, "Error while saving tracking data", it) }
                    )
                    .also { saveLocationDisposables.add(it) }
        }

    }


    private fun sendData() {
        if (sendDataDisposable?.isDisposed != false) {
            sendDataDisposable = trackingInteractor.sendTrackingData()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .subscribe(
                            { Log.i(LOG_TAG, "Sending tracking data is finished") },
                            { Log.e(LOG_TAG, "Error while sending tracking data", it) }
                    )
        }
    }

    override fun onDestroy() {
        saveLocationDisposables.dispose()
        sendDataDisposable?.dispose()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    override fun onBind(intent: Intent): IBinder? = null
}
