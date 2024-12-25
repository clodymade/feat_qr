/**
 * File: HiQrResult.kt
 *
 * Description: This sealed class defines various scan result types, including QR codes, NFC tags, BLE devices, Beacons, and OCR data.
 *              Each result class contains relevant details such as scanned data, error messages, and specific attributes.
 *
 * Author: netcanis
 * Created: 2024-11-19
 *
 * License: MIT
 *
 * References:
 * - Android BLE Overview: https://developer.android.com/guide/topics/connectivity/bluetooth/ble-overview
 */

package com.mwkg.qr.model

/**
 * Represents the result of a QR scan.
 *
 * @property qrData The data string scanned from the QR code.
 * @property error The error message that occurred during scanning.
 */
data class HiQrResult(
    val qrData: String,
    val error: String = ""
)
