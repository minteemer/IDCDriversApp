package sa.idc.driversapp.data.db

import android.database.sqlite.SQLiteDatabase

abstract class BaseTable {
    abstract val name: String

    abstract val onCreate: String

    fun onUpdate(db: SQLiteDatabase, oldVersion: Int, newVersion: Int){

    }

}