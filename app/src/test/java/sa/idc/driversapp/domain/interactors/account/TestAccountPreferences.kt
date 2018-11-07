package sa.idc.driversapp.domain.interactors.account

import sa.idc.driversapp.IDCDriversApp
import sa.idc.driversapp.repositories.preferences.AppPreferences
import sa.idc.driversapp.repositories.preferences.PreferencesRepository

class TestAccountPreferences:PreferencesRepository{

    companion object {
        private const val NAME = "app_preferences"

        val instance: TestAccountPreferences by lazy { TestAccountPreferences() }

    }
    override var token: String = "1"

    override var login: String = "login"

    override var driverName: String = "Bobrov"

    override var driverId: Long =1

    override var acceptedTaskId: Long = 1

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
