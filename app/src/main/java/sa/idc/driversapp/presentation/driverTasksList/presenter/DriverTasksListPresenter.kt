package sa.idc.driversapp.presentation.driverTasksList.presenter

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.domain.interactors.driverTasks.DriverTasksInteractor
import sa.idc.driversapp.repositories.preferences.AppPreferences

class DriverTasksListPresenter(private val view: DriverTasksListView) {

    private val interactor = DriverTasksInteractor()

    private val disposables = CompositeDisposable()

    fun callToOperator() {
        interactor.getOperatorPhoneNumber()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view.callSupportNumber(it) },
                        { Log.e("TaskListPresenter", "callToOperator error") })
                .also { disposables.add(it) }
    }

    fun logOut() {
        var preferences = AppPreferences.instance
        preferences.login = AppPreferences.Default.LOGIN
        preferences.token = AppPreferences.Default.TOKEN
    }
    fun refreshTasks() {
        view.startTasksRefresh()
        interactor.getTasksList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view.apply {
                                showTasksList(it)
                                finishTasksRefresh()
                            }
                        },
                        {
                            Log.e("TasksListPresenter", "Get tasks list error", it)
                            view.showGetTasksError()
                        }
                )
                .also { disposables.add(it) }
    }

    fun selectTask(task: DriverTask) {
        view.openTask(task.id)
    }

    fun destroy(){
        disposables.dispose()
    }
}