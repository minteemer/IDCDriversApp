package sa.idc.driversapp.presentation.supportChat.view

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.rv_fragment_chat_message.view.*
import sa.idc.driversapp.domain.entities.support.SupportChatMessage
import sa.idc.driversapp.repositories.preferences.AppPreferences
import sa.idc.driversapp.util.DateFormats


class SupportChatMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun attach(message: SupportChatMessage) {
        itemView.tv_message.text = message.text
        itemView.tv_name.text = message.name
        itemView.tv_time.text = DateFormats.defaultDateTime.format(message.date)

        if (itemView is LinearLayout){
            itemView.gravity = if (message.name == AppPreferences.instance.driverName)
                Gravity.END
            else
                Gravity.START
        }


    }
}