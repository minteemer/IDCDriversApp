package sa.idc.driversapp.domain.entities.tracking

import android.location.Location
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator
import java.util.*

data class TrackingData @StorIOSQLiteCreator constructor(
        val location: Location,
        val timeMills: Long
) {
    val date: Date
        get() = Date(timeMills)
}