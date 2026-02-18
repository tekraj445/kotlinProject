package com.example.project.Model


    data class ProductModel(
        var productId: String = "",
        var productName: String = "",
        var productImage: String = "",
        var description: String = "",
        var price: Double = 0.0,
        var quantity: Int = 0,
        var categoryId: String = "",

        ) {
        fun toMap(): Map<String, Any?> {
            return mapOf(
                "productName" to productName,
                "productImage" to productImage,
                "description" to description,
                "price" to price,
                "quantity" to quantity,
                "categoryId" to categoryId
            )

        }

    }
