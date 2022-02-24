package com.example.allsmokeme.cart

import java.math.BigDecimal

//интерфейс в котором у нас храниться имя и цена товара
interface Saleable {
    val price: BigDecimal?
    val name_eng: String?
}