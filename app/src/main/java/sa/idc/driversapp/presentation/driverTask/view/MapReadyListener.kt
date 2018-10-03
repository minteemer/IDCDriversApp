package sa.idc.driversapp.presentation.driverTask.view

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe

class MapReadyListener : OnMapReadyCallback, SingleOnSubscribe<Pair<GoogleMap, LatLng>> {

    private var emitter: SingleEmitter<Pair<GoogleMap, LatLng>>? = null
    private var map: GoogleMap? = null
    private var location: LatLng? = null

    override fun subscribe(emitter: SingleEmitter<Pair<GoogleMap, LatLng>>) {
        this.emitter = emitter
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.map = googleMap
        checkOnSuccess()
    }

    fun setDestination(location: LatLng) {
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