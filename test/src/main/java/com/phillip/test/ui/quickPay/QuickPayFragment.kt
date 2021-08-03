package com.phillip.test.ui.quickPay

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.speech.RecognizerIntent
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.phillip.test.R
import com.phillip.test.models.Product
import com.phillip.test.models.User
import com.phillip.test.ui.dialog.KeyboardDialog
import com.phillip.test.utils.Functions
import kotlinx.android.synthetic.main.fragment_quickpay.*
import java.util.*

class QuickPayFragment(private val platform: String) : Fragment(), QuickPayContract.View {

    var mListener: onQuickpayListener? = null
    private var quatity: Int = 1
    private var product: Product? = null
    private var user: User? = null
    private var presenter: QuickPayPresenter? = null
    private var requestCode: Int = REQUEST_VOICE_NAME_CODE

    private var edt_phone: EditText? = null
    private var edt_name: EditText? = null
    private var bn_voice_name: RelativeLayout? = null
    private var bn_voice_phone: RelativeLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let {
            presenter = QuickPayPresenter(this@QuickPayFragment, it)
        }
        val view = inflater.inflate(R.layout.fragment_quickpay, container, false)
        edt_name = view.findViewById(R.id.edt_name)
        edt_phone = view.findViewById(R.id.edt_phone)
        bn_voice_name = view.findViewById(R.id.bn_voice_name)
        bn_voice_phone = view.findViewById(R.id.bn_voice_phone)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arrayListOf("box2019", "box2020", "box2021").contains(platform)) {
            bn_voice_name?.visibility = View.VISIBLE
            bn_voice_phone?.visibility = View.VISIBLE
        } else {
            bn_voice_name?.visibility = View.GONE
            bn_voice_phone?.visibility = View.GONE
        }

        edt_phone?.setText(user?.phone)
        edt_name?.setText(user?.name)

        tvName?.text = product?.name
        price?.text = Functions.formatMoney(product!!.pricing!!.price_with_vat)
        image?.let {
            Functions.loadImage(product!!.image_cover, it)
        }

        Handler().postDelayed({
            bn_confirm.requestFocus()
        }, 100)

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

        edt_name?.apply {
            setOnClickListener {
                if (edt_name != null) {
                    val keyboardDialog = KeyboardDialog(
                        KeyboardDialog.KEYBOARD_NAME_TYPE,
                        edt_name?.text.toString(),
                        edt_name!!, edt_name
                    )
                    keyboardDialog.show(childFragmentManager, keyboardDialog.tag)
                }
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (edt_name != null)
                    if (hasFocus) {
                        Functions.animateScaleUp(edt_name!!, 1.01f)
                        edt_name?.setSelection(edt_name!!.text.length)
                    } else {
                        Functions.animateScaleDown(edt_name!!)
                    }
            }

            setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.action == KeyEvent.ACTION_DOWN) {
                    Handler().postDelayed({
                        bn_voice_name?.requestFocus()
                    }, 0)
                    return@OnKeyListener true
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.action == KeyEvent.ACTION_DOWN) {
                    Handler().postDelayed({
                        edt_name?.requestFocus()
                    }, 0)
                    return@OnKeyListener true
                }
                false
            })
        }

        edt_phone?.apply {
            setOnClickListener {
                val keyboardDialog = KeyboardDialog(
                    KeyboardDialog.KEYBOARD_PHONE_TYPE,
                    edt_phone?.text.toString(),
                    edt_phone!!, edt_phone
                )
                keyboardDialog.show(childFragmentManager, keyboardDialog.tag)
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    Functions.animateScaleUp(edt_phone!!, 1.01f)
                    edt_phone?.setSelection(edt_phone!!.text.length)
                } else {
                    Functions.animateScaleDown(edt_phone!!)
                }
            }

            setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.action == KeyEvent.ACTION_DOWN) {
                    Handler().postDelayed({
                        bn_voice_phone?.requestFocus()
                    }, 0)
                    return@OnKeyListener true
                }
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.action == KeyEvent.ACTION_DOWN) {
                    Handler().postDelayed({
                        edt_name?.requestFocus()
                    }, 0)
                    return@OnKeyListener true
                }
                false
            })
        }

        bn_voice_name?.setOnClickListener {
            this.requestCode = REQUEST_VOICE_NAME_CODE
            requestVoicePermission()
        }

        bn_voice_phone?.setOnClickListener {
            this.requestCode = REQUEST_VOICE_PHONE_CODE
            requestVoicePermission()
        }

        bn_confirm.apply {
            setOnClickListener {
                product?.quantity = quatity
                if (edt_name?.text.toString().isNullOrBlank()) {
                    Functions.showMessage(activity!!, "Vui lòng nhập họ tên")
                    return@setOnClickListener
                }
                if (edt_phone?.text.toString().isNullOrBlank()) {
                    Functions.showMessage(activity!!, "Vui lòng nhập số điện thoại")
                    return@setOnClickListener
                }
                if (edt_phone?.text!!.length > 9) {
                    if (product != null) {
                        product!!.quantity = quatity
                        mListener?.onQuickPayConfirmClick(
                            product = product!!,
                            user = user!!
                        )
                    } else {
                        Functions.showMessage(
                            activity!!,
                            "Chức năng đang được bảo trì, Vui lòng liện hệ sau"
                        )
                        return@setOnClickListener
                    }
                } else {
                    Functions.showMessage(activity!!, "Số điện thoại không đúng")
                    return@setOnClickListener
                }
            }
        }
    }

    fun setListener(listenr: onQuickpayListener) {
        mListener = listenr
    }

    private fun setSetQuantity(q: Int) {
        quantity.text = "$q"
    }

    interface onQuickpayListener {
        fun onQuickPayConfirmClick(product: Product, user: User)
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ bn_confirm?.requestFocus() }, 100)
    }

    private fun requestVoicePermission() {
        activity?.let {
            requestPermissions(
                arrayOf(
                    Manifest.permission.RECORD_AUDIO
                ),
                REQUEST_VOICE_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d("BBBHAU", "Fragmnet - onRequestPermissionsResult")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        activity?.let {
            if (requestCode == REQUEST_VOICE_PERMISSIONS_REQUEST_CODE) {
                if (ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    voiceSpeechInput(this.requestCode)
                }
            }
        }
    }

    private fun voiceSpeechInput(requestCode: Int) {
        this.let {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("AAA", "checkSelfPermission: Done")
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM

                )
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN")
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                startActivityForResult(intent, requestCode)
            } else {
                requestVoicePermission()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                when (requestCode) {
                    REQUEST_VOICE_NAME_CODE -> {
                        edt_name?.setText(result?.get(0)?.toUpperCase())
                    }
                    REQUEST_VOICE_PHONE_CODE -> {
                        edt_phone?.setText(result?.get(0)?.replace(" ", ""))
                    }
                }
            }
        }
    }

    companion object {
        const val REQUEST_VOICE_NAME_CODE = 123
        const val REQUEST_VOICE_PHONE_CODE = 321
        const val REQUEST_VOICE_PERMISSIONS_REQUEST_CODE = 1

        @JvmStatic
        fun newInstance(platform: String) =
            QuickPayFragment(platform)
    }


    fun setUser(user: User) {
        this.user = user
    }

    fun setProduct(product: Product) {
        this.product = product
    }
}