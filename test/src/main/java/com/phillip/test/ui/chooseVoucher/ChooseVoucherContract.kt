package com.phillip.test.ui.chooseVoucher

import com.phillip.test.models.Voucher


class ChooseVoucherContract {
    /**
     * Represents the View in MVP.
     */
    interface View {
        fun sendAppyVoucherSuccess(data: Voucher)
        fun sendListVoucherSuccess(listVoucher: ArrayList<Voucher>)
        fun sendFailed(message: String)
    }

    /**
     * Represents the Presenter in MVP.
     */
    interface Presenter {
        fun checkVoucher(
            productId: String,
            orderQuality: Int,
            voucherCode: String,
            customerId: String
        )

        fun getListVoucher(mUserId: String)
    }
}