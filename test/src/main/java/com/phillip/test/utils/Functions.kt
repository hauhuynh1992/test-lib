package com.phillip.test.utils

import android.animation.Animator
import android.animation.FloatEvaluator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import com.phillip.test.BuildConfig
import com.phillip.test.R
import com.phillip.test.models.*
import com.phillip.test.utils.view.Config
import kotlinx.android.synthetic.main.dialog_fav.*
import kotlinx.android.synthetic.main.dialog_message.tvMessage
import kotlinx.android.synthetic.main.dialog_message.tvTimout
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.net.URL
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

object Functions {
    private val suffixes = TreeMap<Long, String>()

    @Synchronized
    fun animateScaleDown(v: View, duration: Long = 200) {
        val currentScaleX = v.scaleX
        val currentScaleY = v.scaleY
        val targetScale = 1f.takeIf { duration == 200L } ?: 0f
        val scaleXHolder =
            PropertyValuesHolder.ofFloat(View.SCALE_X, currentScaleX, targetScale)
        val scaleYHolder =
            PropertyValuesHolder.ofFloat(View.SCALE_Y, currentScaleY, targetScale)
        val scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(v, scaleXHolder, scaleYHolder)
        scaleAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                if (targetScale == 0f) {
                    v.visibility = View.GONE
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
        scaleAnimator.duration = duration
        scaleAnimator.start()
    }

    @Synchronized
    fun animateScaleUp(v: View, scale: Float, duration: Long = 100) {
        v.visibility = View.VISIBLE
        val scaleXHolder = PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, scale)
        val scaleYHolder = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f, scale)
        val scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(v, scaleXHolder, scaleYHolder)
        scaleAnimator.duration = duration
        scaleAnimator.start()
    }

    fun formatMoney(money: Double): String {
        val formatter = DecimalFormat("#,###đ")
        return formatter.format(money)
    }

    fun format(value: Long): String {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == java.lang.Long.MIN_VALUE) return format(java.lang.Long.MIN_VALUE + 1)
        if (value < 0) return "-" + format(-value)
        if (value < 1000) return value.toString() //deal with easy case

        val e = suffixes.floorEntry(value)
        val divideBy = e.key
        val suffix = e.value

        val truncated = value / (divideBy!! / 10) //the number part of the output times 10
        val hasDecimal = truncated < 100 && truncated / 10.0 != (truncated / 10).toDouble()
        return if (hasDecimal) (truncated / 10.0).toString() + suffix else (truncated / 10).toString() + suffix
    }

    fun parseJSONString(jsonObject: JSONObject, key: String): String {
        try {
            return jsonObject.getString(key)
        } catch (e: Exception) {
            return ""
        }
    }

    fun parseJSONInt(jsonObject: JSONObject, key: String): Int {
        try {
            return jsonObject.getInt(key)
        } catch (e: Exception) {
            return 0
        }
    }

    fun parseJSONLong(jsonObject: JSONObject, key: String): Long {
        try {
            return jsonObject.getLong(key)
        } catch (e: Exception) {
            return 0
        }
    }

    fun parseJSONDouble(jsonObject: JSONObject, key: String): Double {
        try {
            return jsonObject.getDouble(key)
        } catch (e: Exception) {
            return 0.00
        }
    }

    fun parseJSONBoolean(jsonObject: JSONObject, key: String): Boolean {
        try {
            return jsonObject.getBoolean(key)
        } catch (e: Exception) {
            return false
        }
    }

    fun parseJSONBrandShop(jsonProductObject: JSONObject): ArrayList<BrandShop> {
        var brandShop = ArrayList<BrandShop>()
        try {
            val list: JSONArray = jsonProductObject.getJSONArray("brand_shop")
            val item = BrandShop(
                uid = parseJSONString(list.getJSONObject(0), "uid"),
                display_name_detail = parseJSONString(list.getJSONObject(0), "display_name_detail"),
                image_logo = parseJSONString(list.getJSONObject(0), "image_logo"),
                skin_image = parseJSONString(list.getJSONObject(0), "skin_image"),
                product_counts = parseJSONInt(list.getJSONObject(0), "product_counts")
            )
            brandShop.add(item)
            return brandShop
        } catch (e: Exception) {
             return brandShop
        }
    }

    fun parseJSONProduct(product: JSONObject): Product {
        try {
            var priceObject = product.getJSONObject("product.pricing")
            var supplierObject = product.getJSONObject("product.supplier")
            var sortDesObject = product.getJSONObject("short_descriptions")
            val supplier = Supplier(
                name = parseJSONString(supplierObject, "supplier_name"),
                shipping_time = parseJSONInt(
                    supplierObject,
                    "shipping_time"
                )
            )
            val brandShop = parseJSONBrandShop(product)
            val pricing = Pricing(
                price_with_vat = parseJSONDouble(
                    priceObject,
                    "price_with_vat"
                ),
                listed_price_with_vat = parseJSONDouble(
                    priceObject,
                    "listed_price_with_vat"
                )
            )
            val shortDescriptions = ShortDescriptions(
                short_des_1 = parseJSONString(
                    sortDesObject,
                    "short_des_1"
                ),
                short_des_1_color = parseJSONString(
                    sortDesObject,
                    "short_des_1_color"
                ),
                short_des_2 = parseJSONString(
                    sortDesObject,
                    "short_des_2"
                ),
                short_des_2_color = parseJSONString(
                    sortDesObject,
                    "short_des_2_color"
                ),
                short_des_3 = parseJSONString(
                    sortDesObject,
                    "short_des_3"
                ),
                short_des_3_color = parseJSONString(
                    sortDesObject,
                    "short_des_3_color"
                ),
                short_des_4 = parseJSONString(
                    sortDesObject,
                    "short_des_4"
                ),
                short_des_4_color = parseJSONString(
                    sortDesObject,
                    "short_des_4_color"
                ),
            )
            val mProduct = Product(
                uid = parseJSONString(product, "uid"),
                sku_id = parseJSONString(product, "sku_id"),
                name = parseJSONString(product, "display_name_detail"),
                image_cover = parseJSONString(product, "image_cover"),
                pricing = pricing,
                supplier = supplier,
                shortDescriptions = shortDescriptions,
                brandShops = brandShop
            )
            return mProduct
        } catch (e: Exception) {
            return Product()
        }
    }

    fun showFavDialog(
        mActivity: Activity,
        productName: String,
        productImage: String,
        productPrice: Double,
        time: Int

    ) {
        val mDialogView =
            mActivity.layoutInflater.inflate(R.layout.dialog_fav, null) as ViewGroup
        val messageDialog = AlertDialog.Builder(mActivity)
            .setOnCancelListener { }
            .create().apply {
                setView(mDialogView)
                setCanceledOnTouchOutside(true)
                show()
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window?.attributes!!.windowAnimations = R.style.SlideRightAnimation
                window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                window?.setGravity(Gravity.END)
                window?.setLayout(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                val lp = window?.attributes
//                lp?.dimAmount = 0.2f
                lp?.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
                this?.window?.attributes = lp
                this?.tvMessage?.text = "Đã thêm vào mục yêu thích "
                this?.tv_name?.text = productName
                this?.tv_price?.text = formatMoney(productPrice)
                val imageLink = Config.baseImageUrl + productImage + "?mode=scale&w=342&h=342"
                val executor: ExecutorService = Executors.newSingleThreadExecutor()
                val handler = Handler(Looper.getMainLooper())
                executor.execute {
                    var logo: Bitmap? = null
                    try {
                        var inputStream: InputStream = URL(imageLink).openStream()
                        logo = BitmapFactory.decodeStream(inputStream)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    handler.post {
                        this?.ivProduct?.setImageBitmap(logo)
                    }
                }


            }
        val timer = object : CountDownTimer(time.toLong() + 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                messageDialog?.tvTimout?.text = "Đóng " + "(" + (millisUntilFinished / 1000) + ")"
            }

            override fun onFinish() {
                messageDialog?.dismiss()
            }
        }
        timer.start()
    }

    fun showMessage(mActivity: Activity, message: String) {
        val mDialogView =
            mActivity.layoutInflater.inflate(R.layout.dialog_message, null) as ViewGroup
        val messageDialog = AlertDialog.Builder(mActivity)
            .setOnCancelListener { }
            .create().apply {
                setView(mDialogView)
                setCanceledOnTouchOutside(true)
                show()
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window?.attributes!!.windowAnimations = R.style.SlideRightAnimation
                window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                window?.setGravity(Gravity.END)
                window?.setLayout(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                val lp = window?.attributes
                lp?.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
                this?.window?.attributes = lp
                this?.tvMessage?.text = message

            }
        val timer = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                messageDialog?.tvTimout?.text = "Đóng " + "(" + (millisUntilFinished / 1000) + ")"
            }

            override fun onFinish() {
                messageDialog?.dismiss()
            }
        }
        timer.start()
    }

    @Synchronized
    fun animateScaleUpLiveStream(v: View, scale: Float, duration: Long = 100) {
        v.visibility = View.VISIBLE
        val scaleXHolder = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, scale)
        val scaleYHolder = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, scale)
        val scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(v, scaleXHolder, scaleYHolder)
        scaleAnimator.setEvaluator(FloatEvaluator())
        scaleAnimator.interpolator = AccelerateInterpolator()
        scaleAnimator.duration = duration
        scaleAnimator.start()
    }

    fun loadImage(imageLink: String, imageView: ImageView) {
        val imageLink = Config.baseImageUrl + imageLink + "?mode=scale&w=342&h=342"
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            var logo: Bitmap? = null
            try {
                var inputStream: InputStream = URL(imageLink).openStream()
                logo = BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            handler.post {
                imageView?.setImageBitmap(logo)
            }
        }
    }

    fun getShippingTimeBySupplier(t: Int): String {
        val rightNow = Calendar.getInstance()
        val now = rightNow.get(Calendar.HOUR_OF_DAY)

        val data = now + t
        return if (data < 9) {
            "Hôm nay"
        } else if (data >= 20) {
            var dayDuration: Int = data / 24
            if (dayDuration < 1)
                dayDuration = 1
            rightNow.add(Calendar.DAY_OF_YEAR, dayDuration)
            val day: Date = rightNow.time
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            dateFormat.format(day)
        } else {
            "${data}:00 hôm nay"
        }
    }

    fun checkPhoneNumber(phone: String): Boolean {
        return android.util.Patterns.PHONE.matcher(phone).matches()
    }

}