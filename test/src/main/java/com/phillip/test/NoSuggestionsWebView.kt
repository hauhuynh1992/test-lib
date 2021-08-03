package com.phillip.test

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.webkit.WebView

class NoSuggestionsWebView : WebView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context, attrs,
        defStyle
    )

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection? {
        val ic = super.onCreateInputConnection(outAttrs)

        /* clear VARIATION type to be able to set new value */
        outAttrs.inputType = outAttrs.inputType and EditorInfo.TYPE_MASK_VARIATION.inv()
        /* WEB_PASSWORD type will prevent form suggestions */
        outAttrs.inputType = outAttrs.inputType or InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD

        return ic
    }
}