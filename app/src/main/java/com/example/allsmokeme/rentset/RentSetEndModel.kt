package com.example.allsmokeme.rentset

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class RentSetEndModel (
    var service: String? = "",
    var prise: Int = 0,
    var quantity: Int = 0, //количество
    var addMix: Boolean = true, //нужно ли добавлять микс
    var visibleSet: Boolean = true //нужно ли показывать этот пункт
): Parcelable {}