package com.example.virtuaaliverstas.qrreader

import android.Manifest.permission.CAMERA
import android.content.Intent
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.virtuaaliverstas.R

/**
 * Composable function to display the QR code reader screen.
 *
 * This screen handles camera permission requests, initializes the QR code scanner,
 * and displays the camera preview. When a QR code is scanned, it shows an alert
 * dialog with the scanned link and options to open or cancel.
 *
 * @param navController The navigation controller used to handle navigation actions.
 */

@Composable
fun QrCodeReaderScreen(navController: NavHostController) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var scannedLink by remember { mutableStateOf("") }

    // Request camera permission
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                // Camera permission denied
                Toast.makeText(context, context.getString(
                    R.string.camera_permissions_denied
                ), Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Launch the camera permission request when the screen is displayed
    LaunchedEffect(Unit) {
        cameraPermissionLauncher.launch(CAMERA)
    }
    // Create and remember the PreviewView. This ensures a single instance is used
    val previewView = remember { PreviewView(context) }

    // Initialize the QR code scanner with the previewView
    val qrCodeScanner = remember {
        QrCodeScanner(
            context = context,
            previewView = previewView
        ) { link ->
            scannedLink = link
            showDialog = true
        }
    }

    // Handle the lifecycle of the QR code scanner
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    // Dispose the scanner when the screen is disposed
    DisposableEffect(lifecycleOwner) {
        onDispose {
            qrCodeScanner.closeScanner()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = { _ ->
                previewView.apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
                qrCodeScanner.startCamera()
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.click_qr_code),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            )
        }
    }

    // Show the link dialog when QR code is pressed
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = stringResource(id = R.string.open_link))
            },
            text = {
                Text(text = scannedLink)
           },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    // Open the link in default browser
                    val intent = Intent(Intent.ACTION_VIEW, scannedLink.toUri())
                    navController.context.startActivity(intent)
                }) {
                    Text(text = stringResource(id = R.string.open))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }
}