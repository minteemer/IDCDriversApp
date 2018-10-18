package sa.idc.driversapp.presentation.launcher

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.launcher_activity.*
import sa.idc.driversapp.R
import sa.idc.driversapp.presentation.driverTasksList.view.DriverTasksListActivity
import sa.idc.driversapp.presentation.loginIn.view.LoginActivity
import sa.idc.driversapp.repositories.preferences.AppPreferences
import sa.idc.driversapp.services.trackingData.TrackingDataService
import sa.idc.driversapp.util.AppPermissions

class LauncherActivity : AppCompatActivity() {

    private object RequestCodes {
        const val LOCATION_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.launcher_activity)

        if (AppPermissions.permissionIsGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            initServicesAndFinish()
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        AppPermissions.requestPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION,
                RequestCodes.LOCATION_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            RequestCodes.LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initServicesAndFinish()
                } else {
                    tv_allow_location_message.visibility = View.VISIBLE
                    requestLocationPermission()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun initServicesAndFinish() {
        TrackingDataService.start(this)

        if (AppPreferences.instance.token != AppPreferences.Default.TOKEN){
            DriverTasksListActivity.start(this)
        } else {
            LoginActivity.start(this)
        }

        finish()
    }
}