package com.example.allsmokeme.rentcart

import android.location.Address
import android.os.Parcel
import android.os.Parcelable
import com.example.allsmokeme.renthookah.RentHookahModel
import com.example.allsmokeme.rentmix.RentMixModel
import com.example.allsmokeme.rentset.RentSetEndModel

class RentCartModel (
    var hookah: RentHookahModel? = null, //Тип кальяна
    var mix: ArrayList<RentMixModel?> = ArrayList(), //Массив миксов
    var rent1SetEndModel: ArrayList<RentSetEndModel?> = ArrayList(), // массив допов чекбокс
    var rent2SetEndModel: ArrayList<RentSetEndModel?> = ArrayList(), //Массив допов регулируемые
    var comments: String? = null,
    var sumOne: Long = 0,
    var sumAll: Long = 0,
    var delivery: Boolean? = null, //Доставка/самовывоз     <---может убрать, а самовывоз определять, есть сумма доставки равна нулю
    var deliveryFast: Boolean? = null, //Доставка на ближайшее
//    var deliveryTime: Date? = null, //Время доставки
    var deliveryThere: Long = 0, //Доставить сумма
    var deliveryBack: Long = 0, // Забрать сумма
    var deliveryAddress: Address? = null //адрес доставки
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(RentHookahModel::class.java.classLoader),
        arrayListOf<RentMixModel?>().apply { parcel.readList(this, RentMixModel::class.java.classLoader)},
        arrayListOf<RentSetEndModel?>().apply { parcel.readList(this, RentSetEndModel::class.java.classLoader)},
        arrayListOf<RentSetEndModel?>().apply { parcel.readList(this, RentSetEndModel::class.java.classLoader)},
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
//        TODO("deliveryTime"),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readParcelable(Address::class.java.classLoader)
    ) {

    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(hookah, flags)
        parcel.writeList(mix as List<RentMixModel?>)
        parcel.writeList(rent1SetEndModel as List<RentSetEndModel?>)
        parcel.writeList(rent2SetEndModel as List<RentSetEndModel?>)
        parcel.writeString(comments)
        parcel.writeLong(sumOne)
        parcel.writeLong(sumAll)
        parcel.writeValue(delivery)
        parcel.writeValue(deliveryFast)
        parcel.writeLong(deliveryThere)
        parcel.writeLong(deliveryBack)
        parcel.writeParcelable(deliveryAddress, flags)

    }
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RentCartModel> {
        override fun createFromParcel(parcel: Parcel): RentCartModel {
            return RentCartModel(parcel)
        }

        override fun newArray(size: Int): Array<RentCartModel?> {
            return arrayOfNulls(size)
        }
    }
}
