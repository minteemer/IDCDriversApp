package sa.idc.driversapp.data.db.support

import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType
import sa.idc.driversapp.domain.entities.support.SupportChatMessage

@StorIOSQLiteType(table = SupportChatMessageEntry.Table.NAME)
data class SupportChatMessageEntry @StorIOSQLiteCreator constructor(
        @StorIOSQLiteColumn(name = Table.Columns.NAME) val name: String,
        @StorIOSQLiteColumn(name = Table.Columns.TEXT) val text: String,
        @StorIOSQLiteColumn(name = Table.Columns.TIMESTAMP) val timestamp: Long,
        @StorIOSQLiteColumn(key = true, name = Table.Columns.ID) val id: Long? = null
) {

    object Table {
        const val NAME: String = "support_chat_messages"

        object Columns {
            const val ID = "_id"
            const val NAME = "name"
            const val TEXT = "text"
            const val TIMESTAMP = "timestamp"
        }

        const val ON_CREATE: String = """
            CREATE TABLE $NAME (
                ${Columns.ID} INTEGER NOT NULL PRIMARY KEY,
                ${Columns.NAME } STRING NOT NULL,
                ${Columns.TEXT } STRING NOT NULL,
                ${Columns.TIMESTAMP } INTEGER NOT NULL
            )
        """
    }

    constructor(message: SupportChatMessage) : this(message.name, message.text, message.timestamp)

    fun toDomainEntity() = SupportChatMessage(name, text, timestamp)
}