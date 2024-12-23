/**
 * File: HiQrCodeListViewModel.kt
 * Description: ViewModel for managing the list of scanned QR codes.
 *              Provides a reactive data stream for observing QR code updates.
 *
 * Author: netcanis
 * Created: 2024-11-19
 *
 * License: MIT
 *
 * References:
 * - ViewModel Overview: https://developer.android.com/topic/libraries/architecture/viewmodel
 * - Kotlin StateFlow: https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/
 */

package com.mwkg.qr.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.mwkg.qr.model.HiQrCode

/**
 * ViewModel for managing a list of scanned QR codes.
 * Ensures that the list is reactive and can be observed by the UI.
 */
class HiQrCodeListViewModel : ViewModel() {
    // MutableStateFlow to hold the list of QR codes (private to prevent direct modification)
    private val _codes = MutableStateFlow<List<HiQrCode>>(emptyList())

    // Publicly exposed StateFlow for observing changes to the QR code list
    val codes: StateFlow<List<HiQrCode>> get() = _codes

    /**
     * Updates the list of QR codes by either adding a new QR code
     * or replacing an existing one with the same content.
     *
     * @param code The HiQrCode object to add or update in the list.
     */
    fun update(code: HiQrCode) {
        val currentCodes = _codes.value.toMutableList()

        // Find an existing QR code with the same content
        val existingIndex = currentCodes.indexOfFirst {
            it.content == code.content
        }

        if (existingIndex >= 0) {
            // Replace the existing QR code if found
            currentCodes[existingIndex] = code
        } else {
            // Add the new QR code if not found
            currentCodes.add(code)
        }

        // Update the StateFlow with the new list (immutable copy)
        _codes.value = currentCodes.toList()
    }
}
