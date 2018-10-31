package sa.idc.driversapp.presentation.supportChat.view

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_support_chat.*
import sa.idc.driversapp.R
import sa.idc.driversapp.domain.entities.support.SupportChatMessage
import sa.idc.driversapp.presentation.supportChat.presenter.SupportChatPresenter
import sa.idc.driversapp.presentation.supportChat.presenter.SupportChatView

class SupportChatActivity : AppCompatActivity(), SupportChatView {

    companion object {
        fun start(context: Context){
            context.startActivity(Intent(context, SupportChatActivity::class.java))
        }
    }

    private val presenter = SupportChatPresenter(this)

    private val messagesList = ArrayList<SupportChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_chat)

        initChat()

        presenter.getMessages()
    }

    private fun initChat() {
        rv_support_chat.layoutManager = LinearLayoutManager(this)
        rv_support_chat.adapter = SupportChatMessagesAdapter(messagesList)
    }

    override fun showMessages(messages: List<SupportChatMessage>) {
        messagesList.apply {
            clear()
            addAll(messages)
        }
        rv_support_chat.adapter?.notifyDataSetChanged()
    }
}
