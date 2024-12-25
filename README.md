# **feat_qr**

A **feature module** for QR code scanning and management on Android.

---

## **Overview**

`feat_qr` is an Android module that simplifies QR code scanning and management:
- Displays a camera preview for scanning QR codes using CameraX.
- Uses ML Kit for real-time QR code detection.
- Manages scanned QR codes and displays them in a modern UI with **Jetpack Compose**.

This module is compatible with **Android 11 (API 30)** and above, leveraging **Kotlin Coroutines** for asynchronous operations.

---

## **Features**

- ✅ **QR Code Scanning**: Real-time detection and scanning of QR codes.
- ✅ **Torch Control**: Built-in support for toggling the camera flashlight.
- ✅ **Modern UI**: Displays scanned QR codes in a sleek Compose-based UI.
- ✅ **Reactive State Management**: Uses ViewModel and StateFlow for dynamic updates.
- ✅ **Modular Design**: Lightweight and easy-to-integrate module.

---

## **Requirements**

| Requirement        | Minimum Version         |
|--------------------|-------------------------|
| **Android OS**     | 11 (API 30)             |
| **Kotlin**         | 1.9.22                  |
| **Android Studio** | Giraffe (2022.3.1)      |
| **Gradle**         | 8.0                     |

---

## **Setup**

### **1. Add feat_qr to Your Project**

Include `feat_qr` as a module in your project. Add the following to your `settings.gradle` file:

```gradle
include ':feat_qr'
```

Then, add it as a dependency in your app module’s build.gradle file:
```gradle
implementation project(":feat_qr")
```

### **2. Permissions**

Add the required permissions to your AndroidManifest.xml:

```xml
<!-- Camera Permissions -->  
<uses-permission android:name="android.permission.CAMERA" />  

<!-- Camera Hardware Feature -->  
<uses-feature android:name="android.hardware.camera" android:required="true" />  
```

Ensure runtime permissions are handled in your app:

```kotlin
val permissions = arrayOf(  
    android.Manifest.permission.CAMERA  
)  

ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)  
```

---

## **Usage**

### **1. Start QR Code Scanning**

Integrate the scanner into your activity:

```kotlin
HiQrScanner.start(activity = this, previewView = previewView) { result ->  
    when (result) {  
        is HiResult.HiQrResult -> {  
            println("QR Code Content: ${result.qrData}")  
        }  
        else -> {  
            println("Error: ${result.error}")  
        }  
    }  
}  
```

### **2. Stop QR Code Scanning**

Stop scanning when it’s no longer needed:

```kotlin
HiQrScanner.stop()
```

### **3. Toggle Torch (Flashlight)**

Control the flashlight during scanning:

```kotlin
HiQrScanner.toggleTorch(true) // Turn on  
HiQrScanner.toggleTorch(false) // Turn off  
```

---

## **HiQrResult**

The QR scan results are encapsulated in the HiQrResult class. Key properties include:

| Property          | Type             | Description                         |
|-------------------|------------------|-------------------------------------|
| qrData            | String           | QR scan result string.              |
| error             | String           | Error message, if any.              |

---

## **Example UI**

To display scanned QR codes in a Jetpack Compose-based UI:

```kotlin
@Composable  
fun HiQrCodeListScreen(  
    qrCodes: StateFlow<List<HiQrCode>>,  
    onBackPressed: () -> Unit  
) {  
    val qrCodeList by qrCodes.collectAsState()  

    Scaffold(  
        topBar = {  
            TopAppBar(  
                title = { Text("QR Code List") },  
                navigationIcon = {  
                    IconButton(onClick = onBackPressed) {  
                        Icon(Icons.Default.Close, contentDescription = "Back")  
                    }  
                }  
            )  
        }  
    ) { paddingValues ->  
        LazyColumn(  
            contentPadding = PaddingValues(16.dp),  
            modifier = Modifier.padding(paddingValues)  
        ) {  
            items(qrCodeList) { qrCode ->  
                HiQrCodeItem(qrCode)  
            }  
        }  
    }  
}  
```

---

## **License**

feat_qr is available under the MIT License. See the LICENSE file for details.

---

## **Contributing**

Contributions are welcome! To contribute:

1. Fork this repository.
2. Create a feature branch:
```
git checkout -b feature/your-feature
```
3. Commit your changes:
```
git commit -m "Add feature: description"
```
4. Push to the branch:
```
git push origin feature/your-feature
```
5. Submit a Pull Request.

---

## **Author**

### **netcanis**
iOS GitHub: https://github.com/netcanis
Android GitHub: https://github.com/clodymade

---

