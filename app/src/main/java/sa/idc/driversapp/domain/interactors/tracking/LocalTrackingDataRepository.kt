package sa.idc.driversapp.domain.interactors.tracking

import android.location.Location
import io.reactivex.Completable
import sa.idc.driversapp.domain.entities.tracking.TrackingData
import java.sql.Time

interface LocalTrackingDataRepository {
    fun saveTrackingData(location: Location, time: Time): Completable
    fun getTrackingData(): List<TrackingData>
}