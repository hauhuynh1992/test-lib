package com.phillip.test.ui.detailCart

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.phillip.test.R
import com.phillip.test.models.Product
import com.phillip.test.models.User
import com.phillip.test.utils.Functions
import kotlinx.android.synthetic.main.fragment_detail_cart.*

class DetailCartFragment : Fragment(), DetailCartContract.View {

    var mListener: onAddToCartListener? = null
    private var quatity: Int = 1
    private var product: Product? = null
    private var user: User? = null
    private var presenter: DetailCartPresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let {
            presenter = DetailCartPresenter(this@DetailCartFragment, it)
        }
        val view = inflater.inflate(R.layout.fragment_detail_cart, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvName?.text = product?.name
        price?.text = Functions.formatMoney(product!!.pricing!!.price_with_vat)
        image?.let {
            Functions.loadImage(product!!.image_cover, it)
        }

        bn_minus?.apply {
            setOnClickListener {
                if (quatity > 1)
                    setSetQuantity(--quatity)
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    Functions.animateScaleUpLiveStream(rl_quantity, 1.05f)
                    image_bn_minus.setColorFilter(Color.parseColor("#A1B753"))
                    rl_quantity.isSelected = true
                } else {
                    image_bn_minus.setColorFilter(Color.parseColor("#484848"))
                    if (!bn_minus.hasFocus() && !bn_plus.hasFocus()) {
                        Functions.animateScaleDown(rl_quantity)
                        rl_quantity.isSelected = false
                    }
                }
            }
        }


        bn_plus?.apply {
            setOnClickListener {
                setSetQuantity(++quatity)
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    Functions.animateScaleUpLiveStream(rl_quantity, 1.05f)
                    image_bn_plus.setColorFilter(Color.parseColor("#A1B753"))
                    rl_quantity.isSelected = true
                } else {
                    image_bn_plus.setColorFilter(Color.parseColor("#484848"))
                    if (!bn_minus.hasFocus() && !bn_plus.hasFocus()) {
                        rl_quantity.isSelected = false
                        Functions.animateScaleDown(rl_quantity)
                    }
                }
            }
        }

        bn_confirm.apply {
            setOnClickListener {
                product?.quantity = quatity
                if (product != null) {
                    product!!.quantity = quatity
                    presenter?.saveCart(userId = user!!.uid, product!!)
                } else {
                    Functions.showMessage(
                        activity!!,
                        "Chức năng đang được bảo trì, Vui lòng liện hệ sau"
                    )
                    return@setOnClickListener
                }
            }
        }
    }

    fun setListener(listenr: onAddToCartListener) {
        mListener = listenr
    }

    private fun setSetQuantity(q: Int) {
        quantity.text = "$q"
    }

    interface onAddToCartListener {
        fun onSubmitAddToCartSuccess(product: Product)
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ bn_confirm?.requestFocus() }, 100)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DetailCartFragment()
    }


    fun setUser(user: User) {
        this.user = user
    }

    fun setProduct(product: Product) {
        this.product = product
    }


    override fun sendSaveCartSuccess() {
        mListener?.onSubmitAddToCartSuccess(product!!)
    }

    override fun sendFailed(message: String) {
        activity?.let {
            Functions.showMessage(it, message)
        }
    }
}