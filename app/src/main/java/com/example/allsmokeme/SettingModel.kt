package com.example.allsmokeme.rentmix

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SettingModel( //модель микса
    var prise1: Int = 0,
    var prise2: Int = 0,
    var priseScore: Int = 0, //стоимость забитой чаши
    var priseScorePineaple: Int = 0 //стоимость забитой чаши
): Parcelable {}