package sa.idc.driversapp.presentation.loginIn.presenter

interface LoginView {
    /**Open list of tasks */
    fun openTaskList()

    /**show Wrong password toast */
    fun showWrongPasswordMessage()

    /** show error connection message*/
    fun showConnectionErrorMessage()

    /** show error message*/
    fun showErrorMessage()
}
