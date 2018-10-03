package sa.idc.driversapp.domain.entities.tools

import sa.idc.driversapp.IDCDriversApp
import sa.idc.driversapp.R
import java.text.SimpleDateFormat
import java.util.*

object DateFormats {
    val defaultDateTime: SimpleDateFormat
            get() = SimpleDateFormat(
                    IDCDriversApp.instance.getString(R.string.default_date_time_format),
                    Locale.getDefault()
            )
}