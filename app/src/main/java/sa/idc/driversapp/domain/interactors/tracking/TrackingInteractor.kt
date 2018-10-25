package sa.idc.driversapp.domain.interactors.tracking

import android.location.Location
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Single
import sa.idc.driversapp.repositories.tracking.RemoteTrackingDataRepositoryImpl
import sa.idc.driversapp.repositories.tracking.LocalTrackingDataRepositoryImpl
import java.util.*

class TrackingInteractor {

    companion object {
        private const val LOG_TAG = "TrackingInteractor"
    }

    private val remoteRepository: RemoteTrackingDataRepository = RemoteTrackingDataRepositoryImpl()
    private val localRepository: LocalTrackingDataRepository = LocalTrackingDataRepositoryImpl()

    /**
     * Locally saves [location] to DB
     * @return [Completable] that indicates when data is saved and [sendTrackingData]
     * can start to send the data from DB.
     */
    fun saveLocation(location: Location): Completable {
        Log.d(LOG_TAG, "loc: $location, time: ${System.currentTimeMillis()}")
        return localRepository.saveTrackingData(location, Date(System.currentTimeMillis()))
    }

    /**
     * Attemts to send tracking data to the server
     * @return true if all data was successfully sent, false if an error occurred
     */
    fun sendTrackingData(): Single<Boolean> = localRepository.getTrackingData()
            .flatMap { data ->
                data.map {
                    Log.d(LOG_TAG, "Sending tracking data: $it")
                    remoteRepository.sendTrackingData(it.location, it.date).flatMap { result ->
                        if (result == RemoteTrackingDataRepository.SendDataResult.Success)
                            localRepository.removeTrackingData(it)
                                    .toSingleDefault(true)
                        else
                            Single.just(false)
                    }
                }.let {
                    Single.merge(it).reduce(true) { a, b -> a and b }
                }
            }
}