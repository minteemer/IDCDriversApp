package sa.idc.driversapp.data.db.tasks

class TaskEntry(
        val id: Int,
        val orderId: Int,
        val status: String
) {
    object TasksTable {
        const val NAME: String = "tasks"

        object Columns {
            const val ID = "_id"
            const val OREDR_ID = "order_id"
            const val STATUS = "status"
        }

        const val ON_CREATE: String = """
        CREATE TABLE $NAME (
            ${Columns.ID} INTEGER NOT NULL PRIMARY KEY,
            ${Columns.ID} INTEGER NOT NULL,
            ${Columns.ID} INTEGER NOT NULL
        )
    """
    }
}