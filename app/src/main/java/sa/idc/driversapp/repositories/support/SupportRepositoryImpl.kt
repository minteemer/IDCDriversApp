package sa.idc.driversapp.repositories.support

import io.reactivex.Single
import sa.idc.driversapp.IDCDriversApp
import sa.idc.driversapp.R
import sa.idc.driversapp.domain.interactors.support.SupportRepository

class SupportRepositoryImpl: SupportRepository {
    override fun getSupportOperatorNumber(): Single<String> =
            Single.just(IDCDriversApp.instance.getString(R.string.default_support_operator_number))
}