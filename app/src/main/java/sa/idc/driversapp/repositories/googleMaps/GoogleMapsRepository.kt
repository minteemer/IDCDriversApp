package sa.idc.driversapp.repositories.googleMaps

import android.location.Location
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.LatLng
import com.google.maps.model.TravelMode
import io.reactivex.Single
import org.joda.time.DateTime
import sa.idc.driversapp.IDCDriversApp
import sa.idc.driversapp.R

class GoogleMapsRepository {

    companion object {
        private val geoApiContext: GeoApiContext by lazy {
            GeoApiContext()
                    .setQueryRateLimit(3)
                    .setApiKey(IDCDriversApp.instance.getString(R.string.google_maps_key))
        }
    }

    fun getPath(origin: Location, destination: Location): Single<DirectionsResult> =
            Single.create<DirectionsResult> { emitter ->
                DirectionsApi.newRequest(geoApiContext)
                        .mode(TravelMode.DRIVING)
                        .origin(LatLng(origin.latitude, origin.longitude))
                        .destination(LatLng(destination.latitude, destination.longitude))
                        .departureTime(DateTime())
                        .alternatives(true)
                        .await()
                        .also { emitter.onSuccess(it) }
            }

}