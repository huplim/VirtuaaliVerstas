package com.example.virtuaaliverstas.qrreader

import android.content.Intent
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.google.mlkit.vision.barcode.common.Barcode
import androidx.core.net.toUri

/**
 * ViewModel class for handling QR code data and interactions.
 *
 * This class is responsible for processing a detected QR code, extracting its content,
 * and providing a touch callback to handle user interactions with the detected QR code area.
 * It supports both URL and other types of QR code content.
 *
 * @param barcode The Barcode object detected by ML Kit.
 */

class QrCodeReaderViewModel(barcode: Barcode) {
    var boundingRect: Rect? = barcode.boundingBox!!
    var qrContent: String
    var qrCodeTouchCallback: (View, MotionEvent) -> Boolean = { _, _ -> false }

    init {
        if (barcode.valueType == Barcode.TYPE_URL) {
            // Ensure that the URL is not null
            qrContent = barcode.url?.url ?: "Invalid URL"
            qrCodeTouchCallback = { view, event ->
                // Set the touch callback to open the URL in a browser
                if (event.action == MotionEvent.ACTION_DOWN && boundingRect?.contains(event.x.toInt(),
                        event.y.toInt()) == true) {
                    // Create an intent to open the URL in a browser
                    val openBrowserIntent = Intent(Intent.ACTION_VIEW).apply {
                        data = qrContent.toUri()
                    }
                    // Attempt to start the activity to open the URL
                    try {
                        view.context.startActivity(openBrowserIntent)
                    } catch (e: Exception) {
                        Log.e("QrCodeReaderViewModel", "Error opening URL", e)
                    }
                }
                true
            }
        }
        else {
            qrContent = "Unsupported data type: ${barcode.rawValue.toString()}"
        }
    }
    companion object
}