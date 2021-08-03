package com.phillip.test.utils.view

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.phillip.test.R

class BDAKeyboardView : LinearLayout, View.OnFocusChangeListener {
    private val row1 = arrayOf(
        Key("q"),
        Key("w"),
        Key("e"),
        Key("r"),
        Key("t"),
        Key("y"),
        Key("u"),
        Key("i"),
        Key("o"),
        Key("p"),
        Key("/"),
        Key("-"),
        Key("_")

    )
    private val row2 = arrayOf(
        Key("a"),
        Key("s"),
        Key("d"),
        Key("f"),
        Key("g"),
        Key("h"),
        Key("j"),
        Key("k"),
        Key("l"),
        Key("@"),
        Key(
            "left",
            R.drawable.abc_left_selector,
            R.drawable.menu_border_selector
        ),
        Key(
            "right",
            R.drawable.abc_right_selector,
            R.drawable.menu_border_selector
        ),
        Key("Xoá")
    )

    private val row3 = arrayOf(
        Key("z"),
        Key("x"),
        Key("c"),
        Key("v"),
        Key("b"),
        Key("n"),
        Key("m"),
        Key("Dấu cách"),
        Key("Hoàn tất"),
        Key(
            "down",
            R.drawable.abc_next_selector,
            R.drawable.menu_border_selector
        )
    )

    private val number12345 = arrayOf(
        Key("1"),
        Key("2"),
        Key("3"),
        Key("4"),
        Key("5"),
        Key(
            "left",
            R.drawable.abc_left_selector,
            R.drawable.menu_border_selector
        ),
        Key(
            "right",
            R.drawable.abc_right_selector,
            R.drawable.menu_border_selector
        ),
        Key(
            "delete",
            R.drawable.abc_delete_white_selector,
            R.drawable.menu_border_selector
        )
    )

    private val number67890 = arrayOf(
        Key("6"),
        Key("7"),
        Key("8"),
        Key("9"),
        Key("0"),
        Key("Hoàn tất"),
        Key("Xoá"),
        Key(
            "down",
            R.drawable.abc_next_selector,
            R.drawable.menu_border_selector
        )
    )

    private val numbers1 = arrayOf(
        Key("1"),
        Key("2"),
        Key("3"),
        Key("4"),
        Key("5"),
        Key("6"),
        Key("7"),
        Key("8"),
        Key("9"),
        Key("0"),
        Key("."),
        Key(","),
        Key(
            "delete",
            R.drawable.abc_delete_white_selector,
            R.drawable.menu_border_selector
        )
    )
    ////////CAPLOCK///////

    private val row4 = arrayOf(
        Key("Q"),
        Key("W"),
        Key("E"),
        Key("R"),
        Key("T"),
        Key("Y"),
        Key("U"),
        Key("I"),
        Key("O"),
        Key("P"),
        Key("-"),
        Key("_"),
        Key(
            "delete",
            R.drawable.abc_delete_white_selector,
            R.drawable.menu_border_selector
        )

    )
    private val row5 = arrayOf(
        Key("A"),
        Key("S"),
        Key("D"),
        Key("F"),
        Key("G"),
        Key("H"),
        Key("J"),
        Key("K"),
        Key("L"),
        Key(
            "left",
            R.drawable.abc_left_selector,
            R.drawable.menu_border_selector
        ),
        Key(
            "right",
            R.drawable.abc_right_selector,
            R.drawable.menu_border_selector
        ),
        Key("Xoá"),
        Key("@")
    )
    private val row6 = arrayOf(
        Key("Z"),
        Key("X"),
        Key("C"),
        Key("V"),
        Key("B"),
        Key("N"),
        Key("M"),
        Key("Dấu cách"),
        Key("Hoàn tất"),
        Key(
            "down",
            R.drawable.abc_next_selector,
            R.drawable.menu_border_selector
        )

    )


    private var mCallback: OnKeyboardCallback? = null
    private var mQuery: StringBuffer = StringBuffer()
    private var cursor: Int = 0

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

    fun initKeyboardNumber() {
        if (!isInEditMode) {
            this.setPadding(
                0,
                (resources.displayMetrics.density * 30).toInt(),
                0,
                (resources.displayMetrics.density * 30).toInt()
            )

            orientation = VERTICAL
            gravity = Gravity.CENTER
            createNumberView()
            onFocusChangeListener = this
        }
    }

    fun initKeyboardName() {
        if (!isInEditMode) {
            this.setPadding(
                0,
                (resources.displayMetrics.density * 30).toInt(),
                0,
                (resources.displayMetrics.density * 30).toInt()
            )
            orientation = VERTICAL
            gravity = Gravity.CENTER
            createABCNameView()
            onFocusChangeListener = this
        }
    }

    fun initKeyboardAddress() {
        if (!isInEditMode) {
            this.setPadding(
                0,
                (resources.displayMetrics.density * 18).toInt(),
                0,
                (resources.displayMetrics.density * 18).toInt()
            )
            orientation = VERTICAL
            gravity = Gravity.CENTER
            createABCAddressView()
            onFocusChangeListener = this
        }
    }

    /*fun initKeyboardVaucher() {
        if (!isInEditMode) {
            this.setPadding(
                0,
                (resources.displayMetrics.density * 30).toInt(),
                0,
                (resources.displayMetrics.density * 30).toInt()
            )
            orientation = VERTICAL
            gravity = Gravity.CENTER
            createABCVaucher()
            onFocusChangeListener = this
        }
    }*/


    @Suppress("SENSELESS_COMPARISON")
    private fun createRow(vararg cells: Key): LinearLayout {
        val row = LinearLayout(context)
        row.gravity = Gravity.START
        row.orientation = HORIZONTAL

        val textSize: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (resources.displayMetrics.density * 7).toInt()
        } else {
            (resources.displayMetrics.density * 9).toInt()
        }
        val margin = (resources.displayMetrics.density * 7).toInt()
        val height = (resources.displayMetrics.density * 35).toInt()
        val width = (resources.displayMetrics.density * 35).toInt()
        if (cells != null && cells.isNotEmpty()) {
            for (cell in cells) {
                if (cell.resourceId > 0) {
                    val params =
                        LayoutParams(height, height)
                    val button = createKeyWithIcon(cell, margin, params)
                    row.addView(button)
                } else {
                    val params: LayoutParams = if (cell.name.length > 1) {
                        when (cell.name) {
                            "Hoàn tất" -> {
                                LayoutParams(
                                    (resources.displayMetrics.density * 84).toInt(),
                                    height
                                )
                            }
                            "Dấu cách" -> {
                                LayoutParams(
                                    (resources.displayMetrics.density * 133).toInt(),
                                    height
                                )
                            }
                            else -> {
                                LayoutParams(width, height)
                            }
                        }

                    } else {
                        LayoutParams(width, height)
                    }
                    val tvText = createKeyWithText(cell, textSize, margin, params)
                    if (tvText != null) row.addView(tvText)
                }
            }
        }
        return row
    }

    private fun createKeyWithIcon(
        @NonNull key: Key, margin: Int,
        params: LayoutParams
    ): View {
        val cardView = CardView(context)
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        lp.setMargins(margin, margin, margin, margin / 4)
        cardView.maxCardElevation = resources.displayMetrics.density * 2
        cardView.cardElevation = resources.displayMetrics.density * 2
//        cardView.setCardBackgroundColor(
//            ContextCompat.getColor(
//                context,
//                R.color.lb_error_background_color_translucent
//            )
//        )
        cardView.radius = resources.displayMetrics.density * 5
        cardView.layoutParams = lp

        val button = ImageButton(context)
        button.isFocusable = true
        button.onFocusChangeListener = this
        button.setBackgroundResource(key.backgroundId)
        button.scaleType = ImageView.ScaleType.CENTER_INSIDE
        button.isFocusableInTouchMode = true
        button.setImageResource(key.resourceId)
        button.setOnClickListener { _ ->
            detectKeyWithCustomAction(
                key.name
            )
        }
        button.layoutParams = params
        cardView.addView(button)
        return cardView
    }


    private fun createKeyWithText(
        @NonNull key: Key, textSize: Int,
        margin: Int,
        params: LayoutParams
    ): View {
        val cardView = CardView(context)
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        lp.setMargins(margin, margin, margin, margin / 2)
        cardView.maxCardElevation = resources.displayMetrics.density * 2
        cardView.cardElevation = resources.displayMetrics.density * 2
//        cardView.setCardBackgroundColor(
//            ContextCompat.getColor(
//                context,
//                R.color.lb_error_background_color_translucent
//            )
//        )
        cardView.radius = resources.displayMetrics.density * 5
        cardView.layoutParams = lp

        val button = TextView(context)
        button.text = key.name
        button.textSize = textSize.toFloat()
        button.onFocusChangeListener = this
//        button.typeface = FontUtils.getFontSFMedium(context.assets)
//        val textColorId = ContextCompat.getColorStateList(
//            context,
//            if (key.isColorResource) R.color.lb_tv_white else R.color.lb_tv_white
//        )
//        button.setTextColor(textColorId)
        button.setBackgroundResource(key.backgroundId)
        button.isFocusable = true
        button.gravity = Gravity.CENTER
        button.isFocusableInTouchMode = true
        button.layoutParams = params
        button.setOnClickListener { _ ->
            detectKeyWithCustomAction(
                key.name
            )
        }
        cardView.addView(button)
        return cardView
    }

    @Suppress("SENSELESS_COMPARISON")
    private fun detectKeyWithCustomAction(text: String) {
        when (text) {
            "123" -> {
                replaceABCToNumberView()
                return
            }
            "abc" -> {
                replaceNumberToABCView()
                return
            }
            "Dấu cách" -> {
                mQuery.insert(cursor, " ")
                cursor++
            }
            "Xoá" -> {
                mQuery.delete(0, mQuery.length)
                cursor = 0
            }
            "left" -> if (mQuery != null && mQuery.isNotEmpty()) {
                cursor = if ((cursor - 1) < 1) 0 else (cursor - 1)
                if (mCallback != null) {
                    mCallback!!.onChangeCursor(cursor)
                    return
                }
            }
            "right" -> if (mQuery != null && mQuery.isNotEmpty()) {
                cursor = if ((cursor + 1) > mQuery.length) mQuery.length else (cursor + 1)
                if (mCallback != null) {
                    mCallback!!.onChangeCursor(cursor)
                    return
                }
            }
            "delete" ->
                if (mQuery != null && mQuery.isNotEmpty() && cursor >= 1) {
                    mQuery.deleteCharAt(cursor - 1)
                    cursor--
                    if (mQuery.length == 0) {
                        cursor = 0
                    }
                }
            "down" -> if (mQuery != null && mQuery.isNotEmpty()) {
                if (mCallback != null) {
                    mCallback!!.onNext()
                }
            }
            "Hoàn tất" -> {
                if (mCallback != null) {
                    mCallback!!.onComplete()
                }
            }
            else -> {
                mQuery.insert(cursor, text)
                cursor++
            }
        }
        if (mCallback != null) {
            mCallback!!.onSearchSubmit(mQuery.toString(), cursor)
        }
    }

    /*fun searchVoice(text: String?) {
        if (mCallback != null) mCallback!!.onSearchSubmit(text, cursor)
    }*/

    fun addText(text: String) {
        mQuery.insert(0, text)
        cursor = mQuery.length
        if (mCallback != null) mCallback!!.onSearchSubmit(text, cursor)
    }

    private fun createABCView() {
        val layout = LinearLayout(context)
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layout.layoutParams = lp
        layout.gravity = Gravity.CENTER
        layout.orientation = VERTICAL
        layout.addView(createRow(*row1))
        layout.addView(createRow(*row2))
        layout.addView(createRow(*row3))
        addView(layout)

    }

    private fun createABCNameView() {
        val layout = LinearLayout(context)
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layout.layoutParams = lp
        layout.gravity = Gravity.CENTER
        layout.orientation = VERTICAL
        layout.addView(createRow(*row4))
        layout.addView(createRow(*row5))
        layout.addView(createRow(*row6))
        addView(layout)
    }

    private fun createABCAddressView() {

        val layout = LinearLayout(context)
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layout.layoutParams = lp
        layout.gravity = Gravity.CENTER
        layout.orientation = VERTICAL
        layout.addView(createRow(*numbers1))
        layout.addView(createRow(*row1))
        layout.addView(createRow(*row2))
        layout.addView(createRow(*row3))
        addView(layout)
    }

    /*private fun createEmailView() {
        val layout = LinearLayout(context)
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layout.layoutParams = lp
        layout.gravity = Gravity.CENTER
        layout.orientation = VERTICAL
        layout.addView(createRow(*numbers))
        layout.addView(createRow(*row1))
        layout.addView(createRow(*row2))
        layout.addView(createRow(*row3))
        addView(layout)
    }*/

    /* private fun createABCVaucher() {
         val layout = LinearLayout(context)
         val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
         layout.layoutParams = lp
         layout.gravity = Gravity.CENTER
         layout.orientation = VERTICAL
         layout.addView(createRow(*numbers1))
         layout.addView(createRow(*row4))
         layout.addView(createRow(*row5))
         layout.addView(createRow(*row6))
         addView(layout)
     }*/

    private fun createNumberView() {
        val layout = LinearLayout(context)
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layout.layoutParams = lp
        layout.gravity = Gravity.CENTER
        layout.orientation = VERTICAL
        layout.addView(createRow(*number12345))
        layout.addView(createRow(*number67890))
        addView(layout)
    }

    private fun replaceABCToNumberView() {
        removeAllViews()
        createNumberView()
        requestLayout()
        val focusView = getChildAt(0)
        focusView?.requestFocus()
    }

    private fun replaceNumberToABCView() {
        removeAllViews()
        createABCView()
        requestLayout()
        val focusView = getChildAt(0)
        focusView?.requestFocus()
    }

    private var view: View? = null
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        view = v
        if (mCallback != null) {
            mCallback!!.onSearchFocusable(hasFocus)
        }
    }

    private class Key @JvmOverloads constructor(
        var name: String, @field:DrawableRes @DrawableRes val resourceId: Int = 0,
        @field:DrawableRes val backgroundId: Int = R.drawable.menu_border_selector
    ) {
        val isColorResource: Boolean = backgroundId == R.drawable.menu_border_selector
    }

    fun setOnKeyboardCallback(callback: OnKeyboardCallback?) {
        mCallback = callback
    }

    interface OnKeyboardCallback {
        fun onSearchSubmit(query: String?, cursor: Int)
        fun onChangeCursor(position: Int)
        fun onComplete()
        fun onNext()
        fun onSearchFocusable(isFocus: Boolean)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event!!.action == KeyEvent.ACTION_DOWN) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_0 -> {
                    detectKeyWithCustomAction("0")
                }
                KeyEvent.KEYCODE_1 -> {
                    detectKeyWithCustomAction("1")
                }
                KeyEvent.KEYCODE_2 -> {
                    detectKeyWithCustomAction("2")
                }
                KeyEvent.KEYCODE_3 -> {
                    detectKeyWithCustomAction("3")
                }
                KeyEvent.KEYCODE_4 -> {
                    detectKeyWithCustomAction("4")
                }
                KeyEvent.KEYCODE_5 -> {
                    detectKeyWithCustomAction("5")
                }
                KeyEvent.KEYCODE_6 -> {
                    detectKeyWithCustomAction("6")
                }
                KeyEvent.KEYCODE_7 -> {
                    detectKeyWithCustomAction("7")
                }
                KeyEvent.KEYCODE_8 -> {
                    detectKeyWithCustomAction("8")
                }
                KeyEvent.KEYCODE_9 -> {
                    detectKeyWithCustomAction("9")
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }
}
