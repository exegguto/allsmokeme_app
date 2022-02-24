package com.example.allsmokeme.cart

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.util.*

@Parcelize
class CartEntity: Parcelable {
    private val cartItemMap: MutableMap<Saleable, Long?> = HashMap()
    var totalPrice = BigDecimal.ZERO
        private set
    var totalQuantity: Long = 0
        private set

    fun add(sellable: Saleable, quantity: Long) {
        if (cartItemMap.containsKey(sellable)) {
            cartItemMap[sellable] = cartItemMap[sellable]!! + quantity
        } else {
            cartItemMap[sellable] = quantity
        }
        totalPrice =
            totalPrice.add(sellable.price!!.multiply(BigDecimal.valueOf(quantity.toLong())))
        totalQuantity += quantity
    }

    @Throws(ProductNotFoundException::class, QuantityOutOfRangeException::class)
    fun update(sellable: Saleable, quantity: Long) {
        if (!cartItemMap.containsKey(sellable)) throw ProductNotFoundException()
        if (quantity < 0) throw QuantityOutOfRangeException("$quantity is not a valid quantity. It must be non-negative.")
        val productQuantity = cartItemMap[sellable]!!
        val productPrice =
            sellable.price!!.multiply(BigDecimal.valueOf(productQuantity.toLong()))
        cartItemMap[sellable] = quantity
        totalQuantity = totalQuantity - productQuantity + quantity
        totalPrice = totalPrice.subtract(productPrice)
            .add(sellable.price!!.multiply(BigDecimal.valueOf(quantity.toLong())))
    }

    @Throws(ProductNotFoundException::class, QuantityOutOfRangeException::class)
    fun remove(sellable: Saleable, quantity: Long) {
        if (!cartItemMap.containsKey(sellable)) throw ProductNotFoundException()
        val productQuantity = cartItemMap[sellable]!!
        if (quantity < 0 || quantity > productQuantity) throw QuantityOutOfRangeException("$quantity is not a valid quantity. It must be non-negative and less than the current quantity of the product in the shopping ic_cart.")
        if (productQuantity == quantity) {
            cartItemMap.remove(sellable)
        } else {
            cartItemMap[sellable] = productQuantity - quantity
        }
        totalPrice =
            totalPrice.subtract(sellable.price!!.multiply(BigDecimal.valueOf(quantity.toLong())))
        totalQuantity -= quantity
    }

    @Throws(ProductNotFoundException::class)
    fun remove(sellable: Saleable) {
        if (!cartItemMap.containsKey(sellable)) throw ProductNotFoundException()
        val quantity = cartItemMap[sellable]!!
        cartItemMap.remove(sellable)
        totalPrice =
            totalPrice.subtract(sellable.price!!.multiply(BigDecimal.valueOf(quantity.toLong())))
        totalQuantity -= quantity
    }

    fun clear() {
        cartItemMap.clear()
        totalPrice = BigDecimal.ZERO
        totalQuantity = 0
    }

    @Throws(ProductNotFoundException::class)
    fun getQuantity(sellable: Saleable?): Long? {
        if (!cartItemMap.containsKey(sellable)) throw ProductNotFoundException()
        return cartItemMap.get(sellable)
    }

    @Throws(ProductNotFoundException::class)
    fun getCost(sellable: Saleable): BigDecimal {
        if (!cartItemMap.containsKey(sellable)) throw ProductNotFoundException()
        return sellable.price!!.multiply(cartItemMap[sellable]?.let { BigDecimal.valueOf(it) })
    }

    val products: Set<Saleable> get() = cartItemMap.keys

    val itemWithQuantity: Map<Saleable, Long?>
        get() {
            val cartItemMap: MutableMap<Saleable, Long?> =
                HashMap()
            cartItemMap.putAll(this.cartItemMap)
            return cartItemMap
        }

    override fun toString(): String {
        val strBuilder = StringBuilder()
        for ((key, value) in cartItemMap) {
            strBuilder.append(
                String.format(
                    "Product: %s, Unit Price: %f, Quantity: %d%n",
                    key.name_eng,
                    key.price,
                    value
                )
            )
        }
        strBuilder.append(
            String.format(
                "Total Quantity: %d, Total Price: %f",
                totalQuantity,
                totalPrice
            )
        )
        return strBuilder.toString()
    }

    companion object { private const val serialVersionUID = 42L }
}