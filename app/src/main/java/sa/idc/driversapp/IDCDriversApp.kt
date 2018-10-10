package sa.idc.driversapp

import android.app.Application

class IDCDriversApp : Application() {
    companion object {
        lateinit var instance: IDCDriversApp
            private set
    }

    init {
        instance = this
    }

}