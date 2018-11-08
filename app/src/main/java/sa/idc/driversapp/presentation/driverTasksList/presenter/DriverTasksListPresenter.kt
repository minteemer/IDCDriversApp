package sa.idc.driversapp.presentation.driverTasksList.presenter

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.domain.interactors.account.AccountInteractor
import sa.idc.driversapp.domain.interactors.driverTasks.DriverTasksInteractor

class DriverTasksListPresenter(private val view: DriverTasksListView) {

    private val interactor = DriverTasksInteractor()

    private val disposables = CompositeDisposable()


    fun logOut() {
        AccountInteractor().logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                        { view.logOut() },
                        { Log.d("TasksListPresenter", "Log out error", it)}
                )
                .also { disposables.add(it) }
    }

    fun refreshTasks() {
        view.startTasksRefresh()
        interactor.refreshTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { view.finishTasksRefresh() }
                .subscribe(
                        { view.showTasksList(it) },
                        {
                            Log.e("TasksListPresenter", "Get tasks list error", it)
                            view.showGetTasksError()
                            getCachedTasks()
                        }
                )
                .also { disposables.add(it) }
    }

    private fun getCachedTasks() {
        interactor.getCachedTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view.showTasksList(it) },
                        { Log.e("TasksListPresenter", "Get cached tasks list error", it) }
                )
                .also { disposables.add(it) }
    }

    fun selectTask(task: DriverTask) {
        view.openTask(task.id)
    }

    fun destroy() {
        disposables.dispose()
    }

    fun connectWithSupport() {
        view.startSupportChatActivity()
    }
}