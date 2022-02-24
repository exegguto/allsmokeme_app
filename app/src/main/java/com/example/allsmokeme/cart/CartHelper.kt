package com.example.allsmokeme.cart

object CartHelper {
    private var cartEntity: CartEntity? = CartEntity()
    val cart: CartEntity?
        get() {
            if (cartEntity == null) { cartEntity = CartEntity() }
            return cartEntity
        }

    val cartItems: List<CartItemsEntityModel>
        get() {
            val cartItems: MutableList<CartItemsEntityModel> = ArrayList()
            val itemMap: Map<Saleable, Long?> = cart!!.itemWithQuantity
            for ((key, value) in itemMap) {
                val cartItem = CartItemsEntityModel()
                cartItem.product = key as ProductEntityModel?
                cartItem.quantity = value!!
                cartItems.add(cartItem)
            }
            return cartItems
        }
}