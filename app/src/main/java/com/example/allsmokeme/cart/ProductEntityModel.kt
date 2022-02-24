package com.example.allsmokeme.cart

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
class ProductEntityModel(
    var id: String? = null,
    override var name_eng: String? = null,
    override var price: BigDecimal? = null,
    var name_ru: String? = null,
    var weight: String? = null,
    var composition: String? = null,
    var photo: String? = null
) : Saleable, Parcelable {


    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        return if (other !is ProductEntityModel) false else id === other.id
    }

    override fun hashCode(): Int {
        val prime = 31
        var hash = 1
        hash = hash * prime + if (id == null) 0 else id.hashCode()
        hash = hash * prime + if (name_eng == null) 0 else name_eng.hashCode()
        hash = hash * prime + if (name_ru == null) 0 else name_ru.hashCode()
        hash = hash * prime + if (weight == null) 0 else weight.hashCode()
        hash = hash * prime + if (price == null) 0 else price.hashCode()
        hash = hash * prime + if (composition == null) 0 else composition.hashCode()
        return hash
    }

    companion object {
        private const val serialVersionUID = -4073256626483275668L
    }
}