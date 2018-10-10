package sa.idc.driversapp.presentation.loginIn.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import sa.idc.driversapp.R
import sa.idc.driversapp.presentation.loginIn.presenter.LoginView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import sa.idc.driversapp.presentation.driverTask.view.DriverTaskActivity
import sa.idc.driversapp.presentation.driverTasksList.view.DriverTasksListActivity
import sa.idc.driversapp.presentation.loginIn.presenter.LoginPresenter

class LoginActivity : AppCompatActivity(), LoginView {
    private  val presenter= LoginPresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
       // presenter.onLaunch()
        setLoginButton()
    }

    override fun showErrorMessage() {
        Toast.makeText(this, "Login error", Toast.LENGTH_LONG).show()
    }

    override fun showConnectionErrorMessage() {
        Toast.makeText(this, "Check your internet connection", Toast.LENGTH_LONG).show()
    }

    override fun showWrongPasswordMessage() {
        Toast.makeText(this, "Wrong password", Toast.LENGTH_LONG).show()
    }

    override fun openTaskList() {
        DriverTasksListActivity.start(this)
        finish()
    }
    fun setLoginButton(){
        login_btn.setOnClickListener{
            val login = login_tv_field.text.toString()
            val password = password_tv_field.text.toString()
            presenter.login(log =login,password = password)

        }
    }


}
