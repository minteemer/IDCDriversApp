package sa.idc.driversapp

import android.app.Application
import sa.idc.driversapp.services.trackingData.TrackingDataService

class IDCDriversApp : Application() {
    companion object {
        lateinit var instance: IDCDriversApp
            private set
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        TrackingDataService.start(this)
    }
}