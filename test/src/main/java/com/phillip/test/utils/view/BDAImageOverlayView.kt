package com.phillip.test.utils.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.Nullable
import androidx.cardview.widget.CardView
import com.phillip.test.R
import com.phillip.test.utils.QuickPayUtils

class BDAImageOverlayView : LinearLayout {

    private var mCallback: OnImageOverlayCallback? = null
    var imageView = ImageView(context)

    constructor(context: Context?) : super(context) {

    }

    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context, attrs) {

    }

    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

    }

    fun initSquare(mPosition: String) {
        if (!isInEditMode) {
            val width = (resources.displayMetrics.density * 153).toInt()
            val height = (resources.displayMetrics.density * 153).toInt()
            this.setPadding(
                (resources.displayMetrics.density * 30).toInt(),
                (resources.displayMetrics.density * 30).toInt(),
                (resources.displayMetrics.density * 30).toInt(),
                (resources.displayMetrics.density * 30).toInt()
            )
            val layout = LinearLayout(context)
            layout.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            layout.orientation = VERTICAL
            layout.gravity = QuickPayUtils.getScreenPosition(mPosition)
            layout.addView(createImage(width, height))
            layout.addView(createProgressBar(width))
            addView(layout)
        }
    }

    fun initSmallRectangle(mPosition: String) {
        if (!isInEditMode) {
            val width = (resources.displayMetrics.density * 153).toInt()
            val height = (resources.displayMetrics.density * 128).toInt()
            this.setPadding(
                (resources.displayMetrics.density * 30).toInt(),
                (resources.displayMetrics.density * 30).toInt(),
                (resources.displayMetrics.density * 30).toInt(),
                (resources.displayMetrics.density * 30).toInt()
            )
            val layout = LinearLayout(context)
            layout.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layout.orientation = VERTICAL
            layout.gravity = QuickPayUtils.getScreenPosition(mPosition)
            layout.addView(createImage(width, height))
            layout.addView(createProgressBar(width))
            addView(layout)
        }
    }


    fun initMediumRectangleVertical(mPosition: String) {
        if (!isInEditMode) {
            val width = (resources.displayMetrics.density * 89).toInt()
            val height = (resources.displayMetrics.density * 337).toInt()
            this.setPadding(
                (resources.displayMetrics.density * 30).toInt(),
                (resources.displayMetrics.density * 30).toInt(),
                (resources.displayMetrics.density * 30).toInt(),
                (resources.displayMetrics.density * 30).toInt()
            )
            val layout = LinearLayout(context)
            layout.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            layout.orientation = VERTICAL
            layout.gravity = QuickPayUtils.getScreenPosition(mPosition)
            layout.addView(createImage(width, height))
            layout.addView(createProgressBar(width))
            addView(layout)
        }
    }

    fun initMediumRectangleHorizontal(mPosition: String) {
        if (!isInEditMode) {
            val width = (resources.displayMetrics.density * 409).toInt()
            val height = (resources.displayMetrics.density * 50).toInt()
            this.setPadding(
                (resources.displayMetrics.density * 30).toInt(),
                (resources.displayMetrics.density * 30).toInt(),
                (resources.displayMetrics.density * 30).toInt(),
                (resources.displayMetrics.density * 30).toInt()
            )
            val layout = LinearLayout(context)
            layout.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            layout.orientation = VERTICAL
            layout.gravity = QuickPayUtils.getScreenPosition(mPosition)
            layout.addView(createImage(width, height))
            layout.addView(createProgressBar(width))
            addView(layout)
        }
    }

    fun initLargeRectangleHorizontal(mPosition: String) {
        if (!isInEditMode) {
            val width = (resources.displayMetrics.density * 307).toInt()
            val height = (resources.displayMetrics.density * 82).toInt()
            this.setPadding(
                (resources.displayMetrics.density * 30).toInt(),
                (resources.displayMetrics.density * 30).toInt(),
                (resources.displayMetrics.density * 30).toInt(),
                (resources.displayMetrics.density * 30).toInt()
            )
            val layout = LinearLayout(context)
            layout.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            layout.orientation = VERTICAL
            layout.gravity = QuickPayUtils.getScreenPosition(mPosition)
            layout.addView(createImage(width, height))
            layout.addView(createProgressBar(width))
            addView(layout)
        }
    }

    fun initLargeRectangleVertical(mPosition: String) {
        if (!isInEditMode) {
            val width = (resources.displayMetrics.density * 153).toInt()
            val height = (resources.displayMetrics.density * 307).toInt()
            this.setPadding(
                (resources.displayMetrics.density * 30).toInt(),
                (resources.displayMetrics.density * 30).toInt(),
                (resources.displayMetrics.density * 30).toInt(),
                (resources.displayMetrics.density * 30).toInt()
            )
            val layout = LinearLayout(context)
            layout.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            layout.orientation = VERTICAL
            layout.gravity = QuickPayUtils.getScreenPosition(mPosition)
            layout.addView(createImage(width, height))
            layout.addView(createProgressBar(width))
            addView(layout)
        }
    }

    fun createImage(width: Int, height: Int): View {
        val cardView = CardView(context)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            cardView.background =
                resources.getDrawable(R.drawable.bg_quick_pay_progressbar_total, null)
        }
        cardView.maxCardElevation = resources.displayMetrics.density * 1
        cardView.cardElevation = resources.displayMetrics.density * 1
        cardView.radius = resources.displayMetrics.density * 15
        cardView.layoutParams = LayoutParams(
            width,
            height
        )

        imageView.layoutParams = LayoutParams(
            width,
            height
        )
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        imageView.setImageResource(R.drawable.ic_launcher_background)
        cardView.addView(imageView)
        return cardView
    }


    fun createProgressBar(width: Int): View {
        val height = (resources.displayMetrics.density * 8).toInt()
        val params = LayoutParams(width, height)
        params.setMargins(0, (resources.displayMetrics.density * 8).toInt(), 0, 0)
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = params

        var progressBar = RelativeLayout(context)
        progressBar.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            progressBar.background =
                resources.getDrawable(R.drawable.bg_quick_pay_progressbar_total, null)
        }

        var status = RelativeLayout(context)
        status.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, (resources.displayMetrics.density * 7).toInt())
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            status.background =
                resources.getDrawable(R.drawable.bg_quick_pay_progressbar, null)
        }
        progressBar.addView(status)
        linearLayout.addView(progressBar)
        val timer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                mCallback?.onImageExit()
            }
        }
        timer.start()

        // anim progress bar
        var anim = ValueAnimator.ofInt(
            width,
            0
        )
        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams =
                status?.getLayoutParams()
            layoutParams.width = value
            status?.setLayoutParams(layoutParams)
        }
        anim.duration = 10000
        anim.start()

        return linearLayout

    }


    fun setOnKeyboardCallback(callback: OnImageOverlayCallback?) {
        mCallback = callback
    }

    fun setImage(image: Bitmap) {
        imageView.setImageBitmap(image)
    }

    interface OnImageOverlayCallback {
        fun onImageExit()
    }

    companion object {

        // Size
        const val SQUARE = "square"
        const val SMALL_RECTANGLE = "small-rectangle"
        const val LARGE_RECTANGLE_VERTICAL = "large-rectangle-vertical"
        const val LARGE_RECTANGLE_HORIZONTAL = "large-rectangle-horizontal"
        const val MEDIUM_RECTANGLE_HORIZONTAL = "medium-rectangle-horizontal"
        const val MEDIUM_RECTANGLE_VERTICAL = "medium-rectangle-vertical"
    }
}
