package sa.idc.driversapp

import android.app.Application
import com.facebook.stetho.Stetho

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
        Stetho.initializeWithDefaults(this)
    }

}