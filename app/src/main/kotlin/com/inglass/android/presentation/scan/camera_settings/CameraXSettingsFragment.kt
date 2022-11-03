package com.inglass.android.presentation.scan.camera_settings

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.camera.core.CameraSelector
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import com.inglass.android.R
import com.inglass.android.R.string
import com.inglass.android.R.xml
import com.inglass.android.utils.barcodescanner.PreferenceUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LivePreviewPreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(xml.fragment_camerax_settings)
        setUpCameraPreferences()
    }

    fun setUpCameraPreferences() {
        val cameraPreference = findPreference<PreferenceCategory>(getString(R.string.pref_category_key_camera))
        setUpCameraXTargetAnalysisSizePreference(
            string.pref_key_camerax_rear_camera_target_resolution, CameraSelector.LENS_FACING_BACK
        )
    }

    private fun setUpCameraXTargetAnalysisSizePreference(
        @StringRes previewSizePrefKeyId: Int, lensFacing: Int
    ) {
        val pref = findPreference<ListPreference>(getString(previewSizePrefKeyId))
        val cameraCharacteristics = getCameraCharacteristics(lensFacing)
        val entries: Array<String?>
        if (cameraCharacteristics != null) {
            val map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            val outputSizes = map!!.getOutputSizes(SurfaceTexture::class.java)
            entries = arrayOfNulls(outputSizes.size)
            for (i in outputSizes.indices) {
                entries[i] = outputSizes[i].toString()
            }
        } else {
            entries = arrayOf(
                "2000x2000",
                "1600x1600",
                "1200x1200",
                "1000x1000",
                "800x800",
                "600x600",
                "400x400",
                "200x200",
                "100x100"
            )
        }
        pref!!.entries = entries
        pref.entryValues = entries
        pref.summary = if (pref.entry == null) "Default" else pref.entry
        pref.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue: Any? ->
                val newStringValue = newValue as String?
                pref.summary = newStringValue
                PreferenceUtils.saveString(requireContext(), previewSizePrefKeyId, newStringValue)
                true
            }
    }

    fun getCameraCharacteristics(
        lensFacing: Int
    ): CameraCharacteristics? {
        val cameraManager = requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val cameraList = Arrays.asList(*cameraManager.cameraIdList)
            for (availableCameraId in cameraList) {
                val availableCameraCharacteristics = cameraManager.getCameraCharacteristics(availableCameraId!!)
                val availableLensFacing = availableCameraCharacteristics.get(CameraCharacteristics.LENS_FACING)
                    ?: continue
                if (availableLensFacing == lensFacing) {
                    return availableCameraCharacteristics
                }
            }
        } catch (e: CameraAccessException) {
            // Accessing camera ID info got error
        }
        return null
    }


//
//    fun setUpCameraPreferences() {
//        val cameraPreference = findPreference<PreferenceCategory>(getString(R.string.pref_category_key_camera))
//        val mainCameraTargetResolutionPreference =
//            findPreference<ListPreference>(getString(R.string.pref_key_camerax_rear_camera_target_resolution)) as Preference
//        cameraPreference?.removePreference(mainCameraTargetResolutionPreference)
//        setUpCameraPreviewSizePreference(
//            R.string.pref_key_rear_camera_preview_size,
//            R.string.pref_key_rear_camera_picture_size,
//            CAMERA_FACING_BACK
//        )
//    }
//
//    private fun setUpCameraPreviewSizePreference(
//        @StringRes previewSizePrefKeyId: Int, @StringRes pictureSizePrefKeyId: Int, cameraId: Int
//    ) {
//        val previewSizePreference = findPreference(getString(previewSizePrefKeyId)) as? ListPreference
//        var camera: Camera? = null
//        try {
//            camera = Camera.open(cameraId)
//            val previewSizeList: List<SizePair> = CameraSource.generateValidPreviewSizeList(camera)
//            val previewSizeStringValues = arrayOfNulls<String>(previewSizeList.size)
//            val previewToPictureSizeStringMap: MutableMap<String, String> = HashMap()
//            for (i in previewSizeList.indices) {
//                val sizePair: SizePair = previewSizeList[i]
//                previewSizeStringValues[i] = sizePair.preview.toString()
//                if (sizePair.picture != null) {
//                    previewToPictureSizeStringMap[sizePair.preview.toString()] = sizePair.picture.toString()
//                }
//            }
//            previewSizePreference?.entries = previewSizeStringValues
//            previewSizePreference?.entryValues = previewSizeStringValues
//            if (previewSizePreference?.entry == null) {
//                // First time of opening the Settings page.
//                val sizePair: SizePair = CameraSource.selectSizePair(
//                    camera,
//                    DEFAULT_REQUESTED_CAMERA_PREVIEW_WIDTH,
//                    DEFAULT_REQUESTED_CAMERA_PREVIEW_HEIGHT
//                ) ?: return
//                val previewSizeString: String = sizePair.preview.toString()
//                previewSizePreference?.value = previewSizeString
//                previewSizePreference?.summary = previewSizeString
//                PreferenceUtils.saveString(
//                    requireContext(),
//                    pictureSizePrefKeyId,
//                    if (sizePair.picture != null) sizePair.picture.toString() else null
//                )
//            } else {
//                previewSizePreference?.summary = previewSizePreference?.entry
//            }
//            previewSizePreference?.onPreferenceChangeListener =
//                Preference.OnPreferenceChangeListener { _, newValue: Any ->
//                    val newPreviewSizeStringValue = newValue as String
//                    previewSizePreference?.summary = newPreviewSizeStringValue
//                    PreferenceUtils.saveString(
//                        requireContext(),
//                        pictureSizePrefKeyId,
//                        previewToPictureSizeStringMap[newPreviewSizeStringValue]
//                    )
//                    true
//                }
//        } catch (e: RuntimeException) {
//            if (previewSizePreference != null) {
//                (findPreference<PreferenceCategory>(getString(R.string.pref_category_key_camera)))
//                    ?.removePreference(previewSizePreference)
//            }
//        } finally {
//            camera?.release()
//        }
//    }
}