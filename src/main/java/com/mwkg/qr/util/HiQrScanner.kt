/**
 * File: HiQrScanner.kt
 * Description: Utility object for scanning QR codes using the Android CameraX API and ML Kit Barcode Scanner.
 *              Handles camera lifecycle, frame analysis, and QR code data extraction.
 *
 * Author: netcanis
 * Created: 2024-11-19
 *
 * License: MIT
 *
 * References:
 * - CameraX API: https://developer.android.com/training/camerax
 * - ML Kit Barcode Scanning: https://developers.google.com/ml-kit/vision/barcode-scanning
 */

package com.mwkg.qr.util

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.util.Size
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.camera.view.PreviewView
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.mwkg.qr.model.HiQrResult
import com.mwkg.util.HiPermissionType
import com.mwkg.util.PermissionReqCodes
import com.mwkg.util.hiHasPermissions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * QR code scanner utility based on CameraX and ML Kit.
 * Handles camera preview, QR code detection, and lifecycle management.
 */
@SuppressLint("StaticFieldLeak")
object HiQrScanner {

    // Callback for delivering scan results
    private var callback: ((HiQrResult) -> Unit)? = null
    // Executor for handling camera background tasks
    private var cameraExecutor: ExecutorService? = null
    // ML Kit barcode scanner for detecting QR codes
    private lateinit var barcodeScanner: BarcodeScanner
    // Camera control for managing features like flash
    private var cameraControl: CameraControl? = null
    // Reference to the activity using this scanner
    private var activity: Activity? = null

    init {
        // Logs initialization of the QR scanner
        Log.d("ModularX::HiQrScanner", "QR Scanner initialized")
    }

    // Initializes camera resources and barcode scanner
    private fun initialize() {
        cameraExecutor = Executors.newSingleThreadExecutor() // Single-threaded executor for camera operations
        barcodeScanner = BarcodeScanning.getClient() // Initialize ML Kit barcode scanner
        Log.d("ModularX::HiQrScanner", "QR Scanner setup complete")
    }

    /**
     * Starts the QR scanner with a camera preview.
     *
     * @param activity The current activity context.
     * @param previewView The view for displaying the camera preview.
     * @param callback The callback for delivering scan results.
     */
    @OptIn(ExperimentalGetImage::class)
    fun start(activity: Activity, previewView: PreviewView, callback: (HiQrResult) -> Unit) {
        HiQrScanner.activity = activity
        HiQrScanner.callback = callback

        initialize() // Set up the scanner resources

        // Check for camera permissions
        val reqPermissions = HiPermissionType.CAMERA.requiredPermissions()
        if (!activity.hiHasPermissions(reqPermissions)) {
            activity.requestPermissions(reqPermissions, PermissionReqCodes.CAMERA)
            return
        }

        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)

        // Set up the camera provider and configure preview, analysis, and selection
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .setTargetRotation(previewView.display.rotation) // Align preview with display rotation
                .build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setTargetRotation(previewView.display.rotation) // Align analyzer with display rotation
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST) // Process only the latest frame
                .build().also {
                    it.setAnalyzer(cameraExecutor!!) { imageProxy -> // Pass each frame to the analyzer
                        processImageProxy(imageProxy)
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll() // Unbind previous use cases
                val camera = cameraProvider.bindToLifecycle(
                    activity as LifecycleOwner, cameraSelector, preview, imageAnalyzer
                )
                cameraControl = camera.cameraControl // Initialize camera control
            } catch (exc: Exception) {
                Log.e("ModularX::HiQrScanner", "Failed to bind camera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(activity))
    }

    // Stops the QR scanner and releases resources
    fun stop() {
        cameraExecutor?.shutdown() // Shut down the camera executor if initialized
        Log.d("ModularX::HiQrScanner", "QR scanning stopped")
    }

    // Checks if the required permissions are granted
    fun hasRequiredPermissions(): Boolean {
        return activity?.let {
            val reqPermissions = HiPermissionType.CAMERA.requiredPermissions()
            it.hiHasPermissions(reqPermissions)
        } ?: false
    }

    /**
     * Toggles the camera's torch (flashlight).
     *
     * @param isOn True to turn the torch on, false to turn it off.
     */
    fun toggleTorch(isOn: Boolean) {
        cameraControl?.enableTorch(isOn)
    }

    /**
     * Processes a single image frame to detect QR codes.
     *
     * @param imageProxy The image frame captured by the camera.
     */
    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            barcodeScanner.process(inputImage)
                .addOnSuccessListener { barcodes -> // Process detected barcodes
                    for (barcode in barcodes) {
                        handleBarcodeResult(barcode)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("ModularX::HiQrScanner", "QR scan failed: ${e.message}")
                }
                .addOnCompleteListener {
                    imageProxy.close() // Close the image frame
                }
        }
    }

    /**
     * Handles the result of a detected QR code.
     *
     * @param barcode The detected barcode containing QR code data.
     */
    private fun handleBarcodeResult(barcode: Barcode) {
        val qrResult = barcode.displayValue ?: ""
        if (qrResult.isNotEmpty()) {
            callback?.let { it(HiQrResult(qrResult, "")) }
        } else {
            callback?.let { it(HiQrResult("", "QR code could not be recognized.")) }
        }
    }
}
