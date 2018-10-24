package sa.idc.driversapp.domain.interactors.support

import io.reactivex.Single

interface SupportRepository {
    fun getSupportOperatorNumber(): Single<String>
}