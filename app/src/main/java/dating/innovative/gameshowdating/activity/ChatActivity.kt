package dating.innovative.gameshowdating.activity

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import dating.innovative.gameshowdating.R
import dating.innovative.gameshowdating.data.WebSocketHandler
import dating.innovative.gameshowdating.model.Message
import dating.innovative.gameshowdating.util.ChatAdapter
import dating.innovative.gameshowdating.util.PreferenceManagerClass
import dating.innovative.gameshowdating.util.SQLiteHelper
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : Activity() {

    private lateinit var messagesAdapter: ChatAdapter

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val messagesLayoutManager = LinearLayoutManager(this)
        messagesLayoutManager.reverseLayout = true
        chatRecyclerView.layoutManager = messagesLayoutManager
        messagesAdapter = ChatAdapter(listOf<Message>())
        chatRecyclerView.adapter = messagesAdapter
        chatRecyclerView.scrollToPosition(0)

        updateMessages()

        setUpSendMessage()
    }

    private fun setUpSendMessage() {
        val messages = mutableListOf<Message>()
        val dbHelper = SQLiteHelper.getSqLiteHelperInstance(applicationContext)!!
        chatSendMessageButton.setOnClickListener {
            if (!chatEditText.text.toString().isEmpty()) {
                val to = intent.getStringExtra("username")

                dbHelper.addMessageToConversationFromSelf(
                    PreferenceManagerClass.getUsername(applicationContext),
                    intent.getStringExtra("username"), chatEditText.text.toString()
                )

                val message = Message(
                    PreferenceManagerClass.getUsername(applicationContext), intent.getStringExtra("username"),
                    chatEditText.text.toString(), null
                )
                WebSocketHandler.instance.sendChatMessage(to, message.messageFromSelf, System.currentTimeMillis()) {
                    updateMessages()
                }
                messages.add(0, message)
                messagesAdapter.notifyDataSetChanged()
                chatRecyclerView.scrollToPosition(0)
                chatEditText.setText("")
            }
        }
    }

    private fun updateMessages() {
        val toUser = intent.getStringExtra("username")

        WebSocketHandler.instance.getMessages(PreferenceManagerClass.getUsername(this)) { messageMap ->
            val messageList = messageMap[toUser]?: listOf()
            messagesAdapter.messages = messageList
            runOnUiThread {
                messagesAdapter.notifyDataSetChanged()
            }
        }
    }
}