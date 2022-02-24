package com.example.allsmokeme.rentmix

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class RentMixModel( //модель микса
    var mix1: String? = "Выбрать вкус",
    var mix2: String? = "Выбрать вкус",
    var mix3: String? = "Выбрать вкус",
    var priseMix1: Int = 0,
    var priseMix2: Int = 0,
    var priseMix3: Int = 0,
    var switch1: Boolean = true, //простой
    var switch2: Boolean = false, //прем
    var prise1: Int = 120,
    var prise2: Int = 350,
    var score: Boolean = false, //нужно ли забить чашу
    var priseScore: Int = 0, //стоимость забитой чаши
    var scorePineaple: Boolean = false, //нужно ли забить чашу
    var priseScorePineaple: Int = 0 //стоимость забитой чаши
): Parcelable {}