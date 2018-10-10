package sa.idc.driversapp.domain.interactors.tracking

import android.location.Location
import io.reactivex.Single
import java.util.*

interface RemoteTrackingDataRepository {
    enum class SaveDataResult {
        Success, ConnectionError
    }

    fun sendTrackingData(location: Location, time: Date): Single<SaveDataResult>
}