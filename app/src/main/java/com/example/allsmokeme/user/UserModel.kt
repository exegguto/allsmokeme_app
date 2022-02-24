package com.example.allsmokeme.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserModel( //данные пользователя
    var uid: String? = "",
    var userName: String? = "",
    var userPhone: String? = ""
): Parcelable {}