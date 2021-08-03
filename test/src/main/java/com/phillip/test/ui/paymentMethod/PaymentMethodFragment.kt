package com.phillip.test.ui.paymentMethod

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.phillip.test.R
import com.phillip.test.models.Product
import com.phillip.test.models.User
import com.phillip.test.models.Voucher
import com.phillip.test.utils.Functions
import kotlinx.android.synthetic.main.fragment_detail_product.*
import kotlinx.android.synthetic.main.fragment_payment_method.*
import kotlinx.android.synthetic.main.fragment_payment_method.price

class PaymentMethodFragment : Fragment(), PaymentMethodContract.View {

    var mListener: onPaymentMethodListener? = null
    private var mProduct: Product? = null
    private var mUser: User? = null
    var mVoucher: Voucher? = null
    private var presenter: PaymentMethodPresenter? = null
    private var btnChooseVoucher: RelativeLayout? = null
    private var tvBtnChooseVoucher: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = PaymentMethodPresenter(this@PaymentMethodFragment, it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.fragment_payment_method, container, false)
        btnChooseVoucher = mView.findViewById(R.id.bn_choose_voucher)
        tvBtnChooseVoucher = mView.findViewById(R.id.text_choose_voucher)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {

            mVoucher = Voucher(
                uid = it.getString("STR_VOUCHER_ID", ""),
                code = it.getString("STR_VOUCHER_CODE", ""),
                applied_value = it.getDouble("STR_VOUCHER_APPLY_VALUE", 0.0)
            )
        }

        bn_confirm?.setOnClickListener {
            var voucherCode = if (mVoucher == null) "" else mVoucher!!.code
            var voucherId = if (mVoucher == null) "" else mVoucher!!.uid

            presenter?.submitOrder(
                customerId = mUser!!.uid,
                name = mUser!!.name,
                phone = mUser!!.phone,
                voucherCode = voucherCode,
                voucherId = voucherId,
                requestType = 2,
                list = arrayListOf(mProduct!!)
            )

        }

        mProduct?.let {
            price?.text = Functions.formatMoney(it.pricing.price_with_vat * it.quantity)
        }
        btnChooseVoucher?.setOnClickListener {
            mProduct?.let {
                mListener?.onSelectVoucherClick(
                    product = it,
                    user = mUser!!
                )
            }
        }

        btnChooseVoucher?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                tvBtnChooseVoucher?.setTextColor(Color.WHITE)
            } else {
                tvBtnChooseVoucher?.setTextColor(Color.parseColor("#484848"))
            }
        }
    }

    private fun totalPrice(): String {
        var voucherValue = 0.0

        if (mVoucher != null) voucherValue = mVoucher!!.applied_value

        voucher?.text =
            if (voucherValue > 0) "-${Functions.formatMoney(voucherValue)}" else Functions.formatMoney(
                voucherValue
            )
        var total = 0.0
        mProduct?.let {
            total = it.pricing.price_with_vat * it.quantity - voucherValue
        }


        if (total >= freeShipValue) {
            ship?.text = "Miễn phí"
        } else {
            ship?.text = "Xác nhận sau"
        }

        return Functions.formatMoney(total)
    }

    override fun onResume() {
        super.onResume()

        Handler().postDelayed({
            bn_confirm?.requestFocus()
        }, 100)

        total_vat?.text = totalPrice()
        if (mVoucher != null) {
            tvBtnChooseVoucher?.text = mVoucher!!.code
        } else {
            tvBtnChooseVoucher?.text = "Chọn / nhập voucher"
        }
    }

    fun setListener(listenr: onPaymentMethodListener) {
        mListener = listenr
    }

    interface onPaymentMethodListener {
        fun onSubmitOrderSuccess(orderId: String, product: Product, totalPrice: String)
        fun onSelectVoucherClick(
            product: Product,
            user: User
        )
    }

    companion object {
        val freeShipValue = 200000

        @JvmStatic
        fun newInstance() =
            PaymentMethodFragment()
    }

    override fun sendOrderSuccess(orderId: String) {
        mListener?.onSubmitOrderSuccess(orderId, mProduct ?: Product(), totalPrice())
    }

    override fun sendFailed(message: String) {
        activity?.let {
            Functions.showMessage(it, message)
        }
    }

    fun setUser(user: User) {
        this.mUser = user
    }

    fun setProduct(product: Product) {
        this.mProduct = product
    }

    fun setVoucher(voucher: Voucher) {
        this.mVoucher = voucher
    }
}