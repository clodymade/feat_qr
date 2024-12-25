/**
 * File: HiQrCodeItem.kt
 * Description: Composable function for displaying a single QR code's details in a card layout.
 *
 * Author: netcanis
 * Created: 2024-11-19
 *
 * License: MIT
 *
 * References:
 * - Jetpack Compose Basics: https://developer.android.com/jetpack/compose
 * - Date Formatting in Kotlin: https://developer.android.com/reference/java/text/SimpleDateFormat
 */

package com.mwkg.qr.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mwkg.qr.model.HiQrCode
import java.text.SimpleDateFormat
import java.util.*

/**
 * Displays a single QR code's content and the time it was scanned in a card format.
 *
 * @param qrCode The HiQrCode object containing the QR content and scanned timestamp.
 */
@Composable
fun HiQrCodeItem(qrCode: HiQrCode) {
    // Formatter for converting the scanned timestamp into a readable date format
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val scannedDate = dateFormatter.format(Date(qrCode.scannedAt))

    // Card layout to display QR code details
    Card(
        modifier = Modifier
            .fillMaxWidth() // Makes the card span the full width of the parent
            .padding(8.dp)  // Adds spacing around the card
    ) {
        // Column layout for arranging text elements vertically
        Column(modifier = Modifier.padding(16.dp)) {
            // Display QR code content
            Text(text = "QR Content: ${qrCode.content}")
            // Display the formatted scan date
            Text(text = "Scanned At: $scannedDate")
        }
    }
}
