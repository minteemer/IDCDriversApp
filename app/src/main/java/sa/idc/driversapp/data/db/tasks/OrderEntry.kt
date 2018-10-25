package sa.idc.driversapp.data.db.tasks

import android.location.Location
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType
import sa.idc.driversapp.domain.entities.driverTasks.Order
import java.util.*

@StorIOSQLiteType(table = OrderEntry.Table.NAME)
data class OrderEntry @StorIOSQLiteCreator constructor(
        @StorIOSQLiteColumn(key = true, name = Table.Columns.ID) val id: Long,
        @StorIOSQLiteColumn(name = Table.Columns.DUE_DATE) val dueDate: Long,
        @StorIOSQLiteColumn(name = Table.Columns.ORIGIN_LAT) val originLat: Double,
        @StorIOSQLiteColumn(name = Table.Columns.ORIGIN_LNG) val originLng: Double,
        @StorIOSQLiteColumn(name = Table.Columns.DESTINATION_LAT) val destinationLat: Double,
        @StorIOSQLiteColumn(name = Table.Columns.DESTINATION_LNG) val destinationLng: Double,
        @StorIOSQLiteColumn(name = Table.Columns.STATUS) val status: String,
        @StorIOSQLiteColumn(name = Table.Columns.WEIGHT) val weight: Double,
        @StorIOSQLiteColumn(name = Table.Columns.WORTH) val worth: Long,
        @StorIOSQLiteColumn(name = Table.Columns.DESCRIPTION) val description: String,
        @StorIOSQLiteColumn(name = Table.Columns.CUSTOMER_PHONE_NUMBER) val customerPhoneNumber: String
) {
    object Table {
        const val NAME: String = "orders"

        object Columns {
            const val ID = "_id"
            const val DUE_DATE = "due_date"
            const val ORIGIN_LAT = "origin_lat"
            const val ORIGIN_LNG = "origin_lng"
            const val DESTINATION_LAT = "destination_lat"
            const val DESTINATION_LNG = "destination_lng"
            const val STATUS = "status"
            const val WEIGHT = "weight"
            const val WORTH = "worth"
            const val DESCRIPTION = "description"
            const val CUSTOMER_PHONE_NUMBER = "customer_phone_number"
        }

        const val ON_CREATE: String = """
            CREATE TABLE $NAME (
                ${Columns.ID} INTEGER NOT NULL PRIMARY KEY,
                ${Columns.DUE_DATE} INTEGER,
                ${Columns.ORIGIN_LAT} REAL,
                ${Columns.ORIGIN_LNG} REAL,
                ${Columns.DESTINATION_LAT} REAL,
                ${Columns.DESTINATION_LNG} REAL,
                ${Columns.STATUS} STRING,
                ${Columns.WEIGHT} REAL,
                ${Columns.WORTH} INTEGER,
                ${Columns.DESCRIPTION} STRING,
                ${Columns.CUSTOMER_PHONE_NUMBER} STRING
            )
        """
    }

    constructor(order: Order): this(
            order.id, order.dueDate.time, order.origin.latitude, order.origin.longitude,
            order.destination.latitude, order.destination.longitude, order.status.toString(),
            order.weight, order.worth, order.description, order.customerContacts
    )

    fun toEntity() = Order(
            id, Date(dueDate),
            createLocation(originLat, originLng), createLocation(destinationLat, destinationLng),
            Order.Status.valueOf(status), weight, worth, description, customerPhoneNumber
    )

    private fun createLocation(lat: Double, lng: Double) = Location("").apply {
        latitude = lat
        longitude = lng
    }
}