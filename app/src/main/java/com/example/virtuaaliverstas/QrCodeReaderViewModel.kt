package com.example.virtuaaliverstas

import android.content.Intent
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import com.google.mlkit.vision.barcode.common.Barcode
import androidx.core.net.toUri

class QrCodeReaderViewModel(barcode: Barcode) {
    var boundingRect: Rect? = barcode.boundingBox!!
    var qrContent: String
    var qrCodeTouchCallback: (View, MotionEvent) -> Boolean = { _, _ -> false }

    init {
        if (barcode.valueType == Barcode.TYPE_URL) {
            // Ensure that Barcode or Barcode.UrlBookmark objects aren't null
            qrContent = barcode.url!!.url!!
            qrCodeTouchCallback = { view, event ->
                // Open URL if the touch event is on the bounding box
                if (event.action == MotionEvent.ACTION_DOWN
                    && boundingRect?.contains(
                        event.x.toInt(), event.y.toInt()) == true
                    ) {

                    val openBrowserIntent = Intent(Intent.ACTION_VIEW).apply {
                        data = qrContent.toUri()
                    }
                    openBrowserIntent.data = qrContent.toUri()
                    view.context.startActivity(openBrowserIntent)
                }
                true
            }
        }
        else {
            qrContent = "Unsupported data type: ${barcode.rawValue.toString()}"
            qrCodeTouchCallback = { _, _ -> false }
        }
    }

    companion object
}