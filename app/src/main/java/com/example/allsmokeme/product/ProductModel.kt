package com.example.allsmokeme.product

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ProductModel (
    var price: Long = 0, //Цена пачки
    var priceMix: Long = 0, //Цена микса
    var id: String = "", //Случайное
    var firm: String = "", //Фирма
    var fortress : String = "", //крепость
    var name_eng: String = "", //Название англ
    var name_ru: String = "", //Название рус
    var weight : String = "", //Вес
    var description: String = "", //Описание
/*    var slicing: String = null, //Нарезка
    var smoke : String = null, //Дымность
    var heat_resistance: String = null, //Жаростойкость*/
    var country: String = "", //Страна производитель
    var nicotine : String = "", //Содержание никотина
    var composition: String = "", //Вкусы
/*
    var segment: String = null, //Ценовой сегмент
*/
    var availability : Long = 0, //Наличие
    var rating: Float = 0.0F,
    var ratingSum: Long = 0
/*
    var photo: String = null
*/
    ): Parcelable


/*
    var price: Long = null //Цена пачки
    var priceMix: Long = null //Цена микса
    var id: String = null //Случайное
    var firm: String = null //Фирма
    var fortress : String = null //крепость
    var name_eng: String = null //Название англ
    var name_ru: String = null //Название рус
    var weight : String = null //Вес
    var description: String = null //Описание
    var slicing: String = null //Нарезка
    var smoke : String = null //Дымность
    var heat_resistance: String = null //Жаростойкость
    var country: String = null //Страна производитель
    var nicotine : String = null //Содержание никотина
    var composition: String = null //Вкусы
    var segment: String = null //Ценовой сегмент
    var availability : Long = null //Наличие
    var rating: Float = null //Рейтинг
    var ratingSum: Long = null //Рейтинг
    var photo: String = null //ссылка на фото

*/

/*    constructor() {}
    constructor(id: String, firm: String, price: Long, priceMix: Long, fortress : String, name_eng: String,
                name_ru: String, weight: String, description: String, slicing: String,
                smoke: String, heat_resistance: String, country: String, nicotine: String,
                composition: String, segment: String, availability: Long, rating: Float, photo: String, ratingSum: Long) {

        this.price = price
        this.priceMix = priceMix
        this.id = id
        this.firm = firm
        this.fortress  = fortress
        this.name_eng = name_eng
        this.name_ru = name_ru
        this.weight = weight
        this.description  = description
        this.slicing = slicing
        this.smoke = smoke
        this.heat_resistance = heat_resistance
        this.country  = country
        this.nicotine = nicotine
        this.composition = composition
        this.segment = segment
        this.availability  = availability
        this.rating  = rating
        this.ratingSum  = ratingSum
        this.photo = photo
    }}*/

/*
class ProductModel {
    var id: Long = null
    var price: Long = null
    var image: String = null
    var title: String = null
    var description: String = null

}*/
