/**
 * File: HiQrCodeListScreen.kt
 * Description: Composable function to display a list of scanned QR codes in a lazy column layout.
 *              Includes a top app bar for navigation.
 *
 * Author: netcanis
 * Created: 2024-11-19
 *
 * License: MIT
 *
 * References:
 * - Jetpack Compose LazyColumn: https://developer.android.com/jetpack/compose/lists
 * - Material3 Design: https://developer.android.com/jetpack/compose/designsystems/material3
 */

package com.mwkg.qr.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mwkg.qr.model.HiQrCode
import kotlinx.coroutines.flow.StateFlow

/**
 * Displays a list of scanned QR codes in a scrollable column layout.
 * Includes a top app bar for navigation.
 *
 * @param qrCodes A StateFlow containing the list of scanned QR codes.
 * @param onBackPressed Callback triggered when the back button is pressed.
 */
@OptIn(ExperimentalMaterial3Api::class) // Opting in to Material3 experimental API
@Composable
fun HiQrCodeListScreen(
    qrCodes: StateFlow<List<HiQrCode>>,
    onBackPressed: () -> Unit
) {
    // Observing the current state of the QR code list
    val qrCodeList = qrCodes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QR Code List") }, // Title of the screen
                navigationIcon = {
                    IconButton(onClick = onBackPressed) { // Back button in the app bar
                        Icon(Icons.Default.Close, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        // LazyColumn to efficiently display a scrollable list of QR codes
        LazyColumn(
            contentPadding = PaddingValues(16.dp), // Padding around the content
            modifier = Modifier.padding(paddingValues) // Apply Scaffold's padding
        ) {
            // Display each QR code in the list using HiQrCodeItem
            items(qrCodeList.value) { qrCode ->
                HiQrCodeItem(qrCode)
            }
        }
    }
}
