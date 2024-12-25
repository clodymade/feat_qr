/**
 * File: HiQrCode.kt
 * Description: Data class representing a QR code and its associated metadata.
 *
 * Author: netcanis
 * Created: 2024-11-19
 *
 * License: MIT
 */

package com.mwkg.qr.model

/**
 * Represents a scanned QR code.
 *
 * @property content The content of the QR code (e.g., text, URL).
 * @property scannedAt The timestamp (in Unix format) when the QR code was scanned.
 */
data class HiQrCode(
    val content: String, // Content of the QR code
    val scannedAt: Long  // Timestamp of when the QR code was scanned (Unix time)
)