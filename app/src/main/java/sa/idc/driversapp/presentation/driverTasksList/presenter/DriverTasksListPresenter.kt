package sa.idc.driversapp.presentation.driverTasksList.presenter

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import sa.idc.driversapp.domain.entities.driverTasks.DriverTask
import sa.idc.driversapp.domain.interactors.driverTasks.DriverTasksInteractor

class DriverTasksListPresenter(private val view: DriverTasksListView) {

    private val interactor = DriverTasksInteractor()

    private val disposables = CompositeDisposable()

    fun getTasks() {
        interactor.getTasksList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view.showTasksList(it) },
                        {
                            Log.e("TasksListPresenter", "Get tasks list error", it)
                            view.showGetTasksError()
                        }
                )
                .also { disposables.add(it) }
    }

    fun selectTask(task: DriverTask) {
        view.openTask(task.taskId)
    }
}