/**
 * File: HiQrCodeListActivity.kt
 * Description: Activity for managing QR code scanning, displaying the camera preview,
 *              and updating the scanned QR code list using Jetpack Compose and ViewModel.
 *
 * Author: netcanis
 * Created: 2024-11-19
 *
 * License: MIT
 *
 * References:
 * - CameraX API: https://developer.android.com/training/camerax
 * - Jetpack Compose Integration: https://developer.android.com/jetpack/compose
 */

package com.mwkg.qr.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.camera.view.PreviewView
import androidx.compose.runtime.mutableStateOf
import com.mwkg.base.model.HiResult
import com.mwkg.qr.model.HiQrCode
import com.mwkg.qr.util.HiQrScanner
import com.mwkg.qr.viewmodel.HiQrCodeListViewModel

/**
 * Activity for scanning QR codes and displaying the results.
 * Integrates CameraX for preview and ML Kit for QR code detection.
 */
class HiQrCodeListActivity : ComponentActivity() {
    // ViewModel to manage the list of scanned QR codes
    private val viewModel: HiQrCodeListViewModel by viewModels()
    // State to track visibility of the camera preview
    private var isPreviewVisible = mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Camera preview view
        val previewView = PreviewView(this)

        // Start the QR scanner
        HiQrScanner.start(
            activity = this,
            previewView = previewView,
        ) { result ->
            when (result) {
                is HiResult.HiQrResult -> {
                    // Log the scanned QR data
                    Log.d("ModularX", result.qrData)

                    // Create a HiQrCode object from the scanned result
                    val code = HiQrCode(
                        content = result.qrData,
                        scannedAt = System.currentTimeMillis() // Record the scan time
                    )
                    viewModel.update(code) // Update the ViewModel with the scanned code

                    // Stop the scanner and hide the preview
                    HiQrScanner.stop()
                    isPreviewVisible.value = false
                }
                else -> {
                    // Log any errors during the scanning process
                    Log.e("ModularX::HiQrCodeListActivity", "Scan result error: ${result.error}")
                }
            }
        }

        // Set the UI content using Jetpack Compose
        setContent {
            HiQrCodeListActivityScreen(
                qrCodes = viewModel.codes, // Reactive list of QR codes from the ViewModel
                previewView = previewView, // The camera preview view
                onBackPressed = { finish() }, // Handle back press
                onToggleTorch = { isOn -> HiQrScanner.toggleTorch(isOn) }, // Control the camera torch
                isPreviewVisible = isPreviewVisible, // Track whether the preview is visible
                onQrScanned = { qrCode -> viewModel.update(qrCode) } // Update ViewModel on scan
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop the QR scanner when the activity is destroyed
        HiQrScanner.stop()
    }
}
