package com.inglass.android.presentation.main.scan2

import android.annotation.SuppressLint
import android.graphics.Rect
import android.graphics.RectF
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import app.inglass.tasker.data.db.AppDatabase
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.inglass.android.data.local.db.entities.ScanResult
import java.util.*

class BarcodeAnalyzer(
    private val barcodeBoxView: BarcodeBoxView,
    private val dataBase: AppDatabase?,
    private val previewViewWidth: Float,
    private val previewViewHeight: Float
) : ImageAnalysis.Analyzer {

    /**
     * This parameters will handle preview box scaling
     */
    private var scaleX = 1f
    private var scaleY = 1f

    private fun translateX(x: Float) = x * scaleX
    private fun translateY(y: Float) = y * scaleY

    private fun adjustBoundingRect(rect: Rect) = RectF(
        translateX(rect.left.toFloat()),
        translateY(rect.top.toFloat()),
        translateX(rect.right.toFloat()),
        translateY(rect.bottom.toFloat())
    )

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        val img = image.image
        if (img != null) {
            scaleX = previewViewWidth / img.height.toFloat()
            scaleY = previewViewHeight / img.width.toFloat()

            val inputImage = InputImage.fromMediaImage(img, image.imageInfo.rotationDegrees)
            val options = BarcodeScannerOptions.Builder().build()
            val scanner = BarcodeScanning.getClient(options)

            scanner.process(inputImage).addOnSuccessListener { barcodes ->
                barcodeBoxView.clearRects()
                for (barcode in barcodes) {
                    println("barcode ${barcode.rawValue}")
                    barcode.boundingBox?.let { rect ->
                        barcodeBoxView.addRect(adjustBoundingRect(rect))
                        val scanResult = ScanResult(
                            id = 1,
                            barcode = barcode.rawValue!!,
                            operation = "Резка",
                            dateAndTime = Calendar.getInstance().time,
                            employee = 1,
                        )
                        dataBase?.scanResultsDao()?.insertScanResult(scanResult)
                    }
                }
            }
        }

        image.close()
    }
}