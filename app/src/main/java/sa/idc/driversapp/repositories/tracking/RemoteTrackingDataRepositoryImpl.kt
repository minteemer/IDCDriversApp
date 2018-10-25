package sa.idc.driversapp.repositories.tracking

import android.location.Location
import io.reactivex.Single
import sa.idc.driversapp.data.network.ApiConstructor
import sa.idc.driversapp.data.network.entities.account.DriverLocationData
import sa.idc.driversapp.domain.interactors.tracking.RemoteTrackingDataRepository
import java.util.*

class RemoteTrackingDataRepositoryImpl: RemoteTrackingDataRepository {

    private val accountApi = ApiConstructor.accountApi

    override fun sendTrackingData(location: Location, time: Date)
            : Single<RemoteTrackingDataRepository.SendDataResult> =
            accountApi.sendTrackingData(DriverLocationData(location)).map {
                if(it.isSuccessful)
                    RemoteTrackingDataRepository.SendDataResult.Success
                else
                    RemoteTrackingDataRepository.SendDataResult.ConnectionError
            }
}