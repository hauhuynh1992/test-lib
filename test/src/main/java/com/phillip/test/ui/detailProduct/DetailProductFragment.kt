package com.phillip.test.ui.detailProduct

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.phillip.test.R
import com.phillip.test.models.Product
import com.phillip.test.models.User
import com.phillip.test.utils.Functions
import kotlinx.android.synthetic.main.fragment_add_to_cart_success.*
import kotlinx.android.synthetic.main.fragment_detail_product.*
import kotlinx.android.synthetic.main.fragment_quickpay.*
import kotlinx.android.synthetic.main.fragment_quickpay.image
import kotlinx.android.synthetic.main.fragment_quickpay.price

class DetailProductFragment : Fragment(), DetailProductContract.View,
    MotionLayout.TransitionListener {

    var mListener: onDetailProductListener? = null
    private var presenter: DetailProductPresenter? = null
    private var product: Product = Product()
    private var user: User = User()
    private var isFavourite = false
    private var productId: String = ""
    private var fptId: String = ""
    private var phone: String = ""
    private var isAddToCart: Boolean = false
    private var isPlayAnimation = false


    private var tvProductName: TextView? = null
    private var icAddToCart: ImageView? = null
    private var tvAddToCart: TextView? = null
    private var btnAddToCart: RelativeLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fptId = it.getString("STR_FPT_PLAY_ID", "")
            phone = it.getString("STR_PHONE", "")
            productId = it.getString("STR_PRODUCT_ID", "")
        }
        activity?.let {
            presenter = DetailProductPresenter(this@DetailProductFragment, it)
        }
        presenter?.getProduct(productId)
        presenter?.getProfile(fptId, phone)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_product, container, false)
        tvProductName = view.findViewById(R.id.tvName)
        icAddToCart = view.findViewById(R.id.img_add_to_cart)
        tvAddToCart = view.findViewById(R.id.tvAddToCart)
        btnAddToCart = view.findViewById(R.id.bn_add_to_cart)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bn_quick_pay?.setOnClickListener {
            mListener?.onBuyProductClick(product, user)
        }

        bn_add_to_cart?.setOnClickListener {
            mListener?.onAddToCartClick(product, user)
        }

        bn_wish_list?.setOnClickListener {
            if (isFavourite) {
                presenter?.deleteToWishList(productId, user.uid)
            } else {
                presenter?.addToWishList(productId, user.uid)
            }

        }

        btnAddToCart?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                tvAddToCart?.setTextColor(Color.WHITE)
                icAddToCart?.setColorFilter(Color.WHITE)

            } else {
                if (isAddToCart) {
                    tvAddToCart?.setTextColor(Color.parseColor("#A1B753"))
                    icAddToCart?.setColorFilter(Color.parseColor("#A1B753"))
                } else {
                    tvAddToCart?.setTextColor(Color.parseColor("#484848"))
                    icAddToCart?.setColorFilter(Color.parseColor("#484848"))
                }
            }
        }

        bn_wish_list?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                image_bn_wish.setColorFilter(Color.WHITE)
            } else {
                if (isFavourite) {
                    image_bn_wish.setColorFilter(Color.parseColor("#A1B753"))
                } else {
                    image_bn_wish.setColorFilter(Color.parseColor("#484848"))
                }

            }
        }
        img_add_to_cart.setColorFilter(Color.parseColor("#484848"))
        image_bn_wish.setColorFilter(Color.parseColor("#484848"))
        loadImageFavourite(isFavourite)
    }

    override fun sendProfileSuccess(user: User) {
        this.user = user
        presenter?.checkWishList(productId, user.uid)
        presenter?.checkCart(userId = user.uid, mProductId = productId)
    }

    override fun sendCheckCartSuccess(isAddToCart: Boolean) {
        this.isAddToCart = isAddToCart
        if (isAddToCart) {
            tvAddToCart?.setTextColor(Color.parseColor("#A1B753"))
            icAddToCart?.setColorFilter(Color.parseColor("#A1B753"))
            activity?.let {
                icAddToCart?.setImageDrawable(
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.ic_tick
                    )
                )
            }
        } else {
            tvAddToCart?.setTextColor(Color.parseColor("#484848"))
            activity?.let {
                icAddToCart?.setImageDrawable(
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.ic_plus
                    )
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        this.product?.let {
            initProduct(it)
        }
    }

    override fun sendProductSuccess(mProduct: Product) {
        product = mProduct
        initProduct(product)
    }

    private fun initProduct(mProduct: Product) {
        tvProductName?.text = mProduct.name
        sale_price?.text = Functions.formatMoney(mProduct.pricing.price_with_vat)
        price?.text = Functions.formatMoney(mProduct.pricing.listed_price_with_vat)
        price?.setPaintFlags(price.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
        image?.let {
            Functions.loadImage(mProduct.image_cover, it)
        }

        val discountPercent =
            (100 - (mProduct.pricing.price_with_vat * 100 / mProduct.pricing.listed_price_with_vat)).toInt()
        if (discountPercent > 0) {
            percent?.visibility = View.VISIBLE
            percent?.text = "-" + discountPercent + "%"
        } else {
            percent?.visibility = View.GONE
        }

        if (mProduct.brandShops.size > 0) {
            detail_supplier?.visibility = View.VISIBLE
            image_supplier?.let {
                Functions.loadImage(mProduct.brandShops.get(0).skin_image, it)
            }
            detail_supplier_name?.text = mProduct.brandShops.get(0).display_name_detail
            supplier_number_count?.text =
                Functions.format(product.brandShops[0].product_counts.toLong()) + " sản phẩm"
        } else {
            detail_supplier?.visibility = View.GONE
        }
        if (mProduct.shortDescriptions.short_des_1 != null && mProduct.shortDescriptions.short_des_1.isNotEmpty()) {
            ll_sort_1?.visibility = View.VISIBLE
            short_des_1?.text = mProduct.shortDescriptions.short_des_1
            try {
                short_des_1?.setTextColor(Color.parseColor(mProduct.shortDescriptions.short_des_1_color.takeIf { mProduct.shortDescriptions.short_des_1_color != null && mProduct.shortDescriptions.short_des_1_color.isNotEmpty() }
                    ?: "#000000"))
            } catch (e: IllegalArgumentException) {

            }
        } else {
            ll_sort_1?.visibility = View.GONE
        }

        if (mProduct.shortDescriptions.short_des_2 != null && mProduct.shortDescriptions.short_des_2.isNotEmpty()) {
            ll_sort_2?.visibility = View.VISIBLE
            short_des_2?.text = mProduct.shortDescriptions.short_des_2
            try {
                short_des_2?.setTextColor(Color.parseColor(mProduct.shortDescriptions.short_des_2_color.takeIf { mProduct.shortDescriptions.short_des_2_color != null && mProduct.shortDescriptions.short_des_2_color.isNotEmpty() }
                    ?: "#000000"))
            } catch (e: IllegalArgumentException) {

            }
        } else {
            ll_sort_2?.visibility = View.GONE
        }

        if (mProduct.shortDescriptions.short_des_3 != null && mProduct.shortDescriptions.short_des_3?.isNotEmpty()) {
            ll_sort_3?.visibility = View.VISIBLE
            short_des_3?.text = mProduct.shortDescriptions.short_des_3
            try {
                short_des_3?.setTextColor(Color.parseColor(mProduct.shortDescriptions.short_des_3_color.takeIf { mProduct.shortDescriptions.short_des_3_color != null && mProduct.shortDescriptions.short_des_3_color.isNotEmpty() }
                    ?: "#000000"))
            } catch (e: IllegalArgumentException) {

            }
        } else {
            ll_sort_3?.visibility = View.GONE
        }

        if (mProduct.shortDescriptions.short_des_4 != null && mProduct.shortDescriptions.short_des_4?.isNotEmpty()) {
            ll_sort_4?.visibility = View.VISIBLE
            short_des_4?.text = mProduct.shortDescriptions.short_des_4
            try {
                short_des_4?.setTextColor(Color.parseColor(mProduct.shortDescriptions.short_des_4_color.takeIf { mProduct.shortDescriptions.short_des_4_color != null && mProduct.shortDescriptions.short_des_4_color.isNotEmpty() }
                    ?: "#000000"))
            } catch (e: IllegalArgumentException) {

            }
        } else {
            ll_sort_4?.visibility = View.GONE
        }
    }

    fun myOnkeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP
            && !isPlayAnimation
            && ((bn_quick_pay != null && bn_quick_pay.hasFocus())
                    || ((bn_add_to_cart != null && bn_add_to_cart.hasFocus()
                    && bn_quick_pay != null
                    && bn_quick_pay?.visibility != View.VISIBLE)
                    || (bn_wish_list != null
                    && bn_wish_list.hasFocus()
                    && bn_quick_pay != null
                    && bn_quick_pay?.visibility != View.VISIBLE)
                    ))
        ) {
            showInfo()
            return true

        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
            && bn_add_to_cart != null
            && bn_wish_list != null
            && (bn_add_to_cart.hasFocus() || bn_wish_list.hasFocus())
            && !isPlayAnimation
        ) {
            hideInfo()
        }

        return false
    }

    override fun sendAddWishListSuccess(isFav: Boolean) {
        isFavourite = isFav
        activity?.let {
            Functions.showFavDialog(
                it,
                productName = product.name,
                productImage = product.image_cover,
                productPrice = product.pricing.price_with_vat,
                5000
            )
        }
        loadImageFavourite(isFav)
    }

    override fun sendDeleteWishListSuccess(isFav: Boolean) {
        isFavourite = isFav
        loadImageFavourite(isFav)
    }

    override fun sendCheckWishListSuccess(isFav: Boolean) {
        isFavourite = isFav
        loadImageFavourite(isFav)
    }

    override fun sendFailed(message: String) {
        activity?.let {
            Functions.showMessage(it, message)
        }

    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }

    fun setListener(listenr: onDetailProductListener) {
        mListener = listenr
    }

    interface onDetailProductListener {
        fun onBuyProductClick(product: Product, user: User)
        fun onAddToCartClick(product: Product, user: User)
    }

    private fun loadImageFavourite(isFav: Boolean) {
        if (isFav) {
            bn_wish_list?.let {
                if (it.hasFocus()) {
                    image_bn_wish.setColorFilter(Color.WHITE)
                } else {
                    image_bn_wish.setColorFilter(Color.parseColor("#A1B753"))
                }
            }

            activity?.let {
                image_bn_wish?.setImageDrawable(
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.ic_heart_liked
                    )
                )
            }
        } else {
            bn_wish_list?.let {
                if (it.hasFocus()) {
                    image_bn_wish.setColorFilter(Color.WHITE)
                } else {
                    image_bn_wish.setColorFilter(Color.parseColor("#484848"))
                }
            }

            activity?.let {
                image_bn_wish?.setImageDrawable(
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.ic_heart_unliked
                    )
                )
            }
        }
    }

    private fun hideInfo() {
        if (!isPlayAnimation)
            motionLayout?.let {
                it.setTransition(R.id.transition_hiding_info)
                it.setTransitionListener(this)
                it.transitionToEnd()
            }
    }

    private fun showInfo() {
        if (!isPlayAnimation)
            motionLayout?.let {
                it.setTransition(R.id.transition_showing_info)
                it.addTransitionListener(this)
                it.transitionToEnd()
            }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DetailProductFragment()
    }

    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
    }

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
        isPlayAnimation = true
    }

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
    }

    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
        isPlayAnimation = false
    }
}