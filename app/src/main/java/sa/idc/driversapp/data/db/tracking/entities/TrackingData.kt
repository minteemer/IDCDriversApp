package sa.idc.driversapp.data.db.tracking.entities

import android.location.Location
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType
import sa.idc.driversapp.data.db.tracking.TrackingDataTable
import java.util.*

@StorIOSQLiteType(table = TrackingDataTable.NAME)
data class TrackingData @StorIOSQLiteCreator constructor(
        @StorIOSQLiteColumn(name = TrackingDataTable.Columns.LAT) val lat: Double,
        @StorIOSQLiteColumn(name = TrackingDataTable.Columns.LNG) val lng: Double,
        @StorIOSQLiteColumn(name = TrackingDataTable.Columns.TIME) val timeMills: Long,
        @StorIOSQLiteColumn(key = true, name = TrackingDataTable.Columns.ID) val id: Int? = null
) {

    val date: Date
        get() = Date(timeMills)

    val location: Location
        get() = Location("").apply {
            latitude = lat
            longitude = lng
        }
}