package com.inglass.android.utils.barcodescanner.barcodescanner

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.inglass.android.presentation.scan.overlay.ScannerOverlayImpl
import com.inglass.android.utils.barcodescanner.GraphicOverlay
import timber.log.Timber

private const val TAG = "BarcodeProcessor"

class BarcodeScannerProcessor(
    context: Context,
    private val scanned: Set<String>,
    private val onScanned: (String) -> Unit,
    private val onScannedVibrate: () -> Unit,
    private val onScannedMusic: () -> Unit
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

    override fun onSuccess(results: List<Barcode>, scannerOverlay: ScannerOverlayImpl) {
        results.forEach { barcode ->
            if (barcode.rawValue in scanned) {
                scannerOverlay.drawBlueRect = true
            } else {
                barcode.rawValue?.let { onScanned(it) }
                onScannedVibrate()
                onScannedMusic()
//                scannerOverlay.drawBlueRect
//                scannerOverlay.drawBlueRect = false
//                scannerOverlay.drawBlueRect
            }
        }
    }

    override fun onFailure(e: Exception) {
        Timber.e("Barcode detection failed $e")
    }
}
