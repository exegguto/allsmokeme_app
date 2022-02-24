package com.example.allsmokeme.firm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.allsmokeme.R


class NewFirmFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val temp = inflater.inflate(R.layout.onefragment_firm, container, false)

        // добавляем в контейнер при помощи метода add()
        val fragment = FirmFragment()
        val arguments: Bundle? = arguments
        fragment.arguments = arguments

        parentFragmentManager.beginTransaction()
            .add(R.id.container, fragment, "FirmFragment")
            .commit()

        return temp
    }

    companion object {

        fun newInstance(activ: String): NewFirmFragment {
            val bundle = Bundle()
            bundle.putString("Activity", activ)
            val newFirmFragment = NewFirmFragment()
            newFirmFragment.arguments = bundle
            return newFirmFragment
        }
    }
}