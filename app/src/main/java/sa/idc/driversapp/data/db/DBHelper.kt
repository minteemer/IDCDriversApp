package sa.idc.driversapp.data.db

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.pushtorefresh.storio3.sqlite.impl.DefaultStorIOSQLite
import sa.idc.driversapp.IDCDriversApp
import sa.idc.driversapp.data.db.support.SupportChatMessageEntry
import sa.idc.driversapp.data.db.support.SupportChatMessageEntrySQLiteTypeMapping
import sa.idc.driversapp.data.db.tasks.*
import sa.idc.driversapp.data.db.tracking.TrackingDataTable
import sa.idc.driversapp.data.db.tracking.entities.TrackingData
import sa.idc.driversapp.data.db.tracking.entities.TrackingDataSQLiteTypeMapping

class DBHelper : SQLiteOpenHelper(IDCDriversApp.instance, DB_NAME, null, VERSION) {

    companion object {
        private const val DB_NAME = "IDCDriversAppDB"

        private const val VERSION = 1
        val defaultStorIOBuilder: DefaultStorIOSQLite.CompleteBuilder by lazy {
            DefaultStorIOSQLite.builder()
                    .sqliteOpenHelper(DBHelper())
                    .addTypeMapping(TrackingData::class.java, TrackingDataSQLiteTypeMapping())
                    .addTypeMapping(OrderEntry::class.java, OrderEntrySQLiteTypeMapping())
                    .addTypeMapping(TaskEntry::class.java, TaskEntry.typeMapping)
                    .addTypeMapping(
                            SupportChatMessageEntry::class.java,
                            SupportChatMessageEntrySQLiteTypeMapping()
                    )
        }

        fun onLogout() {
            listOf(
                    TrackingDataTable.NAME,
                    OrderEntry.Table.NAME,
                    TaskEntry.Table.NAME,
                    SupportChatMessageEntry.Table.NAME
            ).forEach {
                DBHelper().writableDatabase.execSQL("DELETE FROM $it")
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TrackingDataTable.ON_CREATE)
        db.execSQL(OrderEntry.Table.ON_CREATE)
        db.execSQL(TaskEntry.Table.ON_CREATE)
        db.execSQL(SupportChatMessageEntry.Table.ON_CREATE)
    }

    override fun onConfigure(db: SQLiteDatabase) {
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // first version
    }

}