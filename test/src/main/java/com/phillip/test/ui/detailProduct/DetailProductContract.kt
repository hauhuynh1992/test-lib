package com.phillip.test.ui.detailProduct

import com.phillip.test.models.Product
import com.phillip.test.models.User


class DetailProductContract {
    /**
     * Represents the View in MVP.
     */
    interface View {
        fun sendProfileSuccess(user: User)
        fun sendCheckCartSuccess(isAddToCart: Boolean)
        fun sendProductSuccess(product: Product)
        fun sendAddWishListSuccess(isFav: Boolean)
        fun sendDeleteWishListSuccess(isFav: Boolean)
        fun sendCheckWishListSuccess(isFav: Boolean)
        fun sendFailed(message: String)
        fun showProgress()
        fun hideProgress()
    }

    /**
     * Represents the Presenter in MVP.
     */
    interface Presenter {
        fun getProfile(mFptId: String, phone: String)
        fun checkCart(userId: String, productId: String)
        fun getProduct(mProductId: String)
        fun addToWishList(productId: String, userId: String)
        fun deleteToWishList(productId: String, userId: String)
        fun checkWishList(productId: String, userId: String)
    }

}