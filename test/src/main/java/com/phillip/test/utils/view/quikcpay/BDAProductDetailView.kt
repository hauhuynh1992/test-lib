package com.phillip.test.utils.view.quikcpay

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.Nullable
import androidx.cardview.widget.CardView
import com.phillip.test.R

class BDAProductDetailView : LinearLayout {
    private var imageProduct: ImageView? = null
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


    private fun createView(){


    }

    fun createImageProduct(width: Int, height: Int): View {
        val cardView = CardView(context)
        cardView.maxCardElevation = resources.displayMetrics.density * 1
        cardView.cardElevation = resources.displayMetrics.density * 1
        cardView.radius = resources.displayMetrics.density * 15
        cardView.layoutParams = LayoutParams(
            width,
            height
        )

        imageProduct?.layoutParams = LayoutParams(
            width,
            height
        )
        imageProduct?.scaleType = ImageView.ScaleType.FIT_XY
        imageProduct?.setImageResource(R.drawable.ic_launcher_background)
        cardView.addView(imageProduct)
        return cardView
    }
}