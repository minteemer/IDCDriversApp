package sa.idc.driversapp.data.db.tasks


data class OrderEntry(
        val id: Int,
        val dueDate: Long,
        val originLat: Double,
        val originLng: Double,
        val destinationLat: Double,
        val destinationLng: Double,
        val status: String,
        val weight: Double,
        val worth: Long,
        val description: String,
        val customer_phone_number: String
) {
    object Table {
        const val NAME: String = "orders"

        object Columns {
            const val ID = "_id"
        }

        const val ON_CREATE: String = """
        CREATE TABLE $NAME (
            ${Columns.ID} INTEGER NOT NULL PRIMARY KEY,
        )
    """
    }
}