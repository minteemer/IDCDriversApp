package sa.idc.driversapp.presentation.supportChat.presenter

import sa.idc.driversapp.domain.entities.support.SupportChatMessage

interface SupportChatView {
    fun showMessages(messages: List<SupportChatMessage>)
    fun startSendMessageProgress()
    fun onMessageSent()
    fun onMessageError()
    fun addNewMessage(message: SupportChatMessage)
    /**Call to support */
    fun callSupportNumber(number: String)
}