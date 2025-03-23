package com.example.virtuaaliverstas.qrreader

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable

/**
 * Drawable class for visualizing the bounding box of a detected QR code.
 *
 * This class draws a red rectangle on the screen to indicate the location
 * of a detected QR code. It uses the bounding box data from the
 * QrCodeReaderViewModel to determine where to draw the rectangle.
 *
 * @param qrCodeViewModel The ViewModel containing the bounding box information.
 */

class QrCodeDrawable(private val qrCodeViewModel: QrCodeReaderViewModel) : Drawable() {
    private val paint: Paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 8.0f
    }

    override fun draw(canvas: Canvas) {
        val boundingBox: Rect = qrCodeViewModel.boundingRect ?: Rect()
        canvas.drawRect(boundingBox, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int {
        return paint.alpha
    }

    override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) {
        paint.colorFilter = colorFilter
    }
}