package sa.idc.driversapp.data.db

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import sa.idc.driversapp.IDCDriversApp

class DBHelper : SQLiteOpenHelper(
        IDCDriversApp.instance,
        DB_NAME,
        null,
        VERSION
) {
    companion object {
        const val DB_NAME = "IDCDriversAppDB"
        const val VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // first version
    }


}