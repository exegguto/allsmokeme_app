package com.example.allsmokeme

import com.example.allsmokeme.renthookah.RentHookahModel
import com.example.allsmokeme.rentmix.RentMixModel
import com.example.allsmokeme.rentmix.SettingModel
import com.example.allsmokeme.rentset.RentSetEndModel

interface OnHomeFragmentDataListener {
    fun onOpenMixRentFragment(rentHookahModel: RentHookahModel?)
    fun delMixOnClick(listMixTemp: ArrayList<RentMixModel?>, num: Int)
    fun fragmentListMix(listMixTemp: ArrayList<RentMixModel?>)
    fun reCalcRent1Set(listMixTemp: ArrayList<RentSetEndModel?>)
    fun reCalcRent2Set(listMixTemp: ArrayList<RentSetEndModel?>)
    fun rentListMix(listMixTemp: ArrayList<RentMixModel?>)
    //fun addMix()
    fun getSettingsFirebase(): SettingModel
}

interface IOnBackPressed {
    fun onBackPressed(): Boolean
}