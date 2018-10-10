package sa.idc.driversapp.presentation.loginIn.view

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import sa.idc.driversapp.R
import sa.idc.driversapp.presentation.loginIn.presenter.LoginView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import sa.idc.driversapp.presentation.driverTask.view.DriverTaskActivity
import sa.idc.driversapp.presentation.driverTasksList.view.DriverTasksListActivity
import sa.idc.driversapp.presentation.loginIn.presenter.LoginPresenter

class LoginActivity : AppCompatActivity(), LoginView {
    private  val presenter= LoginPresenter(this)
    private var permission_allowed =false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
       // presenter.onLaunch()
        requestLocationPermission()
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
            if (permission_allowed) {
                val login = login_tv_field.text.toString()
                val password = password_tv_field.text.toString()
                presenter.login(log = login, password = password)
            }else{
                Toast.makeText(this, "You have to allow location permission to use this application", Toast.LENGTH_LONG).show()
                requestLocationPermission()
            }

        }
    }
    private object RequestCodes {
        const val GET_LOCATION_PERMISSIONS = 0
    }

    fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                RequestCodes.GET_LOCATION_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            RequestCodes.GET_LOCATION_PERMISSIONS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    permission_allowed=true
                } else {
                   permission_allowed=false
                }
            }
        }
    }


}
