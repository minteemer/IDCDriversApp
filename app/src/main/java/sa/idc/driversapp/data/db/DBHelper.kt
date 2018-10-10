package sa.idc.driversapp.data.db

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.pushtorefresh.storio3.sqlite.impl.DefaultStorIOSQLite
import sa.idc.driversapp.IDCDriversApp
import sa.idc.driversapp.data.db.tracking.TrackingDataTable
import sa.idc.driversapp.data.db.tracking.entities.TrackingData
import sa.idc.driversapp.data.db.tracking.entities.TrackingDataSQLiteTypeMapping

class DBHelper : SQLiteOpenHelper(IDCDriversApp.instance, DB_NAME, null, VERSION) {

    companion object {
        const val DB_NAME = "IDCDriversAppDB"
        const val VERSION = 1

        val defaultStorIOBuilder: DefaultStorIOSQLite.CompleteBuilder by lazy {
            DefaultStorIOSQLite.builder()
                    .sqliteOpenHelper(DBHelper())
                    .addTypeMapping(TrackingData::class.java, TrackingDataSQLiteTypeMapping())
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TrackingDataTable.ON_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // first version
    }

}