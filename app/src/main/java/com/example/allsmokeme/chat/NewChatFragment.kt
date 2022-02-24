package com.example.allsmokeme.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.allsmokeme.R


class NewChatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val temp = inflater.inflate(R.layout.onefragment_chat, container, false)

        // добавляем в контейнер при помощи метода add()
        val fragment = ChatsFragment()
        val arguments: Bundle? = arguments
        fragment.arguments = arguments

        parentFragmentManager.beginTransaction()
            .add(R.id.container_chats, fragment, "ChatsFragment")
            .commit()

        return temp
    }

    companion object {

        fun newInstance(): NewChatFragment {
            val bundle = Bundle()
            val chatFragment = NewChatFragment()
            chatFragment.arguments = bundle
            return chatFragment
        }
    }
}