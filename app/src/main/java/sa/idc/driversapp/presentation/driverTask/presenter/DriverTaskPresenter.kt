package sa.idc.driversapp.presentation.driverTask.presenter

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import sa.idc.driversapp.domain.interactors.driverTasks.DriverTasksInteractor

class DriverTaskPresenter(private val view: DriverTaskView) {
    companion object {
        const val LOG_TAG = "DriverTaskPresenter"
    }

    private val interactor = DriverTasksInteractor()
    private val disposables = CompositeDisposable()

    fun loadTask(id: Int) {
        interactor.getTaskByID(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { task ->
                            task?.let { view.showTask(it) } ?: run {
                                Log.e(LOG_TAG, "Unknown task ID: $id")
                                view.close()
                            }
                        },
                        {
                            Log.e("DriverTaskPresenter", "Error while getting task information", it)
                            view.showGetTaskError()
                        }
                )
                .also { disposables.add(it) }

    }

    fun acceptTask(id: Int) {
        interactor.acceptTaskById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(

                )
    }

    fun destroy() {
        disposables.dispose()
    }
}