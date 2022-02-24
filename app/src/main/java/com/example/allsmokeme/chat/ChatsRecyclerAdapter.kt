package com.example.allsmokeme.chat;

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat

class ChatsRecyclerAdapter (
  private val context: Context,
  private val chatsModel: ArrayList<ChatsModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var onItemClickListener: OnItemClickListener? = null

  override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val view: View =
      LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
    return ReceiveViewHolder(view)
  }

  override fun onBindViewHolder( holder: RecyclerView.ViewHolder, position: Int ) {
    val viewHolder = holder as ReceiveViewHolder

    // вот здесь изменять формат времени

    viewHolder.message_user.text = chatsModel[position].nameChat
    viewHolder.message_time.text =  SimpleDateFormat("HH:mm").format(chatsModel[position].timestamp?.toDate()!!)
    viewHolder.message_text.text = chatsModel[position].lastMessage

/*когда нужно будет фото user
    val url: String = chatsModel[position].firmPhoto!!
    val storage = FirebaseStorage.getInstance()
    val gsReference = storage.getReferenceFromUrl(url)

    val ONE_MEGABYTE: Long = 1024 * 1024
    gsReference.getBytes(ONE_MEGABYTE)
      .addOnSuccessListener { bytes ->
        val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        viewHolder.photo.setImageBitmap(bmp)
        // Data for "images/island.jpg" is returned, use this as needed
      }.addOnFailureListener {
        // Handle any errors
      }
*/

  }

  override fun getItemCount(): Int { return chatsModel.size }

  inner class ReceiveViewHolder(itemView: View?) :
    RecyclerView.ViewHolder(itemView!!), View.OnLongClickListener {
    var message_user: TextView =  itemView!!.findViewById(R.id.message_user)
    var message_time: TextView =  itemView!!.findViewById(R.id.message_time)
    var message_text: TextView =  itemView!!.findViewById(R.id.message_text)
    var message_chat: RelativeLayout =  itemView!!.findViewById(R.id.message_chat)

    init {
      message_chat.setOnLongClickListener(this)

      message_chat.setOnClickListener {
        onItemClickListener!!.onItemClick(chatsModel.get(adapterPosition))
      }
    }
    override fun onLongClick(view: View): Boolean {
      val popupMenu = PopupMenu(context, message_chat)
      val position: Int = adapterPosition
      val chatID = chatsModel[adapterPosition].chatID
      val nameUser = chatsModel[adapterPosition].nameUser
      popupMenu.inflate(R.menu.options_messages)
      popupMenu.setOnMenuItemClickListener {
        when (it.itemId) {
          R.id.delete -> {
            val db = FirebaseFirestore.getInstance()

            db.collection("users")
              .document(nameUser)
              .collection(chatID)
              .get()
              .addOnSuccessListener { documents ->
                for (document1 in documents) {
                  db.collection("users")
                    .document(nameUser)
                    .collection(chatID)
                    .document(document1.id)
                    .delete()
                }
              }
              .addOnFailureListener { }

            db.collection("users")
              .document(nameUser)
              .collection("chats")
              .document(chatID)
              .delete()

/*
            chatMessage.removeAt(adapterPosition)
            notifyDataSetChanged()
*/
            true
          }
          else -> false
        }
      }

      popupMenu.show()
      return true
    }
  }

  fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
    this.onItemClickListener = onItemClickListener
  }

  interface OnItemClickListener { fun onItemClick(chatsModel: ChatsModel?) }
}