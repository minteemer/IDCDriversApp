package sa.idc.driversapp.presentation.supportChat.view

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.rv_fragment_chat_message.view.*
import sa.idc.driversapp.domain.entities.support.SupportChatMessage

class SupportChatMessageViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun attach(message: SupportChatMessage) {
        view.tv_message.text = message.text
    }
}