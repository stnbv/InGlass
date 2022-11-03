package com.inglass.android.utils.barcodescanner.barcodescanner

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.inglass.android.presentation.scan.overlay.ScannerOverlayImpl
import timber.log.Timber

class BarcodeScannerProcessor(
    context: Context,
    private val scanned: Set<String>,
    private val onScanned: (String) -> Unit,
    private val setCameraInfo: (String) -> Unit
) : VisionProcessorBase<List<Barcode>>(context) {

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_CODE_128
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
        results.forEach { barcode ->
            if (barcode.rawValue in scanned) {
                scannerOverlay.drawGreenRect = true
            } else {
                barcode.rawValue?.let { onScanned(it) }
                scannerOverlay.drawGreenRect = false
            }

            if (info != null) { //TODO заменить на строковые ресурсы
                var text = "InputImage size: ${scannerOverlay.getImageWidth()}x${scannerOverlay.getImageHeight()}\n"
                text += if (info.shouldShowFps != null) {
                    "FPS: ${info.shouldShowFps}, Frame latency: ${info.currentFrameLatencyMs} ms\n"
                } else {
                    "Frame latency: ${info.currentFrameLatencyMs} ms\n"
                }
                text += "Detector latency: ${info.currentDetectorLatencyMs} ms"

                setCameraInfo(text)
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
    val shouldShowFps: Int?
)
