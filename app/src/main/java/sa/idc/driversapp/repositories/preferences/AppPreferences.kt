package sa.idc.driversapp.repositories.preferences

import android.content.Context
import sa.idc.driversapp.IDCDriversApp

class AppPreferences private constructor(context: Context) :
        BaseSharedPreferences(context.getSharedPreferences(NAME, Context.MODE_PRIVATE)) {

    companion object {
        private const val NAME = "app_preferences"

        val instance: AppPreferences by lazy { AppPreferences(IDCDriversApp.instance) }
    }


    var token: String by stringPreference(Keys.TOKEN, DefaultValues.TOKEN)

    var login: String by stringPreference(Keys.LOGIN, DefaultValues.LOGIN)

    var id_of_accepted_task: Int by intPreference(Keys.ID_OF_ACCEPTED_TASK, DefaultValues.ID_OF_ACCEPTED_TASK)

    object DefaultValues {
        const val TOKEN = ""
        const val LOGIN = ""
        const val ID_OF_ACCEPTED_TASK = -1
    }

    private object Keys {
        const val TOKEN = "token"
        const val LOGIN = "login"
        const val ID_OF_ACCEPTED_TASK = "ID_OF_ACCEPTED_TASK"
    }
}