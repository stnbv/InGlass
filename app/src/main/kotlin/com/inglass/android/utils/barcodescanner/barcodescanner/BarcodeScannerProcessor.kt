package com.inglass.android.utils.barcodescanner.barcodescanner

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.barcode.common.Barcode.FORMAT_CODABAR
import com.google.mlkit.vision.barcode.common.Barcode.FORMAT_CODE_128
import com.google.mlkit.vision.barcode.common.Barcode.FORMAT_DATA_MATRIX
import com.google.mlkit.vision.barcode.common.Barcode.FORMAT_EAN_13
import com.google.mlkit.vision.barcode.common.Barcode.FORMAT_EAN_8
import com.google.mlkit.vision.barcode.common.Barcode.FORMAT_QR_CODE
import com.google.mlkit.vision.common.InputImage
import com.inglass.android.R
import com.inglass.android.presentation.scan.overlay.ScannerOverlayImpl
import timber.log.Timber

const val REQUIRED_COUNT = 3

class BarcodeScannerProcessor(
    private val context: Context,
    private val scanned: Set<String>,
    private val onScanned: (String) -> Unit,
    private val setCameraInfo: (String) -> Unit
) : VisionProcessorBase<List<Barcode>>(context) {

    var lastBarcode: String = ""
    var counter: Int = 0

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            FORMAT_EAN_8,
            FORMAT_EAN_13,
            FORMAT_CODE_128,
            FORMAT_CODABAR,
            FORMAT_DATA_MATRIX,
            FORMAT_QR_CODE
        )
        .build()

    private val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient(options)

    override fun stop() {
        super.stop()
        barcodeScanner.close()
    }

    override fun detectInImage(image: InputImage): Task<List<Barcode>> {
        return barcodeScanner.process(image)
    }

    override fun onSuccess(results: List<Barcode>, scannerOverlay: ScannerOverlayImpl, info: CameraXInfo?) {
        if (info != null) {
            setCameraInfo(
                context.getString(
                    R.string.camera_info,
                    scannerOverlay.getImageWidth(),
                    scannerOverlay.getImageHeight(),
                    info.framesPerSecond,
                    info.currentFrameLatencyMs,
                    info.currentDetectorLatencyMs
                )
            )
        }

        scannerOverlay.drawGreenRect = results.firstOrNull()?.rawValue in scanned

        when {
            results.size != 1 -> {
                counter = 0
            }
            results.first().rawValue == lastBarcode && counter + 1 == REQUIRED_COUNT -> {
                onScanned(lastBarcode)
            }
            results.first().rawValue == lastBarcode -> {
                counter += 1
            }
            else -> {
                lastBarcode = results.first().rawValue ?: ""
                counter = 0
            }
        }
    }

    override fun onFailure(e: Exception) {
        Timber.e("Barcode detection failed $e")
    }
}

data class CameraXInfo(
    val currentFrameLatencyMs: Long,
    val currentDetectorLatencyMs: Long,
    val framesPerSecond: Int?
)
