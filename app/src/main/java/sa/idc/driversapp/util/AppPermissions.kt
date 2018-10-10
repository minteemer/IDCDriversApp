package sa.idc.driversapp.util

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import sa.idc.driversapp.IDCDriversApp

object AppPermissions {

    fun permissionIsGranted(permission: String): Boolean = Build.VERSION.SDK_INT < 23 ||
            ContextCompat.checkSelfPermission(IDCDriversApp.instance, permission) == PackageManager.PERMISSION_GRANTED

    fun requestPermission(activity: Activity, permission: String, requestCode: Int) =
            requestPermissions(activity, arrayOf(permission), requestCode)

    fun requestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(
                activity,
                permissions,
                requestCode
        )
    }
}