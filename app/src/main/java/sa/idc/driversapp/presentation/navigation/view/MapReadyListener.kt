package sa.idc.driversapp.presentation.navigation.view

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.maps.model.DirectionsRoute
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe

class MapReadyListener : OnMapReadyCallback, SingleOnSubscribe<Pair<GoogleMap, DirectionsRoute>> {

    private var emitter: SingleEmitter<Pair<GoogleMap, DirectionsRoute>>? = null
    private var map: GoogleMap? = null
    private var location: DirectionsRoute? = null

    override fun subscribe(emitter: SingleEmitter<Pair<GoogleMap, DirectionsRoute>>) {
        this.emitter = emitter
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.map = googleMap
        checkOnSuccess()
    }

    fun setDestination(location: DirectionsRoute) {
        this.location = location
        checkOnSuccess()
    }

    @Synchronized
    private fun checkOnSuccess() {
        location?.let { l ->
            map?.let { m ->
                emitter?.onSuccess(m to l)
            }
        }
    }
}