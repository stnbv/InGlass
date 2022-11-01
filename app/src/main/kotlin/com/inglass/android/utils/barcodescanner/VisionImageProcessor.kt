package com.inglass.android.utils.barcodescanner

import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import com.google.mlkit.common.MlKitException
import com.inglass.android.presentation.scan.overlay.ScannerOverlayImpl
import java.nio.ByteBuffer

/** An interface to process the images with different vision detectors and custom image models.  */
interface VisionImageProcessor {

    /** Processes ImageProxy image data, e.g. used for CameraX live preview case.  */
    @Throws(MlKitException::class)
    fun processImageProxy(image: Bitmap, scannerOverlayImpl: ScannerOverlayImpl)

    /** Stops the underlying machine learning model and release resources.  */
    fun stop()
}