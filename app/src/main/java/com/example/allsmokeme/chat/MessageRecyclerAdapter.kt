package com.example.allsmokeme.chat;

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.allsmokeme.OnHomeFragmentDataListener
import com.example.allsmokeme.R
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat


class MessageRecyclerAdapter(
  private val context: Context,
  private val chatMessage: ArrayList<ChatMessage>,
  private val chatID: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val view: View =
      LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
    return ReceiveViewHolder(view)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val viewHolder = holder as ReceiveViewHolder
    val params = FrameLayout.LayoutParams(
      FrameLayout.LayoutParams.WRAP_CONTENT,
      FrameLayout.LayoutParams.WRAP_CONTENT
    )
    val scale: Float = context.resources.displayMetrics.density

    var timeMessage = ""
    if(position >= 1) timeMessage = SimpleDateFormat("yyyy.MM.dd").format(chatMessage[position - 1].messageTime?.toDate()!!)
    val timeMessageThis = SimpleDateFormat("yyyy.MM.dd").format(chatMessage[position].messageTime?.toDate()!!)

    viewHolder.message_time.text = SimpleDateFormat("HH:mm").format(chatMessage[position].messageTime?.toDate()!!)
    viewHolder.message_text.text = chatMessage[position].messageText

    if(chatMessage[position].messageUser == chatMessage[position].userUid ){
      // convert the DP into pixel

      val pixel5 = (5*scale + 0.5f).toInt()
      var pixel1 = pixel5
      if(position > 0)
        if(chatMessage[position - 1].messageUser == chatMessage[position].messageUser &&
          timeMessage == timeMessageThis)
          pixel1 = (2 * scale + 0.5f).toInt()

      params.setMargins((62 * scale + 0.5f).toInt(), pixel1, pixel5, 0)

      params.gravity = Gravity.END
      viewHolder.message_chat.layoutParams = params
      viewHolder.message_chat.setBackgroundResource(R.drawable.chat_messages_right)
    }

    if (timeMessage == timeMessageThis) viewHolder.message_day.visibility = View.GONE
    else{
      viewHolder.message_day.text = SimpleDateFormat("EEE, d MMM").format(chatMessage[position].messageTime?.toDate()!!)
      viewHolder.message_day.visibility = View.VISIBLE
      params.topMargin = (30 * scale + 0.5f).toInt()
      viewHolder.message_chat.layoutParams = params
    }
  }

  override fun getItemCount(): Int { return chatMessage.size }

  inner class ReceiveViewHolder(itemView: View?) :
    RecyclerView.ViewHolder(itemView!!), View.OnLongClickListener {
    var message_day: TextView =  itemView!!.findViewById(R.id.message_day)
    var message_time: TextView =  itemView!!.findViewById(R.id.message_time)
    var message_text: TextView =  itemView!!.findViewById(R.id.message_text)
    var message_chat: LinearLayout =  itemView!!.findViewById(R.id.message_chat)

    init {
      message_chat.setOnLongClickListener(this)
    }

    override fun onLongClick(view: View): Boolean {
      val popupMenu = PopupMenu(context, message_chat)
      popupMenu.inflate(R.menu.options_messages)
      popupMenu.setOnMenuItemClickListener {
        when (it.itemId) {
          R.id.delete -> {
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
              .document(chatMessage[adapterPosition].userUid)
              .collection(chatID)
              .document(chatMessage[adapterPosition].messageUid)
              .delete()

            db.collection("users")
              .document("operator")
              .collection(chatID)
              .document(chatMessage[adapterPosition].messageUid)
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
      Toast.makeText(view.context, "long click", Toast.LENGTH_SHORT).show()

      return true
    }
  }
}