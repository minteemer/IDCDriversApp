package sa.idc.driversapp.domain.interactors.tracking

import android.location.Location
import io.reactivex.Completable
import io.reactivex.Single

class TrackingInteractor {

    fun saveLocation(location: Location) : Completable {
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