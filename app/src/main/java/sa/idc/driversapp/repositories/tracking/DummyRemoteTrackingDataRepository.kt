package sa.idc.driversapp.repositories.tracking

import android.location.Location
import io.reactivex.Single
import sa.idc.driversapp.domain.interactors.tracking.RemoteTrackingDataRepository
import java.util.*

class DummyRemoteTrackingDataRepository : RemoteTrackingDataRepository {
    override fun sendTrackingData(location: Location, time: Date)
            : Single<RemoteTrackingDataRepository.SendDataResult> =
            Single.just(RemoteTrackingDataRepository.SendDataResult.Success)
}