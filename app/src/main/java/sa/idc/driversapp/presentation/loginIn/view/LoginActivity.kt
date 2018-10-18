package sa.idc.driversapp.presentation.loginIn.view

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import sa.idc.driversapp.R
import sa.idc.driversapp.presentation.loginIn.presenter.LoginView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import sa.idc.driversapp.presentation.driverTasksList.view.DriverTasksListActivity
import sa.idc.driversapp.presentation.loginIn.presenter.LoginPresenter

class LoginActivity : AppCompatActivity(), LoginView {

    companion object {
        fun start(context: Context){
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    private val presenter = LoginPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_btn.setOnClickListener {
            presenter.login(et_login_field.text.toString(), ed_password_field.text.toString())
        }
    }

    override fun showErrorMessage() {
        Toast.makeText(this, "Connection error, please try later", Toast.LENGTH_LONG).show()
    }

    override fun showConnectionErrorMessage() {
        Toast.makeText(this, "Check your internet connection", Toast.LENGTH_LONG).show()
    }

    override fun showWrongPasswordMessage() {
        Toast.makeText(this, "Wrong login or password!", Toast.LENGTH_LONG).show()
    }

    override fun openTaskList() {
        DriverTasksListActivity.start(this)
        finish()
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }

}
