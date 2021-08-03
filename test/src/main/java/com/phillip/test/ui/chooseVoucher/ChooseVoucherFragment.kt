package com.phillip.test.ui.chooseVoucher

import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.phillip.test.R
import com.phillip.test.models.Product
import com.phillip.test.models.User
import com.phillip.test.models.Voucher
import com.phillip.test.ui.dialog.KeyboardDialog
import com.phillip.test.utils.Functions
import kotlinx.android.synthetic.main.fragment_choose_voucher.*

class ChooseVoucherFragment : Fragment(), ChooseVoucherContract.View {

    private var mListener: onChooseVoucherListener? = null
    private var presenter: ChooseVoucherPresenter? = null
    private var mAdapter: VoucherAdapter? = null
    var quanlity: Int = 1
    private var product: Product? = null
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            quanlity = it.getInt("STR_PRODUCT_QUANTITY", 1)
        }
        activity?.let {
            presenter = ChooseVoucherPresenter(this@ChooseVoucherFragment, it)
        }
        presenter?.getListVoucher(user!!.uid)
        return inflater.inflate(R.layout.fragment_choose_voucher, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bn_confirm.setOnClickListener {
            if (edt_voucher.text.isNotEmpty()) {
                presenter?.checkVoucher(
                    productId = product!!.uid,
                    orderQuality = quanlity,
                    voucherCode = edt_voucher.text.toString().toUpperCase(),
                    customerId = user!!.uid
                )
            } else {
                activity?.let {
                    Functions.showMessage(it, "Vui lòng nhập Voucher")
                }
                Handler().postDelayed({
                    edt_voucher.requestFocus()
                }, 0)
            }
        }

        edt_voucher?.apply {
            setOnClickListener {
                if (edt_voucher != null) {
                    val keyboardDialog = KeyboardDialog(
                        KeyboardDialog.KEYBOARD_ADDRESS_TYPE,
                        edt_voucher?.text.toString(),
                        edt_voucher, edt_voucher
                    )
                    keyboardDialog.show(childFragmentManager, keyboardDialog.tag)
                }
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (edt_voucher != null)
                    if (hasFocus) {
                        Functions.animateScaleUp(edt_voucher, 1.01f)
                        edt_voucher?.setSelection(edt_voucher.text.length)
                    } else {
                        Functions.animateScaleDown(edt_voucher)
                    }
            }
        }
    }

    fun setListener(listenr: onChooseVoucherListener) {
        mListener = listenr
    }

    interface onChooseVoucherListener {
        fun onChooseVoucherConfirmClick(
            user: User,
            product: Product,
            vouchers: Voucher
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ChooseVoucherFragment()
    }

    override fun sendAppyVoucherSuccess(data: Voucher) {

        mListener?.onChooseVoucherConfirmClick(
            user!!,
            product!!,
            data
        )
    }

    override fun sendListVoucherSuccess(vouchers: ArrayList<Voucher>) {
        if (vouchers.size > 0) {
            text_choose_voucher?.visibility = View.VISIBLE
            activity?.let {
                mAdapter = VoucherAdapter(it, vouchers) { voucher ->
                    presenter?.checkVoucher(
                        productId = product!!.uid,
                        orderQuality = 1,
                        voucherCode = voucher.code,
                        customerId = user!!.uid
                    )
                }
                voucher_list?.apply {
                    adapter = mAdapter
                }
            }
        } else {
            text_choose_voucher?.visibility = View.GONE
            voucher_list?.visibility = View.GONE

            edt_voucher?.nextFocusDownId = bn_confirm.id
            bn_confirm?.nextFocusUpId = edt_voucher.id
        }
    }

    override fun sendFailed(message: String) {
        activity?.let {
            Functions.showMessage(it, message)
        }
    }

    fun setUser(user: User) {
        this.user = user
    }

    fun setProduct(product: Product) {
        this.product = product
    }

    fun myOnkeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && voucher_list != null && voucher_list.hasFocus()
            && voucher_list.visibility == View.VISIBLE
        ) {
            if (mAdapter != null && mAdapter!!.isFocusBottom) {
                bn_confirm.requestFocus()
                return true
            }
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_UP && voucher_list != null
            && voucher_list.hasFocus() && voucher_list.visibility == View.VISIBLE
        ) {
            if (mAdapter != null && mAdapter!!.isFocusTop) {
                edt_voucher.requestFocus()
                return true
            }
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && voucher_list.hasFocus() && voucher_list.visibility == View.VISIBLE) {
            return true
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && voucher_list.hasFocus() && voucher_list.visibility == View.VISIBLE) {
            return true
        }

        return false
    }
}