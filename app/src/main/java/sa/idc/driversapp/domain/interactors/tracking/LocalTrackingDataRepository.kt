package sa.idc.driversapp.domain.interactors.tracking

import android.location.Location
import io.reactivex.Completable
import io.reactivex.Single
import sa.idc.driversapp.data.db.tracking.entities.TrackingData
import java.util.*

interface LocalTrackingDataRepository {
    fun saveTrackingData(location: Location, time: Date): Completable
    fun getTrackingData(): Single<List<TrackingData>>
    fun removeTrackingData(data: TrackingData): Completable
}