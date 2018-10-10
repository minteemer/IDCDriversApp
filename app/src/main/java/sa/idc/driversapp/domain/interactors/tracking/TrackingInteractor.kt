package sa.idc.driversapp.domain.interactors.tracking

import android.location.Location
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Single

class TrackingInteractor {

    /**
     * Locally saves [location] to DB
     * @return [Completable] that indicates when data is saved and [sendTrackingData]
     * can start to send the data from DB.
     */
    fun saveLocation(location: Location): Completable {
        Log.d("TrackingInter", "loc: $location, time: ${System.currentTimeMillis()}")
        return Completable.complete()
    }

    /**
     * Attemts to send tracking data to the server
     * @return true if all data was sent, false if an error occurred
     */
    fun sendTrackingData(): Single<Boolean> {
        return Single.just(false)
    }
}