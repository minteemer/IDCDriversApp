package sa.idc.driversapp.domain.interactors.support

class SupportInteractor(private val repository: SupportRepository) {

    fun getSupportOperatorNumber() = repository.getSupportOperatorNumber()

}