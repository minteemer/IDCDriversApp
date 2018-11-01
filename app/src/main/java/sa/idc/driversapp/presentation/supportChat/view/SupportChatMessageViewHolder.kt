package sa.idc.driversapp.presentation.supportChat.view

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.rv_fragment_chat_message.view.*
import sa.idc.driversapp.domain.entities.support.SupportChatMessage
import sa.idc.driversapp.repositories.preferences.AppPreferences
import sa.idc.driversapp.util.DateFormats

class SupportChatMessageViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun attach(message: SupportChatMessage) {
        view.tv_message.text = message.text
        view.tv_name.text = message.name
        view.tv_time.text = DateFormats.defaultDateTime.format(message.date)

        if (message.name == AppPreferences.instance.driverName && view is LinearLayout){
            view.gravity = Gravity.END
        }
    }
}