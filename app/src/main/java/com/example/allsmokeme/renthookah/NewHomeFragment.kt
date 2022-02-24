package com.example.allsmokeme.renthookah

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.allsmokeme.R
import com.example.allsmokeme.CsvReader

class NewHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val temp = inflater.inflate(R.layout.onefragment_home, container, false)
        val csvLoad: Button = temp.findViewById(R.id.csvLoad)
        csvLoad.setOnClickListener {
            val fileReader = CsvReader(requireContext())
            fileReader.read("123.csv")
        }

        return temp
    }

    companion object {
        fun newInstance(): NewHomeFragment {
            val bundle = Bundle()
            val newHomeFragment = NewHomeFragment()
            newHomeFragment.arguments = bundle
            return newHomeFragment
        }
    }
}