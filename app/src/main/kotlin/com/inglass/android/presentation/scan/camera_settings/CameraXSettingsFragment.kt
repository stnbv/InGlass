package com.inglass.android.presentation.scan.camera_settings

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.camera.core.CameraSelector
import androidx.preference.ListPreference
import androidx.preference.Preference
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
        setUpCameraXTargetAnalysisSizePreference(
            string.pref_key_camerax_rear_camera_target_resolution, CameraSelector.LENS_FACING_BACK
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view.setBackgroundColor(requireContext().getColor(R.color.dark_gray))
        return view
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

    private fun getCameraCharacteristics(
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
}