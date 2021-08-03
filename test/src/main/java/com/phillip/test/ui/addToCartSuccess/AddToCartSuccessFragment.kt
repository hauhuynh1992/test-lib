package com.phillip.test.ui.addToCartSuccess

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.phillip.test.R
import com.phillip.test.models.Product
import com.phillip.test.utils.Functions
import kotlinx.android.synthetic.main.fragment_add_to_cart_success.*
import kotlinx.android.synthetic.main.fragment_add_to_cart_success.bn_confirm
import kotlinx.android.synthetic.main.fragment_add_to_cart_success.image
import kotlinx.android.synthetic.main.fragment_quickpay.*

class AddToCartSuccessFragment : Fragment() {

    var mListener: onAddToCartSuccessListener? = null
    private var orderId: String? = null
    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = it.getString("STR_ORDER_ID", "123456789")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_to_cart_success, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvBrandShop.text = product!!.supplier.name
        tvOrderId.text = "Đơn hàng: #" + "1234567"
        tvQuantity.text = "Số lượng: " + product!!.quantity
        tvPrice.text = Functions.formatMoney(product!!.pricing.price_with_vat * product!!.quantity)

        image?.let {
            Functions.loadImage(product!!.image_cover, it)
        }

        bn_confirm.setOnClickListener {
            mListener?.onCompleteAddToCartClick()
        }

        bn_confirm.setOnFocusChangeListener { _, hasFocus ->
            if (bn_confirm != null)
                if (hasFocus) {
                    Functions.animateScaleUp(bn_confirm, 1.05f)
//                    text_bn_confirm.setNewTextColor(R.color.color_white)
                } else {
                    Functions.animateScaleDown(bn_confirm)
//                    text_bn_confirm.setNewTextColor(R.color.color_A1B753)
                }
        }
    }

    fun setListener(listenr: onAddToCartSuccessListener) {
        mListener = listenr
    }

    interface onAddToCartSuccessListener {
        fun onCompleteAddToCartClick()
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            bn_confirm?.requestFocus()
        }, 100)
    }

    fun setProduct(product: Product) {
        this.product = product
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AddToCartSuccessFragment()
    }
}