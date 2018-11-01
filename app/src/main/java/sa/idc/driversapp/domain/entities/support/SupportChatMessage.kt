package sa.idc.driversapp.domain.entities.support

import java.util.*

data class SupportChatMessage(
        val name: String,
        val text: String,
        val timestamp: Long
) {
    val date: Date
        get() = Date(timestamp)
}