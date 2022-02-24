package com.example.allsmokeme.chat

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.BaseFragment
import com.example.allsmokeme.R
import com.example.allsmokeme.rentmix.SettingModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query


class ChatsFragment : BaseFragment(), ChatsRecyclerAdapter.OnItemClickListener{

    private var recyclerView: RecyclerView? = null
    private val chatName: ArrayList<ChatsModel> = ArrayList()
    private val user = FirebaseAuth.getInstance().currentUser
    private var dbSnapshot: ListenerRegistration? = null
    var chatID = ""
    var userUid = ""
    var namechatLogin = ""
    val db = FirebaseFirestore.getInstance()
    val nameTwoUser = "operator"

    override val viewId: Int = R.layout.fragment_chats

    override fun onViewCreated(view: View?) {

        userUid = user!!.uid
        chatName.clear()

        var dbtemp = db.collection("users")
            .document(userUid)
            .collection("chats")
            .orderBy("timestamp", Query.Direction.DESCENDING)

        dbSnapshot = dbtemp.addSnapshotListener { snapshots, e ->

                if (e != null) {
//                    Log.w(TAG, "listen:error", e)
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            val chatsModel = dc.document.toObject<ChatsModel>(
                                ChatsModel::class.java
                            )
                            chatsModel.chatID = dc.document.id
                            chatsModel.nameUser = userUid
                            chatName.add(chatsModel)
                        }
                        DocumentChange.Type.MODIFIED -> {
                        }
                        DocumentChange.Type.REMOVED -> {
                            var goodDelete = true
                            var num = 0
                            while (goodDelete && num < chatName.size) {
                                if(chatName[num].chatID == dc.document.id) {
                                    chatName.removeAt(num)
                                    goodDelete = false
//                                    recyclerView!!.adapter?.notifyItemRemoved(num)
                                }else
                                num++
                            }
                        }
                    }
                }

                if(chatName.size == 0) oneAddChatOperator()

                val chatsRecyclerAdapter = ChatsRecyclerAdapter(
                    requireContext(),
                    chatName
                ).also {
                    it.setOnItemClickListener(this)
                }
                recyclerView = view?.findViewById(R.id.recyclerView)
                recyclerView!!.layoutManager = LinearLayoutManager(context)
                recyclerView!!.adapter = chatsRecyclerAdapter

            }
    }

    override fun onItemClick(chatsModel: ChatsModel?) {
        val messagesFragment = MessagesFragment()
        val bundle: Bundle? = arguments
        bundle?.putString("chatID", chatsModel!!.chatID)
        bundle?.putString("userUid", userUid)
        bundle?.putString("nameChat", chatsModel!!.nameChat)

        messagesFragment.arguments = bundle

        dbSnapshot?.remove()

        parentFragmentManager.beginTransaction()
            .replace(R.id.container_chats, messagesFragment,"MessagesFragment")
//            .addToBackStack("MessagesFragment")
            .commit()
    }

    private fun oneAddChatOperator(){
        //если чатов нет, то создали новый с оператором и добавили сообщение
        chatID = ""

        db.collection("users")
            .document(userUid)
            .collection("members")
            .whereEqualTo(nameTwoUser, true)
            .get()
            .addOnSuccessListener { documents ->

                if(documents.size()>0) {
                    for (document1 in documents) {
                        chatID = document1.id
                    }
                    oneAddChatOperatorNext()
                }else{
                    val hashMembers = hashMapOf(
                        userUid to true,
                        nameTwoUser to true
                    )
                    db.collection("users")
                        .document(userUid)
                        .collection("members")
                        .add(hashMembers)
                        .addOnSuccessListener { documentReference ->
                            chatID = documentReference.id
                            db.collection("users")
                                .document(nameTwoUser)
                                .collection("members")
                                .document(chatID)
                                .set(hashMembers)
                                .addOnSuccessListener { oneAddChatOperatorNext() }
                                .addOnFailureListener {}
                        }
                        .addOnFailureListener {}
                }
            }
            .addOnFailureListener {}
    }
    private fun oneAddChatOperatorNext(){
        db.collection("users")
            .document(nameTwoUser)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    namechatLogin = task.result?.get("login") as String
                    val time = Timestamp.now()
                    val chat = hashMapOf(
                        "nameChat" to namechatLogin,
                        "lastMessage" to "",
                        "timestamp" to time
                    )
                    val newMessageHash = hashMapOf(
                        "messageUser" to nameTwoUser,
                        "messageText" to "",
                        "messageTime" to time
                    )

                    if(nameTwoUser == "operator"){
                        chat["lastMessage"] = "Мы рады приветствовать Вас в нашей компании"
                        newMessageHash["messageText"] = "Мы рады приветствовать Вас в нашей компании"
                    }

                    var newChats = db.collection("users")
                        .document(userUid)
                        .collection("chats")
                        .document(chatID)

                    newChats.set(chat)

                    newChats = db.collection("users")
                        .document(nameTwoUser)
                        .collection("chats")
                        .document(chatID)

                    newChats.set(chat)


                    var newMessage = db.collection("users")
                        .document(userUid)
                        .collection(chatID)
                        .document()

                    newMessage.set(newMessageHash)

                    val messageID = newMessage.id

                    newMessage = db.collection("users")
                        .document(nameTwoUser)
                        .collection(chatID)
                        .document(messageID)
                    newMessage.set(newMessageHash)
                }
            }
    }
}