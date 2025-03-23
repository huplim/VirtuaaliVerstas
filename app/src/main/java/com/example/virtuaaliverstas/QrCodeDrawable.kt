package com.example.virtuaaliverstas

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable

class QrCodeDrawable(private val qrCodeViewModel: QrCodeReaderViewModel) : Drawable() {
    private val paint: Paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 8.0f
    }

    override fun draw(canvas: Canvas) {
        val boundingBox: Rect = qrCodeViewModel.boundingRect
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