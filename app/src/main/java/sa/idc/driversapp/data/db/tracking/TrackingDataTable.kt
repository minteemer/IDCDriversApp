package sa.idc.driversapp.data.db.tracking

object TrackingDataTable {
    const val NAME: String = "tracking_data"

    object Columns {
        const val ID = "_id"
        const val LNG = "lng"
        const val LAT = "ltd"
        const val TIME = "time"
    }

    const val ON_CREATE: String = """
        CREATE TABLE $NAME (
            ${Columns.ID} INTEGER NOT NULL PRIMARY KEY,
            ${Columns.LNG} REAL,
            ${Columns.LAT} REAL,
            ${Columns.TIME} INTEGER
        )
    """
}