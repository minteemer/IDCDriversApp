package sa.idc.driversapp.repositories.preferences

import android.content.Context
import sa.idc.driversapp.IDCDriversApp

class AppPreferences private constructor(context: Context) :
        BaseSharedPreferences(context.getSharedPreferences(NAME, Context.MODE_PRIVATE)) {

    companion object {
        private const val NAME = "app_preferences"

        val instance: AppPreferences by lazy { AppPreferences(IDCDriversApp.instance) }

        fun onLogout() {
            instance.apply {
                login = AppPreferences.Default.LOGIN
                token = AppPreferences.Default.TOKEN
                driverName = AppPreferences.Default.DRIVER_NAME
                driverId = AppPreferences.Default.DRIVER_ID
                acceptedTaskId = AppPreferences.Default.ID_OF_ACCEPTED_TASK
            }
        }
    }


    var token: String by stringPreference(Keys.TOKEN, Default.TOKEN)

    var login: String by stringPreference(Keys.LOGIN, Default.LOGIN)

    var driverName: String by stringPreference(Keys.DRIVER_NAME, Default.DRIVER_NAME)

    var driverId: Long by longPreference(Keys.DRIVER_ID, Default.DRIVER_ID)

    var acceptedTaskId: Long by longPreference(Keys.ID_OF_ACCEPTED_TASK, Default.ID_OF_ACCEPTED_TASK)

    object Default {
        const val TOKEN = ""
        const val LOGIN = ""
        const val ID_OF_ACCEPTED_TASK = -1L
        const val DRIVER_NAME = ""
        const val DRIVER_ID = -1L
    }

    private object Keys {
        const val TOKEN = "token"
        const val LOGIN = "login"
        const val ID_OF_ACCEPTED_TASK = "id_of_accepted_task"
        const val DRIVER_NAME = "driver_name"
        const val DRIVER_ID = "driver_id"
    }
}