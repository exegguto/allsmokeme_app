package com.example.allsmokeme.renthookah

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class RentHookahModel (
    var id: String? = null,
    var tipe: String? = null,
    var price_empty: Long? = null,
    var foto: String? = null,
    var time: String? = null
): Parcelable{}