package com.example.virtuaaliverstas.qrreader

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode

/**
 * Class to handle QR code scanning and camera setup using ML Kit and CameraX.
 *
 * This class encapsulates the camera setup, barcode scanning logic, and the
 * detection process for QR codes. It uses ML Kit for barcode scanning and
 * CameraX for managing the camera lifecycle and preview.
 *
 * @param context The application context.
 * @param previewView The PreviewView instance for displaying the camera feed.
 * @param onQrCodeDetected Callback to be invoked when a QR code is detected.
 */

class QrCodeScanner(
    private val context: Context,
    private val previewView: PreviewView,
    private val onQrCodeDetected: (String) -> Unit
) {
    private lateinit var barcodeScanner: BarcodeScanner

    // Initializes and starts the camera and sets up the QR code scanner
    fun startCamera() {
        Log.d("QrCodeScanner", "Starting camera")
        val cameraController = LifecycleCameraController(context)

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
        barcodeScanner = BarcodeScanning.getClient(options)

        // Set the image analysis analyzer to process camera frames and detect QR codes
        cameraController.setImageAnalysisAnalyzer(
            ContextCompat.getMainExecutor(context),
            MlKitAnalyzer(
                listOf(barcodeScanner),
                COORDINATE_SYSTEM_VIEW_REFERENCED,
                ContextCompat.getMainExecutor(context)
            ) { result ->
                val barcodeResults = result?.getValue(barcodeScanner)
                // Clear the overlay and reset the touch listener if no QR code is detected
                if (barcodeResults.isNullOrEmpty()) {
                    previewView.overlay.clear()
                    previewView.setOnTouchListener { view, event ->
                        if (event.action == MotionEvent.ACTION_UP) {
                            view.performClick()
                        }
                        false // Make sure the event is not consumed
                    }
                    return@MlKitAnalyzer
                }

                val qrCodeViewModel = QrCodeReaderViewModel(barcodeResults[0])
                val qrCodeDrawable = QrCodeDrawable(qrCodeViewModel)

                previewView.setOnTouchListener { view, event ->
                    if (event.action == MotionEvent.ACTION_DOWN
                        && qrCodeViewModel.boundingRect?.contains(event.x.toInt(), event.y.toInt()) == true
                    ) {
                        onQrCodeDetected(qrCodeViewModel.qrContent)
                    }
                    if (event.action == MotionEvent.ACTION_UP) {
                        view.performClick()
                    }
                    true
                }
                previewView.overlay.clear()
                previewView.overlay.add(qrCodeDrawable)
            }
        )

        cameraController.bindToLifecycle(context as AppCompatActivity)
        previewView.controller = cameraController
    }

    // Method to close the barcode scanner and release resources
    fun closeScanner() {
        barcodeScanner.close()
    }
}