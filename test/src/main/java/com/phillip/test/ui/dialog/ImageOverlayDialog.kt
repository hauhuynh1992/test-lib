package com.phillip.test.ui.dialog

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.fragment.app.DialogFragment
import com.phillip.test.R
import com.phillip.test.utils.QuickPayUtils
import com.phillip.test.utils.view.BDAImageOverlayView
import kotlinx.android.synthetic.main.dialog_image_overlay.*
import java.io.InputStream
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ImageOverlayDialog(
    private val mUrl: String,
    private val mSize: String,
    private val mPosition: String,
    private val onImageExit: () -> Unit,
    private val onImageClick: () -> Unit
) :
    DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_image_overlay, container).apply {
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (keyboard != null) {
            keyboard.removeAllViews()
            when (mSize) {
                BDAImageOverlayView.SQUARE -> {
                    keyboard.initSquare(mPosition)
                }
                BDAImageOverlayView.SMALL_RECTANGLE -> {
                    keyboard.initSmallRectangle(mPosition)
                }

                BDAImageOverlayView.LARGE_RECTANGLE_HORIZONTAL -> {
                    keyboard.initLargeRectangleHorizontal(mPosition)
                }

                BDAImageOverlayView.LARGE_RECTANGLE_VERTICAL -> {
                    keyboard.initLargeRectangleVertical(mPosition)
                }

                BDAImageOverlayView.MEDIUM_RECTANGLE_HORIZONTAL -> {
                    keyboard.initMediumRectangleHorizontal(mPosition)
                }

                BDAImageOverlayView.MEDIUM_RECTANGLE_VERTICAL -> {
                    keyboard.initMediumRectangleVertical(mPosition)
                }
            }
        }
        keyboard.setOnKeyboardCallback(object : BDAImageOverlayView.OnImageOverlayCallback {
            override fun onImageExit() {
                onImageExit.invoke()
                dismiss()
            }
        })

        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            var logo: Bitmap? = null
            try {
                var inputStream: InputStream = URL(mUrl).openStream()
                logo = BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            handler.post {
                logo?.let {
                    keyboard?.setImage(it)
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
            wlp.gravity = QuickPayUtils.getScreenPosition(mPosition)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes!!.windowAnimations =
                QuickPayUtils.getAnmiation(position = mPosition)
            window.attributes = wlp
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val lp = window.attributes
            lp.dimAmount = 0f
            dialog.setOnKeyListener { dialog, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                        onImageClick.invoke()
                        dismiss()
                    }
                }
                false
            }
            lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            dialog.window?.attributes = lp
        }
    }
}
