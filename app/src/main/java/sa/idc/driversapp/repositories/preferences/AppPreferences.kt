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


    object DefaultValues {
        const val TOKEN = ""
        const val LOGIN = ""
    }

    private object Keys {
        const val TOKEN = "token"
        const val LOGIN = "login"
    }
}