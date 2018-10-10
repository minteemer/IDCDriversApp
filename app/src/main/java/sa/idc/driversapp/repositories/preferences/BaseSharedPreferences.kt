package sa.idc.driversapp.repositories.preferences

import android.content.SharedPreferences
import kotlin.reflect.KProperty

open class BaseSharedPreferences(private val settings: SharedPreferences) {

    protected fun booleanPreference(key: String, defaultValue: Boolean) = PreferenceDelegate(
            { settings.edit().putBoolean(key, it).apply() },
            { settings.getBoolean(key, defaultValue) }
    )

    protected fun intPreference(key: String, defaultValue: Int) = PreferenceDelegate(
            { settings.edit().putInt(key, it).apply() },
            { settings.getInt(key, defaultValue) }
    )

    protected fun longPreference(key: String, defaultValue: Long) = PreferenceDelegate(
            { settings.edit().putLong(key, it).apply() },
            { settings.getLong(key, defaultValue) }
    )

    protected fun floatPreference(key: String, defaultValue: Float) = PreferenceDelegate(
            { settings.edit().putFloat(key, it).apply() },
            { settings.getFloat(key, defaultValue) }
    )

    protected fun doublePreference(key: String, defaultValue: Double) = PreferenceDelegate(
            { settings.edit().putLong(key, it.toRawBits()).apply() },
            {
                // Since version 1.5.1 location fields stored in Double converted to Long instead of Float
                // this change makes SharedPreferences.getLong() to throwClassCastException
                // when tries to read Long from a field where previously Float value was saved
                try {
                    Double.fromBits(settings.getLong(key, defaultValue.toRawBits()))
                } catch (e: ClassCastException) {
                    settings.getFloat(key, defaultValue.toFloat()).toDouble()
                }
            }
    )

    protected fun stringPreference(key: String, defaultValue: String) = PreferenceDelegate(
            { settings.edit().putString(key, it).apply() },
            { settings.getString(key, defaultValue) }
    )

    protected fun stringSetPreference(key: String, defaultValue: Set<String>) = PreferenceDelegate(
            { settings.edit().putStringSet(key, it).apply() },
            { settings.getStringSet(key, defaultValue) }
    )


    class PreferenceDelegate<T>(
            private val preferenceSaver: (value: T) -> Unit,
            private val preferenceReader: () -> T
    ) {

        private var value: T? = null

        operator fun getValue(preferences: Any, prop: KProperty<*>): T {
            return synchronized(this) { value ?: initValue() }
        }

        operator fun setValue(preferences: Any, prop: KProperty<*>, newValue: T) {
            value = newValue
            preferenceSaver(newValue)
        }

        private fun initValue(): T {
            val newValue = preferenceReader()
            value = newValue
            return newValue
        }
    }

}