/**
 * File: HiQrCodeListActivityScreen.kt
 * Description: Composable function to manage the QR code scanning and listing UI.
 *              Displays the camera preview when scanning is active and switches to the
 *              list of scanned QR codes after scanning is complete.
 *
 * Author: netcanis
 * Created: 2024-11-19
 *
 * License: MIT
 */

package com.mwkg.qr.view

import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.StateFlow
import com.mwkg.qr.model.HiQrCode

/**
 * Manages the UI state for the QR code scanning activity.
 * Switches between the camera preview and the list of scanned QR codes.
 *
 * @param qrCodes A StateFlow containing the list of scanned QR codes.
 * @param previewView The camera preview view for scanning QR codes.
 * @param onBackPressed Callback triggered when the user navigates back.
 * @param onToggleTorch Callback to toggle the camera's torch (flashlight).
 * @param isPreviewVisible A MutableState that tracks whether the camera preview is visible.
 * @param onQrScanned Callback triggered when a QR code is scanned.
 */
@Composable
fun HiQrCodeListActivityScreen(
    qrCodes: StateFlow<List<HiQrCode>>,
    previewView: PreviewView,
    onBackPressed: () -> Unit,
    onToggleTorch: (Boolean) -> Unit,
    isPreviewVisible: MutableState<Boolean>
) {
    // Show the camera preview when scanning is active
    if (isPreviewVisible.value) {
        HiQrCodePreviewScreen(
            onToggleTorch = onToggleTorch, // Callback for torch control
            onClosePreview = { isPreviewVisible.value = false }, // Callback to close the preview
            previewView = previewView // Camera preview view
        )
    } else {
        // Show the list of scanned QR codes when scanning is complete
        HiQrCodeListScreen(
            qrCodes = qrCodes, // Flow of scanned QR codes
            onBackPressed = onBackPressed // Handle back navigation
        )
    }
}
