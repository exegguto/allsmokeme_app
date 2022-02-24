package com.example.allsmokeme.chat

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.BaseFragment
import com.example.allsmokeme.IOnBackPressed
import com.example.allsmokeme.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import java.util.*


class MessagesFragment : BaseFragment(), IOnBackPressed {

    private var recyclerView: RecyclerView? = null
    private val chatMessage: ArrayList<ChatMessage> = ArrayList()
    private val db = FirebaseFirestore.getInstance()
    private var dbSnapshot: ListenerRegistration? = null
    private var chatID: String = ""
    private var userUid: String = ""
    private var nameChat: String = ""

    override val viewId: Int = R.layout.fragment_messages

    override fun onViewCreated(view: View?) {

        val bundle: Bundle? = arguments
        chatID = bundle?.getString("chatID").toString()
        userUid = bundle?.getString("userUid").toString()
        nameChat = bundle?.getString("nameChat").toString()


        val toolbarTextView: TextView? = activity?.findViewById(R.id.toolBarText)
        toolbarTextView?.text = nameChat
        toolbarTextView?.visibility = View.VISIBLE


        var dbtemp = db.collection("users")
            .document(userUid)
            .collection(chatID)
            .orderBy("messageTime", Query.Direction.DESCENDING)

        dbSnapshot = dbtemp.addSnapshotListener { snapshots, e ->
                if (e != null) {
//                    Log.w(TAG, "listen:error", e)
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            val chatsModel = dc.document.toObject<ChatMessage>(
                                ChatMessage::class.java
                            )
                            chatsModel.userUid = userUid
                            chatsModel.messageUid = dc.document.id
                            chatMessage.add(chatsModel)
                        }
                        DocumentChange.Type.MODIFIED -> {
                        }
                        DocumentChange.Type.REMOVED -> {
                            var goodDelete = true
                            var num = 0
                            while (goodDelete || num < chatMessage.size - 1) {
                                if (chatMessage[num].messageUid == dc.document.id) {
                                    chatMessage.removeAt(num)
                                    recyclerView!!.adapter?.notifyItemRemoved(num)
                                    goodDelete = false
                                }
                                num++
                            }
                        }
                    }
                }
                if(chatMessage.size>1)
                    if(chatMessage[0].messageTime!! > chatMessage[1].messageTime!!)
                        chatMessage.reverse()

                val chatsRecyclerAdapter = MessageRecyclerAdapter(
                    requireContext(),
                    chatMessage,
                    chatID
                )

                recyclerView = view?.findViewById(R.id.recyclerView)
                val linearLayoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                //linearLayoutManager.stackFromEnd = false  //это изменяет направление снизу вверх
                recyclerView!!.layoutManager = linearLayoutManager
                recyclerView!!.adapter = chatsRecyclerAdapter
                recyclerView!!.scrollToPosition(chatsRecyclerAdapter.itemCount - 1)
                recyclerView!!.adapter?.notifyDataSetChanged()
            }

        val button: ImageButton = requireView().findViewById(R.id.button)

        button.setOnClickListener {
            val input: EditText = requireView().findViewById(R.id.input)

            if (input.text.toString() != "") {
                val time = Timestamp.now()

                val chat = hashMapOf(
                    "nameChat" to nameChat,
                    "lastMessage" to input.text.toString(),
                    "timestamp" to time
                )
                db.collection("users")
                    .document(userUid)
                    .collection("chats")
                    .document(chatID)
                    .set(chat)

                db.collection("users")
                    .document("operator")
                    .collection("chats")
                    .document(chatID)
                    .set(chat)

                val newMessageHash = hashMapOf(
                    "messageUser" to userUid,
                    "messageText" to input.text.toString(),
                    "messageTime" to time
                )

                val newMessage = db.collection("users")
                    .document(userUid)
                    .collection(chatID)
                    .document()

                newMessage.set(newMessageHash)
                val messageID = newMessage.id

                db.collection("users")
                    .document("operator")
                    .collection(chatID)
                    .document(messageID)
                    .set(newMessageHash)

                // Clear the input
                input.setText("")
            } else {
                // если есть текст, то здесь другой код
            }
        }
    }

    override fun onBackPressed(): Boolean {
        dbSnapshot?.remove()
//        val frag = parentFragmentManager.findFragmentByTag("MessagesFragment")
//        frag.
//        parentFragmentManager.popBackStack("MessagesFragment")
        val fragment = ChatsFragment()
        val arguments: Bundle? = arguments
        fragment.arguments = arguments
        parentFragmentManager.beginTransaction()
            .replace(R.id.container_chats, fragment, "ChatsFragment")
            .commit()

        return true
    }
}