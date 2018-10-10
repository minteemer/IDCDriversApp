package sa.idc.driversapp.data.db.tracking

import sa.idc.driversapp.data.db.BaseTable

object TrackingDataTable : BaseTable() {
    override val name: String = "tracking_data"

    object Columns{
        const val ID = "_id"
        const val LNG = "lng"
        const val LTD = "ltd"
        const val time = "time"
    }

    override val onCreate: String = """
        CREATE TABLE $name ()
    """.trimIndent()

}