package com.phillip.test.ui.Ordersuccess

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.phillip.test.R
import com.phillip.test.utils.Functions
import kotlinx.android.synthetic.main.fragment_add_to_cart_success.*
import kotlinx.android.synthetic.main.fragment_order_success.*
import kotlinx.android.synthetic.main.fragment_order_success.bn_confirm
import kotlinx.android.synthetic.main.fragment_order_success.tvBrandShop
import kotlinx.android.synthetic.main.fragment_order_success.tvOrderId
import kotlinx.android.synthetic.main.fragment_order_success.tvQuantity

class OrderSuccessFragment : Fragment() {

    var mListener: onQuickpayListener? = null
    private var orderId: String? = null
    private var brandShop: String? = null
    private var shippingTime: Int = -1
    private var quantity: Int = 0
    private var totalPrice: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = it.getString("STR_ORDER_ID", "")
            brandShop = it.getString("STR_PRODUCT_SUPPLIER_NAME", "")
            shippingTime = it.getInt("STR_PRODUCT_SUPPLIER_TIME", -1)
            quantity = it.getInt("STR_PRODUCT_QUANTITY", 0)
            totalPrice = it.getString("STR_PRODUCT_TOTAL_PRICE", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_success, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvBrandShop.text = brandShop

        tvOrderId.text = orderId
        tvQuantity.text = quantity.toString() + " sản phẩm"

        _price.text = totalPrice

        ship_time.text = Functions.getShippingTimeBySupplier(shippingTime)

        bn_confirm.setOnClickListener {
            mListener?.onOrderSuccessConfirmClick()
        }

        bn_confirm.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                    text_bn_confirm.setTextColor(Color.WHITE)
            } else {
                    text_bn_confirm.setTextColor(Color.parseColor("A1B753"))
            }
        }

        val timer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                text_bn_confirm?.text = "Hoàn tất " + "(" + (millisUntilFinished / 1000) + "s)"
            }

            override fun onFinish() {
                mListener?.onOrderSuccessConfirmClick()
            }
        }
        timer.start()
    }

    fun setListener(listenr: onQuickpayListener) {
        mListener = listenr
    }

    interface onQuickpayListener {
        fun onOrderSuccessConfirmClick()
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            bn_confirm?.requestFocus()
        }, 100)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OrderSuccessFragment()
    }
}