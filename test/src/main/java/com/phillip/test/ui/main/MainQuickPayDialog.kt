package com.phillip.test.ui.main

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.phillip.test.R
import com.phillip.test.models.Product
import com.phillip.test.models.User
import com.phillip.test.models.Voucher
import com.phillip.test.ui.Ordersuccess.OrderSuccessFragment
import com.phillip.test.ui.addToCartSuccess.AddToCartSuccessFragment
import com.phillip.test.ui.chooseVoucher.ChooseVoucherFragment
import com.phillip.test.ui.detailCart.DetailCartFragment
import com.phillip.test.ui.detailProduct.DetailProductFragment
import com.phillip.test.ui.paymentMethod.PaymentMethodFragment
import com.phillip.test.ui.quickPay.QuickPayFragment
import kotlinx.android.synthetic.main.dialog_quickpay.*


class MainQuickPayDialog(
    private val fptId: String,
    private val phone: String,
    private val productId: String,
    private val platform: String,
    private val onQuickPayExit: () -> Unit
) : DialogFragment(),
    QuickPayFragment.onQuickpayListener,
    PaymentMethodFragment.onPaymentMethodListener,
    DetailCartFragment.onAddToCartListener,
    ChooseVoucherFragment.onChooseVoucherListener,
    DetailProductFragment.onDetailProductListener,
    AddToCartSuccessFragment.onAddToCartSuccessListener,
    OrderSuccessFragment.onQuickpayListener {

    private var fm: FragmentManager? = null
    private var ft: FragmentTransaction? = null
    private var detailProductFragment: DetailProductFragment? = null
    private var addToCartFragment: DetailCartFragment? = null
    private var quickPayFragment: QuickPayFragment? = null
    private var paymentMethodFragment: PaymentMethodFragment? = null
    private var chooseVoucherFragment: ChooseVoucherFragment? = null
    private var successFragment: OrderSuccessFragment? = null
    private var addToCartSuccessFragment: AddToCartSuccessFragment? = null

    private var isShowing: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailProductFragment = DetailProductFragment.newInstance()
        quickPayFragment = QuickPayFragment.newInstance(platform)
        paymentMethodFragment = PaymentMethodFragment.newInstance()
        chooseVoucherFragment = ChooseVoucherFragment.newInstance()
        successFragment = OrderSuccessFragment.newInstance()
        addToCartSuccessFragment = AddToCartSuccessFragment.newInstance()
        addToCartFragment = DetailCartFragment.newInstance()

        detailProductFragment?.setListener(this)
        quickPayFragment?.setListener(this)
        paymentMethodFragment?.setListener(this)
        chooseVoucherFragment?.setListener(this)
        successFragment?.setListener(this)
        addToCartSuccessFragment?.setListener(this)
        addToCartFragment?.setListener(this)

        detailProductFragment?.let {
            var bundle = bundleOf(
                "STR_FPT_PLAY_ID" to fptId,
                "STR_PHONE" to phone,
                "STR_PRODUCT_ID" to productId,
            )
            it.arguments = bundle
            loadFragment(it, R.id.container_body, true)
        }

        return inflater.inflate(R.layout.dialog_quickpay, container)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(activity!!, theme) {
            override fun onBackPressed() {
                var fragment = fm?.findFragmentById(R.id.container_body)
                if (fragment is PaymentMethodFragment) {
                    quickPayFragment?.let {
                        loadFragment(it, R.id.container_body, true)
                    }
                } else if (fragment is QuickPayFragment) {
                    detailProductFragment?.let {
                        loadFragment(it, R.id.container_body, true)
                    }
                } else if (fragment is DetailCartFragment) {
                    detailProductFragment?.let {
                        loadFragment(it, R.id.container_body, true)
                    }
                } else if (fragment is ChooseVoucherFragment) {
                    paymentMethodFragment?.let {
                        loadFragment(it, R.id.container_body, true)
                    }
                } else {
                    super.onBackPressed()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {

            // Set gravity of dialog
            dialog.setCanceledOnTouchOutside(true)
            val window = dialog.window
            val wlp = window!!.attributes
            wlp.gravity = Gravity.BOTTOM
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.attributes = wlp
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val lp = window.attributes
            lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            dialog.window!!.attributes = lp
            dialog?.setOnKeyListener { dialog, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        val f = fm?.findFragmentById(R.id.container_body)
                        if (f != null && container_body.visibility == View.VISIBLE
                            && (f is ChooseVoucherFragment)
                        ) {
                            f.myOnkeyDown(keyCode, event)
                        }

                        if (f != null && container_body.visibility == View.VISIBLE
                            && (f is DetailProductFragment)
                        ) {
                            f.myOnkeyDown(keyCode, event)
                        }
                    }

                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        val f = fm?.findFragmentById(R.id.container_body)
                        if (f != null && container_body.visibility == View.VISIBLE
                            && (f is ChooseVoucherFragment)
                        ) {
                            f.myOnkeyDown(keyCode, event)
                        }

                        if (f != null && container_body.visibility == View.VISIBLE
                            && (f is DetailProductFragment)
                        ) {
                            f.myOnkeyDown(keyCode, event)
                        }
                    }
                }
                false
            }
        }
    }

    fun getFManager(): FragmentManager {
        if (fm == null)
            fm = this@MainQuickPayDialog.childFragmentManager
        return fm!!
    }

    open fun loadFragment(fragment: Fragment, layout: Int, isAnimation: Boolean) {
        if (fm == null) {
            fm = getFManager();
            ft = fm!!.beginTransaction();
            ft!!.add(layout, fragment, fragment.javaClass.name)
        } else {
            val tmp =
                fm!!.findFragmentByTag(fragment.javaClass.name)
            if (tmp != null && tmp.isVisible) {
                return
            }

            ft = fm!!.beginTransaction()
            if (isAnimation) {
                ft!!.setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
            }
            ft!!.replace(layout, fragment, fragment.javaClass.name)
            if (isAnimation) {
                ft!!.addToBackStack(fragment.javaClass.name)
            }
        }
        ft!!.commit()
    }

    override fun onOrderSuccessConfirmClick() {
        dialog?.dismiss()
    }

    override fun onSubmitOrderSuccess(orderId: String, product: Product, totalPrice: String) {
        val bundle = bundleOf(
            "STR_ORDER_ID" to orderId,
            "STR_PRODUCT_QUANTITY" to product.quantity,
            "STR_PRODUCT_SUPPLIER_NAME" to product.supplier?.name,
            "STR_PRODUCT_SUPPLIER_TIME" to product.supplier?.shipping_time,
            "STR_PRODUCT_TOTAL_PRICE" to totalPrice
        )
        successFragment?.arguments = bundle
        successFragment?.let {
            loadFragment(it, R.id.container_body, true)
        }
    }

    override fun onSelectVoucherClick(
        product: Product,
        user: User
    ) {

        chooseVoucherFragment?.let {
            it.setUser(user)
            it.setProduct(product)
            loadFragment(it, R.id.container_body, true)
        }
    }

    override fun onChooseVoucherConfirmClick(
        user: User,
        product: Product,
        vouchers: Voucher
    ) {
        paymentMethodFragment?.let {
            it.setProduct(product)
            it.setUser(user)
            it.setVoucher(vouchers)
            loadFragment(it, R.id.container_body, true)
        }
    }

    override fun onBuyProductClick(product: Product, user: User) {
        quickPayFragment?.let {
            it.setProduct(product)
            it.setUser(user)
            loadFragment(it, R.id.container_body, true)
        }
    }

    override fun onAddToCartClick(product: Product, user: User) {
        addToCartFragment?.let {
            it.setProduct(product)
            it.setUser(user)
            loadFragment(it, R.id.container_body, true)
        }
    }

    override fun onQuickPayConfirmClick(product: Product, user: User) {
        paymentMethodFragment?.let {
            it.setUser(user)
            it.setProduct(product)
            loadFragment(it, R.id.container_body, true)
        }
    }

    override fun onCompleteAddToCartClick() {
        dialog?.dismiss()
    }

    override fun onSubmitAddToCartSuccess(product: Product) {
        addToCartSuccessFragment?.let {
            it.setProduct(product)
            loadFragment(it, R.id.container_body, true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isShowing = true
    }

    override fun onDestroy() {
        super.onDestroy()
        isShowing = false
        onQuickPayExit.invoke()
    }

    fun isDialogShowing(): Boolean{
        return isShowing
    }

}