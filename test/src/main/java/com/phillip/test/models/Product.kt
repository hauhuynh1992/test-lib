package com.phillip.test.models

data class Product(
    var uid: String = "",
    var sku_id: String = "",
    var name: String = "",
    var pricing: Pricing = Pricing(),
    var image_cover: String = "",
    var quantity: Int = 0,
    var supplier: Supplier = Supplier(),
    var brandShops: ArrayList<BrandShop> = arrayListOf(),
    var shortDescriptions: ShortDescriptions = ShortDescriptions(),
)

data class Supplier(
    var uid: String = "",
    var name: String = "Đang cập nhật",
    var shipping_time: Int = -1,
)

data class ShortDescriptions(
    var short_des_1: String = "",
    var short_des_1_color: String = "",
    var short_des_2: String = "",
    var short_des_2_color: String = "",
    var short_des_3: String = "",
    var short_des_3_color: String = "",
    var short_des_4: String = "",
    var short_des_4_color: String = "",
)

data class Pricing(
    var price_with_vat: Double = 0.0,
    var listed_price_with_vat: Double = 0.0
)

data class BrandShop(
    var uid: String = "",
    var display_name_detail: String = "",
    var image_logo: String = "",
    var skin_image: String = "",
    var product_counts: Int = 0
)