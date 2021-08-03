package com.phillip.test.ui.detailCart

import com.phillip.test.models.Product


class DetailCartContract {
    /**
     * Represents the View in MVP.
     */
    interface View {
        fun sendSaveCartSuccess()
        fun sendFailed(message: String)
    }

    /**
     * Represents the Presenter in MVP.
     */
    interface Presenter {
        fun saveCart(userId: String, product: Product)
    }
}