package com.inglass.android.presentation.scan

import android.annotation.SuppressLint
import android.content.Context.VIBRATOR_SERVICE
import android.graphics.ImageFormat
import android.graphics.Rect
import android.media.Image
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Size
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.inglass.android.R
import com.inglass.android.databinding.FragmentCameraXBinding
import com.inglass.android.presentation.scan.CameraXFragment.ScannerRectToPreviewViewRelation
import com.inglass.android.utils.barcodescanner.BitmapUtils.getBitmap
import com.inglass.android.utils.barcodescanner.FrameMetadata
import com.inglass.android.utils.barcodescanner.PreferenceUtils
import com.inglass.android.utils.barcodescanner.VisionImageProcessor
import com.inglass.android.utils.barcodescanner.barcodescanner.BarcodeScannerProcessor
import com.inglass.android.utils.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CameraXFragment :
    BaseFragment<FragmentCameraXBinding, CameraXViewModel>(R.layout.fragment_camerax) {

    override val viewModel: CameraXViewModel by viewModels()

    private var cameraProvider: ProcessCameraProvider? = null
    private var previewUseCase: Preview? = null
    private var analysisUseCase: ImageAnalysis? = null
    private var imageProcessor: VisionImageProcessor? = null
    private var needUpdateGraphicOverlayImageSourceInfo = false
    private var lensFacing = CameraSelector.LENS_FACING_BACK

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(
            {
                cameraProvider = cameraProviderFuture.get()
                bindAllCameraUseCases()

            }, ContextCompat.getMainExecutor(requireContext())
        )
    }


    fun setCameraInfo(text: String) {
        binding.cameraInfoTextView.text = text
    }

    override fun onResume() {
        super.onResume()
        bindAllCameraUseCases()
    }

    override fun onPause() {
        super.onPause()
        imageProcessor?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        imageProcessor?.stop()
    }

    private fun bindAllCameraUseCases() {
        if (cameraProvider != null) {
            cameraProvider!!.unbindAll()
            bindPreviewUseCase()
            bindAnalysisUseCase()
        }
    }

    private fun bindPreviewUseCase() {
//        if (!PreferenceUtils.isCameraLiveViewportEnabled(requireContext())) {
//            return //TODO Проверить для чего это, если не закоментить черный экран вместо камеры
//        }
        if (cameraProvider == null) {
            return
        }
        if (previewUseCase != null) {
            cameraProvider!!.unbind(previewUseCase)
        }

        val builder = Preview.Builder()
        val targetResolution = PreferenceUtils.getCameraXTargetResolution(requireContext(), lensFacing)
        if (targetResolution != null) {
            builder.setTargetResolution(targetResolution)
        }
        previewUseCase = builder.build()
        previewUseCase!!.setSurfaceProvider(binding.previewView.surfaceProvider)
        cameraProvider!!.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, previewUseCase)
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun bindAnalysisUseCase() {
        if (cameraProvider == null) {
            return
        }
        if (analysisUseCase != null) {
            cameraProvider!!.unbind(analysisUseCase)
        }
        if (imageProcessor != null) {
            imageProcessor!!.stop()
        }

        imageProcessor = BarcodeScannerProcessor(
            requireContext(),
            viewModel.scanResSet,
            { viewModel.checkBarcode(it) },
            { vibration() },
            { music() },
            { setCameraInfo(it) })

        val builder = ImageAnalysis.Builder()
        val targetResolution = PreferenceUtils.getCameraXTargetResolution(requireContext(), lensFacing)
        if (targetResolution != null) {
            builder.setTargetResolution(targetResolution)
        }
        analysisUseCase = builder.build()

        needUpdateGraphicOverlayImageSourceInfo = true

        analysisUseCase?.setAnalyzer(
            ContextCompat.getMainExecutor(requireContext())
        ) { imageProxy: ImageProxy ->
            try {
                binding.barcodeScannerZone.setHeightAndWidth(imageProxy.height, imageProxy.width)
                val rotation = imageProxy.imageInfo.rotationDegrees
                val scannerRect =
                    getScannerRectToPreviewViewRelation(Size(imageProxy.width, imageProxy.height), rotation)

                val image = imageProxy.image!!
                val cropRect = image.getCropRectAccordingToRotation(scannerRect, rotation)
                image.cropRect = cropRect

                val byteArray = YuvNV21Util.yuv420toNV21(image)
                val bitmap = getBitmap(byteArray, FrameMetadata(cropRect.width(), cropRect.height(), rotation))

                imageProcessor!!.processImageProxy(bitmap, binding.barcodeScannerZone)

//                onBitmapPrepared(bitmap)

                imageProxy.close()
            } catch (e: Exception) {
                Timber.e(e)
            }

        }
        cameraProvider!!.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, analysisUseCase)
    }

    data class ScannerRectToPreviewViewRelation(
        val relativePosX: Float,
        val relativePosY: Float,
        val relativeWidth: Float,
        val relativeHeight: Float
    )

    private fun getScannerRectToPreviewViewRelation(proxySize: Size, rotation: Int): ScannerRectToPreviewViewRelation {
        return when (rotation) {
            0, 180 -> {
                val size = binding.barcodeScannerZone.size
                val width = size.width
                val height = size.height
                val previewHeight = width / (proxySize.width.toFloat() / proxySize.height)
                val heightDeltaTop = (previewHeight - height) / 2

                val scannerRect = binding.barcodeScannerZone.scanRect
                val rectStartX = scannerRect.left
                val rectStartY = heightDeltaTop + scannerRect.top

                ScannerRectToPreviewViewRelation(
                    rectStartX / width,
                    rectStartY / previewHeight,
                    scannerRect.width() / width,
                    scannerRect.height() / previewHeight
                )
            }
            90, 270 -> {
                val size = binding.barcodeScannerZone.size
                val width = size.width
                val height = size.height
                val previewWidth = height / (proxySize.width.toFloat() / proxySize.height)
                val widthDeltaLeft = (previewWidth - width) / 2

                val scannerRect = binding.barcodeScannerZone.scanRect
                val rectStartX = widthDeltaLeft + scannerRect.left
                val rectStartY = scannerRect.top

                ScannerRectToPreviewViewRelation(
                    rectStartX / previewWidth,
                    rectStartY / height,
                    scannerRect.width() / previewWidth,
                    scannerRect.height() / height
                )
            }
            else -> throw IllegalArgumentException("Rotation degree ($rotation) not supported!")
        }
    }


    private fun vibration() {
        val vibrator = requireContext().getSystemService(VIBRATOR_SERVICE) as Vibrator
        val canVibrate: Boolean = vibrator.hasVibrator()
        val milliseconds = 1000L

        if (canVibrate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // API 26
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        milliseconds,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(milliseconds)
            }
        }
    }

    private fun music() {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r = RingtoneManager.getRingtone(context, notification)
        r.play()
    }
}

private fun Image.getCropRectAccordingToRotation(scannerRect: ScannerRectToPreviewViewRelation, rotation: Int): Rect {
    return when (rotation) {
        0 -> {
            val startX = (scannerRect.relativePosX * this.width).toInt()
            val numberPixelW = (scannerRect.relativeWidth * this.width).toInt()
            val startY = (scannerRect.relativePosY * this.height).toInt()
            val numberPixelH = (scannerRect.relativeHeight * this.height).toInt()
            Rect(startX, startY, startX + numberPixelW, startY + numberPixelH)
        }
        90 -> {
            val startX = (scannerRect.relativePosY * this.width).toInt()
            val numberPixelW = (scannerRect.relativeHeight * this.width).toInt()
            val numberPixelH = (scannerRect.relativeWidth * this.height).toInt()
            val startY = height - (scannerRect.relativePosX * this.height).toInt() - numberPixelH
            Rect(startX, startY, startX + numberPixelW, startY + numberPixelH)
        }
        180 -> {
            val numberPixelW = (scannerRect.relativeWidth * this.width).toInt()
            val startX = (this.width - scannerRect.relativePosX * this.width - numberPixelW).toInt()
            val numberPixelH = (scannerRect.relativeHeight * this.height).toInt()
            val startY = (height - scannerRect.relativePosY * this.height - numberPixelH).toInt()
            Rect(startX, startY, startX + numberPixelW, startY + numberPixelH)
        }
        270 -> {
            val numberPixelW = (scannerRect.relativeHeight * this.width).toInt()
            val numberPixelH = (scannerRect.relativeWidth * this.height).toInt()
            val startX = (this.width - scannerRect.relativePosY * this.width - numberPixelW).toInt()
            val startY = (scannerRect.relativePosX * this.height).toInt()
            Rect(startX, startY, startX + numberPixelW, startY + numberPixelH)
        }
        else -> throw IllegalArgumentException("Rotation degree ($rotation) not supported!")
    }
}

object YuvNV21Util {

    fun yuv420toNV21(image: Image): ByteArray {
        val crop = image.cropRect
        val format = image.format
        val width = crop.width()
        val height = crop.height()
        val planes = image.planes
        val data =
            ByteArray(width * height * ImageFormat.getBitsPerPixel(format) / 8)
        val rowData = ByteArray(planes[0].rowStride)
        var channelOffset = 0
        var outputStride = 1
        for (i in planes.indices) {
            when (i) {
                0 -> {
                    channelOffset = 0
                    outputStride = 1
                }
                1 -> {
                    channelOffset = width * height + 1
                    outputStride = 2
                }
                2 -> {
                    channelOffset = width * height
                    outputStride = 2
                }
            }
            val buffer = planes[i].buffer
            val rowStride = planes[i].rowStride
            val pixelStride = planes[i].pixelStride
            val shift = if (i == 0) 0 else 1
            val w = width shr shift
            val h = height shr shift
            buffer.position(rowStride * (crop.top shr shift) + pixelStride * (crop.left shr shift))
            for (row in 0 until h) {
                var length: Int
                if (pixelStride == 1 && outputStride == 1) {
                    length = w
                    buffer[data, channelOffset, length]
                    channelOffset += length
                } else {
                    length = (w - 1) * pixelStride + 1
                    buffer[rowData, 0, length]
                    for (col in 0 until w) {
                        data[channelOffset] = rowData[col * pixelStride]
                        channelOffset += outputStride
                    }
                }
                if (row < h - 1) {
                    buffer.position(buffer.position() + rowStride - length)
                }
            }
        }
        return data
    }
}
