package com.inglass.android.utils.barcodescanner.barcodescanner

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import androidx.annotation.GuardedBy
import androidx.camera.core.ExperimentalGetImage
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskExecutors
import com.google.mlkit.vision.common.InputImage
import com.inglass.android.presentation.scan.overlay.ScannerOverlayImpl
import com.inglass.android.utils.barcodescanner.FrameMetadata
import com.inglass.android.utils.barcodescanner.PreferenceUtils
import com.inglass.android.utils.barcodescanner.ScopedExecutor
import com.inglass.android.utils.barcodescanner.VisionImageProcessor
import java.lang.Math.max
import java.lang.Math.min
import java.nio.ByteBuffer
import java.util.*

abstract class VisionProcessorBase<T>(context: Context) : VisionImageProcessor {

    private var activityManager: ActivityManager =
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    private val fpsTimer = Timer()
    private val executor = ScopedExecutor(TaskExecutors.MAIN_THREAD)

    // Whether this processor is already shut down
    private var isShutdown = false

    // Used to calculate latency, running in the same thread, no sync needed.
    private var numRuns = 0
    private var totalFrameMs = 0L
    private var maxFrameMs = 0L
    private var minFrameMs = Long.MAX_VALUE
    private var totalDetectorMs = 0L
    private var maxDetectorMs = 0L
    private var minDetectorMs = Long.MAX_VALUE

    // Frame count that have been processed so far in an one second interval to calculate FPS.
    private var frameProcessedInOneSecondInterval = 0
    private var framesPerSecond = 0

    // To keep the latest images and its metadata.
    @GuardedBy("this")
    private var latestImage: ByteBuffer? = null

    @GuardedBy("this")
    private var latestImageMetaData: FrameMetadata? = null

    // To keep the images and metadata in process.
    @GuardedBy("this")
    private var processingImage: ByteBuffer? = null

    @GuardedBy("this")
    private var processingMetaData: FrameMetadata? = null

    init {
        fpsTimer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    framesPerSecond = frameProcessedInOneSecondInterval
                    frameProcessedInOneSecondInterval = 0
                }
            },
            0,
            1000
        )
    }

    override fun processImageBitmap(image: Bitmap, scannerOverlayImpl: ScannerOverlayImpl) {
        val inputImage = InputImage.fromBitmap(image, 0)
        val frameStartMs = SystemClock.elapsedRealtime()
        if (isShutdown) {
            return
        }

        setUpListener(
            task = detectInImage(inputImage),
            scannerOverlay = scannerOverlayImpl,
            frameStartMs = frameStartMs
        )
    }

    private fun setUpListener(
        task: Task<T>,
        scannerOverlay: ScannerOverlayImpl,
        shouldShowFps: Boolean = true,
        frameStartMs: Long
    ): Task<T> {
        val detectorStartMs = SystemClock.elapsedRealtime()

        return task
            .addOnSuccessListener(
                executor,
                OnSuccessListener { results: T ->
                    val endMs = SystemClock.elapsedRealtime()
                    val currentFrameLatencyMs = endMs - frameStartMs
                    val currentDetectorLatencyMs = endMs - detectorStartMs
                    if (numRuns >= 500) {
                        resetLatencyStats()
                    }
                    numRuns++
                    frameProcessedInOneSecondInterval++
                    totalFrameMs += currentFrameLatencyMs
                    maxFrameMs = max(currentFrameLatencyMs, maxFrameMs)
                    minFrameMs = min(currentFrameLatencyMs, minFrameMs)
                    totalDetectorMs += currentDetectorLatencyMs
                    maxDetectorMs = max(currentDetectorLatencyMs, maxDetectorMs)
                    minDetectorMs = min(currentDetectorLatencyMs, minDetectorMs)

                    if (frameProcessedInOneSecondInterval == 1) {
                        val mi = ActivityManager.MemoryInfo()
                        activityManager.getMemoryInfo(mi)
                    }
                    scannerOverlay.drawGreenRect = false
                    if (!PreferenceUtils.shouldHideDetectionInfo(scannerOverlay.context)) {
                        this@VisionProcessorBase.onSuccess(
                            results, scannerOverlay, CameraXInfo(
                                currentFrameLatencyMs,
                                currentDetectorLatencyMs,
                                if (shouldShowFps) framesPerSecond else null
                            )
                        )

                    } else {
                        this@VisionProcessorBase.onSuccess(results, scannerOverlay, null)
                    }
                    scannerOverlay.postInvalidate()
                }
            )
            .addOnFailureListener(
                executor,
                OnFailureListener { e: Exception ->
                    e.printStackTrace()
                    this@VisionProcessorBase.onFailure(e)
                }
            )
    }

    override fun stop() {
        executor.shutdown()
        isShutdown = true
        resetLatencyStats()
        fpsTimer.cancel()
    }

    private fun resetLatencyStats() {
        numRuns = 0
        totalFrameMs = 0
        maxFrameMs = 0
        minFrameMs = Long.MAX_VALUE
        totalDetectorMs = 0
        maxDetectorMs = 0
        minDetectorMs = Long.MAX_VALUE
    }

    protected abstract fun detectInImage(image: InputImage): Task<T>

    protected abstract fun onSuccess(results: T, scannerOverlay: ScannerOverlayImpl, info: CameraXInfo?)

    protected abstract fun onFailure(e: Exception)

}
