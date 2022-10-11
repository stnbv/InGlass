package com.inglass.android.presentation.barcodescanner

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Camera.CameraInfo
import android.os.Build.VERSION_CODES
import android.preference.PreferenceManager
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.camera.core.CameraSelector
import com.google.common.base.Preconditions
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.objects.ObjectDetectorOptionsBase.DetectorMode
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions.Builder
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.inglass.android.R
import com.inglass.android.R.string

@SuppressLint("InlinedApi")
val CAMERA_FACING_BACK = CameraInfo.CAMERA_FACING_BACK

@SuppressLint("InlinedApi")
val CAMERA_FACING_FRONT = CameraInfo.CAMERA_FACING_FRONT

object PreferenceUtils {
    private val POSE_DETECTOR_PERFORMANCE_MODE_FAST = 1
    fun saveString(context: Context, @StringRes prefKeyId: Int, value: String?) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(context.getString(prefKeyId), value)
            .apply()
    }

    @RequiresApi(VERSION_CODES.LOLLIPOP)
    fun getCameraXTargetResolution(context: Context, lensfacing: Int): Size? {
        Preconditions.checkArgument(
            (
                    lensfacing == CameraSelector.LENS_FACING_BACK
                            || lensfacing == CameraSelector.LENS_FACING_FRONT)
        )
        val prefKey =
            if (lensfacing == CameraSelector.LENS_FACING_BACK) context.getString(string.pref_key_camerax_rear_camera_target_resolution) else context.getString(
                string.pref_key_camerax_front_camera_target_resolution
            )
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        try {
            return Size.parseSize(sharedPreferences.getString(prefKey, null))
        } catch (e: Exception) {
            return null
        }
    }

    fun shouldHideDetectionInfo(context: Context): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val prefKey = context.getString(string.pref_key_info_hide)
        return sharedPreferences.getBoolean(prefKey, false)
    }

    private fun getObjectDetectorOptions(
        context: Context,
        @StringRes prefKeyForMultipleObjects: Int,
        @StringRes prefKeyForClassification: Int,
        @DetectorMode mode: Int
    ): ObjectDetectorOptions {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val enableMultipleObjects = sharedPreferences.getBoolean(context.getString(prefKeyForMultipleObjects), false)
        val enableClassification = sharedPreferences.getBoolean(context.getString(prefKeyForClassification), true)
        val builder = ObjectDetectorOptions.Builder().setDetectorMode(mode)
        if (enableMultipleObjects) {
            builder.enableMultipleObjects()
        }
        if (enableClassification) {
            builder.enableClassification()
        }
        return builder.build()
    }

    private fun getCustomObjectDetectorOptions(
        context: Context,
        localModel: LocalModel,
        @StringRes prefKeyForMultipleObjects: Int,
        @StringRes prefKeyForClassification: Int,
        @DetectorMode mode: Int
    ): CustomObjectDetectorOptions {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val enableMultipleObjects = sharedPreferences.getBoolean(context.getString(prefKeyForMultipleObjects), false)
        val enableClassification = sharedPreferences.getBoolean(context.getString(prefKeyForClassification), true)
        val builder = Builder(localModel).setDetectorMode(mode)
        if (enableMultipleObjects) {
            builder.enableMultipleObjects()
        }
        if (enableClassification) {
            builder.enableClassification().setMaxPerObjectLabelCount(1)
        }
        return builder.build()
    }

    fun isCameraLiveViewportEnabled(context: Context): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val prefKey = context.getString(R.string.pref_key_camera_live_viewport)
        return sharedPreferences.getBoolean(prefKey, false)
    }

}
