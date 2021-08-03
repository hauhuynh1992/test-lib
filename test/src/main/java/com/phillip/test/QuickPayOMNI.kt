package com.phillip.test

import android.animation.ValueAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import com.phillip.test.ui.dialog.ImageOverlayDialog
import com.phillip.test.ui.main.MainQuickPayDialog
import com.phillip.test.utils.QuickPayUtils
import kotlinx.android.synthetic.main.dialog_fav.ivProduct
import kotlinx.android.synthetic.main.dialog_image_square.*
import java.io.InputStream
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class QuickPayOMNI(
    private val mActivity: Activity,
    private val fragmentManager: FragmentManager,
    private var mUserID: String,
    private var mUserPhone: String,
    private var mProductID: String,
    private var mImage: String,
    private var mPosistion: String,
    private var mSize: String,
    private var mPlatform: String,
    private var mListener: QuickPayListener
) {
    private var quickPayDialog: MainQuickPayDialog? = null
    private var imageDialog: AlertDialog? = null

    val mDialogView =
        mActivity.layoutInflater.inflate(QuickPayUtils.getScreenView(mSize), null) as ViewGroup

    fun isPopupShowing(): Boolean {
        if (imageDialog != null) {
            if (imageDialog!!.isShowing) {
                return true
            } else {
                if (quickPayDialog != null) {
                    return quickPayDialog!!.isDialogShowing()
                } else {
                    return false
                }
            }
        } else {
            return false
        }
    }

    fun showDialog() {
        quickPayDialog = MainQuickPayDialog(
            fptId = mUserID,
            phone = mUserPhone,
            productId = mProductID,
            platform = mPlatform,
            {
                mListener.onQuickPayExit()
            }
        )
        quickPayDialog?.show(fragmentManager, quickPayDialog?.tag)
    }

    fun showImageDialog() {
        if (imageDialog == null) {
            imageDialog = AlertDialog.Builder(mActivity)
                .setOnCancelListener { }
                .create().apply {
                    setView(mDialogView)
                    setCanceledOnTouchOutside(true)
                    show()
                    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    window?.attributes!!.windowAnimations = QuickPayUtils.getAnmiation(mPosistion)
                    window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                    window?.setGravity(QuickPayUtils.getScreenPosition(mPosistion))
                    window?.setLayout(
                        QuickPayUtils.getScreenWidth(mActivity, mSize),
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    val lp = window?.attributes
                    lp?.dimAmount = 0.2f
                    lp?.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
                    this?.window?.attributes = lp
                    val executor: ExecutorService = Executors.newSingleThreadExecutor()
                    val handler = Handler(Looper.getMainLooper())
                    executor.execute {
                        var logo: Bitmap? = null
                        try {
                            var inputStream: InputStream = URL(mImage).openStream()
                            logo = BitmapFactory.decodeStream(inputStream)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        handler.post {
                            this?.ivProduct?.setImageBitmap(logo)
                        }
                    }
                    this!!.setOnDismissListener {
                        mListener.onBannerExit()
                    }
                    this!!.setOnKeyListener(object : DialogInterface.OnKeyListener {
                        override fun onKey(
                            p0: DialogInterface?,
                            p1: Int,
                            event: KeyEvent
                        ): Boolean {
                            if (event.keyCode == KeyEvent.KEYCODE_DPAD_CENTER && event.action == 0) {
                                p0?.dismiss()
                                QuickPayUtils.callLogApi(
                                    mAction = CLICK_BANNER_ACTION,
                                    mUserID = mUserID,
                                    mUserPhone = mUserPhone,
                                    mProductID = mProductID,
                                    mPlatform = mPlatform,
                                    mImage = mImage
                                )
                                showDialog()
                                return true
                            }
                            return false
                        }

                    })

                }
        } else {
            imageDialog?.show()
        }
        val timer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                imageDialog?.dismiss()
            }
        }
        timer.start()

        // anim progress bar
        var anim = ValueAnimator.ofInt(QuickPayUtils.getProgressWidth(mActivity, mSize), 0)
        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams =
                this?.imageDialog!!.rlProgress!!.getLayoutParams()
            layoutParams.width = value
            this?.imageDialog!!.rlProgress!!.setLayoutParams(layoutParams)
        }
        anim.duration = 10000
        anim.start()

        // tracking
        QuickPayUtils.callLogApi(
            mAction = VIEW_BANNER_ACTION,
            mUserID = mUserID,
            mUserPhone = mUserPhone,
            mProductID = mProductID,
            mPlatform = mPlatform,
            mImage = mImage
        )
    }

    fun showImageOverlay() {
        val keyboardDialog = ImageOverlayDialog(
            mImage,
            mSize,
            mPosistion, {
                // Image exit
                mListener.onBannerExit()
            }, {
                // Image Click
                showDialog()
            })
        keyboardDialog.show(fragmentManager, keyboardDialog.tag)
    }

    interface QuickPayListener {
        fun onQuickPayExit()
        fun onBannerExit()
    }

    companion object {
        // Position
        const val TOP_LEFT_POSITION = "top-left"
        const val TOP_RIGHT_POSITION = "top-right"
        const val TOP_CENTER_POSITION = "top"
        const val BOT_LEFT_POSITION = "bottom-left"
        const val BOT_RIGHT_POSITION = "bottom-right"
        const val BOT_CENTER_POSITION = "bottom"
        const val LEFT_CENTER_POSITION = "left"
        const val RIGHT_CENTER_POSITION = "right"


        // Size
        const val SQUARE = "square"
        const val SMALL_RECTANGLE = "small-rectangle"
        const val LARGE_RECTANGLE_VERTICAL = "large-rectangle-vertical"
        const val LARGE_RECTANGLE_HORIZONTAL = "large-rectangle-horizontal"
        const val MEDIUM_RECTANGLE_HORIZONTAL = "medium-rectangle-horizontal"
        const val MEDIUM_RECTANGLE_VERTICAL = "medium-rectangle-vertical"

        // Action
        const val VIEW_BANNER_ACTION = "VIEW_BANNER"
        const val CLICK_BANNER_ACTION = "CLICK_BANNER"
    }
}