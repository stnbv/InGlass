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
import com.inglass.android.presentation.main.scan2.GraphicOverlay

private const val TAG = "BarcodeProcessor"

class BarcodeScannerProcessor(
    context: Context,
    private val scanned: Set<String>,
    private val onScanned: (String) -> Unit
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

    override fun onSuccess(barcodes: List<Barcode>, graphicOverlay: GraphicOverlay) {
        barcodes.forEach { barcode ->
            if (barcode.rawValue in scanned) {
                graphicOverlay.add(BarcodeGraphic(graphicOverlay, barcode, Color.GREEN))
            } else {
                barcode.rawValue?.let { onScanned(it) }
                graphicOverlay.add(BarcodeGraphic(graphicOverlay, barcode))
            }
        }
    }

    override fun onFailure(e: Exception) {
        Log.e(TAG, "Barcode detection failed $e")
    }

}
