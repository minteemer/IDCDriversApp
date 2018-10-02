package sa.idc.driversapp.presentation.driverTask.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import sa.idc.driversapp.domain.interactors.driverTasks.DriverTasksInteractor

class DriverTaskPresenter(private val view: DriverTaskView) {
    private val interactor = DriverTasksInteractor()
    private val disposables = CompositeDisposable()

    fun loadTask(id: Int){
        interactor.getTaskByID(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {view.loadTask(it)},
                        {view.showGetTaskError()}
                )
                .also { disposables.add(it)  }

    }
}