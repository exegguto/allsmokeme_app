package com.example.allsmokeme.rentlogistic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class RentAddressModel( //модель адреса доставки
    var addressID: String = "",
    var Sity: String = "",
    var Street: String = "",
    var House: String? = "",
    var Entrance: String = "",
    var Floor: String = "",
    var Apartment: String = "",
    var Intercom: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var price: Int = 0
): Parcelable {}