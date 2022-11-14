package com.inglass.android.presentation.scan

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.res.Configuration
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
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
import com.inglass.android.utils.barcodescanner.BitmapUtils
import com.inglass.android.utils.barcodescanner.BitmapUtils.getBitmap
import com.inglass.android.utils.barcodescanner.FrameMetadata
import com.inglass.android.utils.barcodescanner.PreferenceUtils
import com.inglass.android.utils.barcodescanner.VisionImageProcessor
import com.inglass.android.utils.barcodescanner.barcodescanner.BarcodeScannerProcessor
import com.inglass.android.utils.base.BaseFragment
import com.inglass.android.utils.ui.getCropRectAccordingToRotation
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindAllCameraUseCases()

        }, ContextCompat.getMainExecutor(requireContext()))

        viewModel.onScannedFlow observe {
            vibration()
            music()
        }
    }

    private fun setCameraInfo(text: String) {
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

        if (cameraProvider == null) {
            return
        }
        if (previewUseCase != null) {
            cameraProvider!!.unbind(previewUseCase)
        }

        val builder = Preview.Builder()
        val targetResolution =
            PreferenceUtils.getCameraXTargetResolution(requireContext())?.let { res ->
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> Size(res.height, res.width)
                    else -> Size(res.width, res.height)
                }
            }
        if (targetResolution != null) {
            builder.setTargetResolution(targetResolution)
        }
        previewUseCase = builder.build()
        previewUseCase!!.setSurfaceProvider(binding.previewView.surfaceProvider) //выше есть проверки на нулабельность
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
            { code -> viewModel.checkBarcode(code) },
            { setCameraInfo(it) }
        )

        val builder = ImageAnalysis.Builder()
        val targetResolution =
            PreferenceUtils.getCameraXTargetResolution(requireContext())?.let { res ->
                when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> Size(res.height, res.width)
                    else -> Size(res.width, res.height)
                }
            }
        if (targetResolution != null) {
            builder.setTargetResolution(targetResolution)
        }
        analysisUseCase = builder.build()

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

                val byteArray = BitmapUtils.yuv420toNV21(image)
                val bitmap = getBitmap(byteArray, FrameMetadata(cropRect.width(), cropRect.height(), rotation))

                imageProcessor!!.processImageBitmap(bitmap, binding.barcodeScannerZone)

                imageProxy.close()
            } catch (e: Exception) {
                Timber.e(e)
            }

        }
        cameraProvider!!.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, analysisUseCase)
    }

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
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = requireContext().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            requireContext().getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        val canVibrate: Boolean = vibrator.hasVibrator()
        val milliseconds = 500L

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

data class ScannerRectToPreviewViewRelation(
    val relativePosX: Float,
    val relativePosY: Float,
    val relativeWidth: Float,
    val relativeHeight: Float
)