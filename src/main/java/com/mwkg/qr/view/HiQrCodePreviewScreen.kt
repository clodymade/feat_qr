/**
 * File: HiQrCodePreviewScreen.kt
 * Description: Composable function for displaying the camera preview for QR code scanning,
 *              with controls for toggling the flashlight and closing the preview.
 *              Includes an overlay with a transparent Region of Interest (ROI).
 *
 * Author: netcanis
 * Created: 2024-11-19
 *
 * License: MIT
 *
 * References:
 * - CameraX API: https://developer.android.com/training/camerax
 * - Jetpack Compose Canvas API: https://developer.android.com/jetpack/compose/graphics
 */

package com.mwkg.qr.view

import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import com.mwkg.qr.R

/**
 * Displays the camera preview with an overlay and action buttons for flashlight control and closing the preview.
 *
 * @param onToggleTorch Callback to toggle the camera flashlight (torch).
 * @param onClosePreview Callback triggered when the close button is pressed.
 * @param previewView The CameraX PreviewView used to display the camera feed.
 */
@Composable
fun HiQrCodePreviewScreen(
    onToggleTorch: (Boolean) -> Unit,
    onClosePreview: () -> Unit,
    previewView: PreviewView
) {
    // Remember the flashlight (torch) state across recompositions
    var isTorchOn by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Render the CameraX PreviewView using AndroidView
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // Draw the overlay with ROI and dimming
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawQrRoiOverlay() // Custom function to draw the overlay
        }

        // Action buttons at the top center (torch toggle and close)
        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flashlight toggle button
            IconButton(onClick = {
                isTorchOn = !isTorchOn // Toggle the torch state
                onToggleTorch(isTorchOn) // Pass the state via callback
            }) {
                Icon(
                    painter = painterResource(
                        id = if (isTorchOn) R.drawable.ic_flashlight_on else R.drawable.ic_flashlight_off
                    ),
                    contentDescription = if (isTorchOn) "Flashlight On" else "Flashlight Off",
                    tint = Color.Unspecified // Retain original icon color
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Spacer to push buttons apart

            // Close button
            IconButton(onClick = onClosePreview) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "Close Preview",
                    tint = Color.Unspecified // Retain original icon color
                )
            }
        }
    }
}

/**
 * Draws an overlay for QR code scanning, including a dimmed background and a transparent ROI (Region of Interest).
 * Also includes red corner markers around the ROI.
 */
fun DrawScope.drawQrRoiOverlay() {
    // Dim the entire screen
    val overlayColor = Color.Black.copy(alpha = 0.6f)
    drawRect(color = overlayColor)

    // Calculate the ROI dimensions
    val roiMargin = 40.dp.toPx()
    val roiSize = size.width - (roiMargin * 2)
    val roiLeft = roiMargin
    val roiTop = (size.height - roiSize) / 2
    val roiRight = roiLeft + roiSize
    val roiBottom = roiTop + roiSize

    // Clear the ROI area
    drawRect(
        color = Color.Transparent,
        topLeft = Offset(roiLeft, roiTop),
        size = androidx.compose.ui.geometry.Size(roiSize, roiSize),
        blendMode = androidx.compose.ui.graphics.BlendMode.Clear
    )

    // Draw corner lines around the ROI
    val cornerLength = 30.dp.toPx()
    val cornerStroke = 4.dp.toPx()
    val cornerColor = Color.Red

    // Top-left corner
    drawLine(color = cornerColor, start = Offset(roiLeft, roiTop), end = Offset(roiLeft + cornerLength, roiTop), strokeWidth = cornerStroke)
    drawLine(color = cornerColor, start = Offset(roiLeft, roiTop), end = Offset(roiLeft, roiTop + cornerLength), strokeWidth = cornerStroke)

    // Top-right corner
    drawLine(color = cornerColor, start = Offset(roiRight, roiTop), end = Offset(roiRight - cornerLength, roiTop), strokeWidth = cornerStroke)
    drawLine(color = cornerColor, start = Offset(roiRight, roiTop), end = Offset(roiRight, roiTop + cornerLength), strokeWidth = cornerStroke)

    // Bottom-left corner
    drawLine(color = cornerColor, start = Offset(roiLeft, roiBottom), end = Offset(roiLeft + cornerLength, roiBottom), strokeWidth = cornerStroke)
    drawLine(color = cornerColor, start = Offset(roiLeft, roiBottom), end = Offset(roiLeft, roiBottom - cornerLength), strokeWidth = cornerStroke)

    // Bottom-right corner
    drawLine(color = cornerColor, start = Offset(roiRight, roiBottom), end = Offset(roiRight - cornerLength, roiBottom), strokeWidth = cornerStroke)
    drawLine(color = cornerColor, start = Offset(roiRight, roiBottom), end = Offset(roiRight, roiBottom - cornerLength), strokeWidth = cornerStroke)
}
