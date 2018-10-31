package sa.idc.driversapp.presentation.supportChat.view

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_support_chat.*
import sa.idc.driversapp.R
import sa.idc.driversapp.domain.entities.support.SupportChatMessage
import sa.idc.driversapp.presentation.supportChat.presenter.SupportChatPresenter
import sa.idc.driversapp.presentation.supportChat.presenter.SupportChatView

class SupportChatActivity : AppCompatActivity(), SupportChatView {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SupportChatActivity::class.java))
        }

        var isInForeground: Boolean = false
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
        rv_support_chat.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        rv_support_chat.adapter = SupportChatMessagesAdapter(messagesList)

        iv_send_button.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        til_message_field.editText?.text?.toString()
                ?.takeIf { it.isNotEmpty() }
                ?.let { presenter.sendMessage(it) }
    }

    override fun showMessages(messages: List<SupportChatMessage>) {
        messagesList.apply {
            clear()
            addAll(messages)
        }
        rv_support_chat.apply {
            adapter?.notifyDataSetChanged()
            scrollToPosition(messagesList.size - 1)
        }
    }

    override fun startSendMessageProgress() {
        iv_send_button.apply {
            isEnabled = false
            visibility = View.INVISIBLE
        }
        pb_send_progress.visibility = View.VISIBLE
    }

    override fun onMessageSent() {
        til_message_field.editText?.editableText?.clear()

        iv_send_button.apply {
            isEnabled = true
            visibility = View.VISIBLE
        }

        pb_send_progress.visibility = View.GONE
    }

    override fun onMessageError() {
        Toast.makeText(this, R.string.send_message_error, Toast.LENGTH_LONG).show()
    }

    override fun addNewMessage(message: SupportChatMessage) {
        messagesList.add(message)
        rv_support_chat.apply {
            adapter?.notifyDataSetChanged()
            scrollToPosition(messagesList.size - 1)
        }
    }


    override fun onResume() {
        super.onResume()
        isInForeground = true
    }

    override fun onPause() {
        isInForeground = false
        super.onPause()
    }
}
