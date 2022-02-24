package com.example.allsmokeme.xz

import com.example.allsmokeme.product.ProductModel

object ProductsHelper {
    val productsList: List<ProductModel>
        get() {
            val productModels: MutableList<ProductModel> = ArrayList()
/*
            var model = ProductModel()
            model.id = 5678L
            model.title = "Honor 6A 2"
            model.description = "16Gb Grey"
            model.image =
                "https://github.com/dajver/CartWithBadgeExample/blob/master/images/honor.jpg?raw=true"
            model.price = 599
            productModels.add(model)
            model = ProductModel()
            model.id = 5672L
            model.title = "Meizu M5s"
            model.description = "32Gb Silver"
            model.image =
                "https://github.com/dajver/CartWithBadgeExample/blob/master/images/meizu.jpg?raw=true"
            model.price = 899
            productModels.add(model)
            model = ProductModel()
            model.id = 5673L
            model.title = "Apple iPhone SE"
            model.description = "32Gb Space Gray"
            model.image =
                "https://github.com/dajver/CartWithBadgeExample/blob/master/images/iphone.jpg?raw=true"
            model.price = 1199
            productModels.add(model)
            model = ProductModel()
            model.id = 5674L
            model.title = "Chuwi Hi10 Pro"
            model.image =
                "https://github.com/dajver/CartWithBadgeExample/blob/master/images/chuwi.jpg?raw=true"
            model.price = 2199
            productModels.add(model)
            model = ProductModel()
            model.id = 5675L
            model.title = "Fermi S7-plus"
            model.description = "10000mAh gray)"
            model.image =
                "https://github.com/dajver/CartWithBadgeExample/blob/master/images/batary.jpg?raw=true"
            model.price = 259
            productModels.add(model)
*/
            return productModels
        }
}