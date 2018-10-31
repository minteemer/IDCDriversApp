package sa.idc.driversapp.presentation.navigation.presenter

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.domain.interactors.driverTasks.DriverTasksInteractor

class NavigationPresenter(private val view: NavigationView) {
    companion object {
        const val LOG_TAG = "DriverTaskPresenter"
    }

    private val interactor = DriverTasksInteractor()
    private val disposables = CompositeDisposable()

    fun loadTask(id: Long) {
        interactor.getTaskByID(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { task ->
                            task?.let {
                                getRoute(it)
                                view.showTask(it)
                            } ?: run {
                                Log.e(LOG_TAG, "Unknown task ID: $id")
                                view.showGetTaskError()
                                view.close()
                            }
                        },
                        {
                            Log.e("DriverTaskPresenter", "Error while getting task information", it)
                            view.showConnectionError()
                        }
                )
                .also { disposables.add(it) }

    }

    private fun getRoute(task: DriverTask) {
        interactor.getRoute(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                        { result ->
                            result?.let {
                                view.showRoute(it)
                            } ?: view.showGetTaskError()
                        },
                        { view.showConnectionError() }
                )
                .also { disposables.add(it) }
    }

    fun destroy() {
        disposables.dispose()
    }

}