package sa.idc.driversapp.presentation.supportChat.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_support_chat.*
import sa.idc.driversapp.R
import sa.idc.driversapp.domain.entities.support.SupportChatMessage
import sa.idc.driversapp.presentation.supportChat.presenter.SupportChatPresenter
import sa.idc.driversapp.presentation.supportChat.presenter.SupportChatView
import android.view.inputmethod.EditorInfo
import sa.idc.driversapp.util.AppPermissions


class SupportChatActivity : AppCompatActivity(), SupportChatView {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SupportChatActivity::class.java))
        }

        var isInForeground: Boolean = false
    }

    private object RequestCodes {
        const val CALL_PERMISSION = 0
    }

    private val presenter = SupportChatPresenter(this)

    private val messagesList = ArrayList<SupportChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_chat)

        initChat()

        presenter.getMessages()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.support_chat_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.call_support_item -> {
                presenter.callToOperator()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun callSupportNumber(number: String) {
        if (!AppPermissions.permissionIsGranted(Manifest.permission.CALL_PHONE)) {
            AppPermissions.requestPermission(
                    this,
                    Manifest.permission.CALL_PHONE,
                    RequestCodes.CALL_PERMISSION
            )
        } else {
            startActivity(Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:$number")))
        }
    }


    private fun initChat() {
        rv_support_chat.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        rv_support_chat.adapter = SupportChatMessagesAdapter(messagesList)

        iv_send_button.setOnClickListener { sendMessage() }

        til_message_field.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage()
            }
            true
        }
    }

    private fun sendMessage() {
        til_message_field.editText?.text?.toString()
                ?.trim()
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
        iv_send_button.apply {
            isEnabled = true
            visibility = View.VISIBLE
        }

        pb_send_progress.visibility = View.GONE

        Toast.makeText(this, R.string.send_message_error, Toast.LENGTH_LONG).show()
    }

    override fun addNewMessage(message: SupportChatMessage) {
        messagesList.add(message)
        rv_support_chat.apply {
            adapter?.notifyDataSetChanged()
            scrollToPosition(messagesList.size - 1)
        }
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
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
