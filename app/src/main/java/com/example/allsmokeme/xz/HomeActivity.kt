
package com.example.allsmokeme.xz
/*
import com.example.allsmokeme.BaseActivity
import com.example.allsmokeme.R

class HomeActivity : BaseActivity() {
    override fun getViewId(): Int { return R.layout.onefragment_home }
    override fun onCreateView() {}
}*/
/*
      params.setMargins(10, 5, 10, 5)
      params.addRule(RelativeLayout.ALIGN_PARENT_END)
      params.removeRule(RelativeLayout.ALIGN_PARENT_START)
      params.addRule(RelativeLayout.START_OF, R.id.message_time)
      viewHolder.message_text.setLayoutParams(params)

      params = RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.WRAP_CONTENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT
      )
      params.setMargins(10, 5, 10, 5)
      params.removeRule(RelativeLayout.ALIGN_PARENT_END)
      params.addRule(RelativeLayout.ALIGN_PARENT_START)
      viewHolder.message_time.setLayoutParams(params)

      params = RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.WRAP_CONTENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT
      )
*/

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
/*        var toolbarText = ""
        db.collection("users")
            .document(userUid)
            .collection("members")
            .document(chatID)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list: MutableList<String> = ArrayList()
                    val map: Map<String, Any?> = task.result?.data as Map<String, Any?>
                    for ((key) in map) {
                        list.add(key)
                        if(key != userUid) toolbarText = key
                    }
                    val toolbarTextView: TextView? = activity?.findViewById(R.id.toolBarText)
                    toolbarTextView?.text = toolbarText
                    toolbarTextView?.visibility = View.VISIBLE
                }
            }*/  //здесь мы получали массив из документа с неизвестным названием значений документа
/*
        db.collection("users")
            .document(userUid)
            .collection(chatID)
            .limit(15)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val chatsModel = document.toObject<ChatMessage>(
                        ChatMessage::class.java
                    )
                    chatsModel.userUid = userUid
                    chatMessage.add(chatsModel)
                    chatMessage.reverse()

                    val chatsRecyclerAdapter = MessageRecyclerAdapter(
                        requireContext(),
                        chatMessage
                    )
                    recyclerView = view?.findViewById(R.id.recyclerView)
                    val linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
                    //linearLayoutManager.stackFromEnd = false  //это изменяет направление снизу вверх
                    recyclerView!!.layoutManager = linearLayoutManager
                    recyclerView!!.adapter = chatsRecyclerAdapter
                    recyclerView!!.adapter?.notifyDataSetChanged()

                }
            }
            .addOnFailureListener { //exception -> textView.setText("Error getting documents: "
            }
*/