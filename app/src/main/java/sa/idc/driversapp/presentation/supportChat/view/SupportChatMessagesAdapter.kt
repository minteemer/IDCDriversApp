package sa.idc.driversapp.presentation.supportChat.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import sa.idc.driversapp.R
import sa.idc.driversapp.domain.entities.support.SupportChatMessage

class SupportChatMessagesAdapter(private val messages: List<SupportChatMessage>)
    : RecyclerView.Adapter<SupportChatMessageViewHolder>() {

    override fun getItemCount(): Int = messages.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            SupportChatMessageViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.rv_fragment_chat_message,
                            parent,
                            false
                    )
            )

    override fun onBindViewHolder(holder: SupportChatMessageViewHolder, position: Int) {
        holder.attach(messages[position])
    }
}