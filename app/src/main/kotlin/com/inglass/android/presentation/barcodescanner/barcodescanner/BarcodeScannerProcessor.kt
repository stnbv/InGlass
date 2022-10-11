/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.inglass.android.presentation.barcodescanner.barcodescanner

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import app.inglass.tasker.data.db.AppDatabase
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.inglass.android.App
import com.inglass.android.data.local.db.entities.ScanResult
import com.inglass.android.presentation.main.scan2.GraphicOverlay
import java.util.*

class BarcodeScannerProcessor(context: Context) : VisionProcessorBase<List<Barcode>>(context) {

  private val options = BarcodeScannerOptions.Builder()
    .setBarcodeFormats(
      Barcode.FORMAT_QR_CODE,
      Barcode.FORMAT_EAN_8,
      Barcode.FORMAT_EAN_13,
      Barcode.FORMAT_CODE_128,
      Barcode.FORMAT_DATA_MATRIX,
    )
    .build()

  private val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient(options)
  private val db = AppDatabase.getInstance(context)

  override fun stop() {
    super.stop()
    barcodeScanner.close()
  }

  override fun detectInImage(image: InputImage): Task<List<Barcode>> {
    return barcodeScanner.process(image)
  }

  override fun onSuccess(barcodes: List<Barcode>, graphicOverlay: GraphicOverlay) {
    for (i in barcodes.indices) {
      val barcode = barcodes[i]
      if (App.scanResSet.contains(barcode.rawValue.toString())) {
        graphicOverlay.add(BarcodeGraphic(graphicOverlay, barcode, Color.GREEN))
        continue
      }

      App.scanResSet.add(barcode.rawValue.toString())
      graphicOverlay.add(BarcodeGraphic(graphicOverlay, barcode))
      logExtrasForTesting(barcode)
      saveBarcode(barcode.rawValue.toString())
    }
  }

  override fun onFailure(e: Exception) {
    Log.e(TAG, "Barcode detection failed $e")
  }

  @SuppressLint("NewApi")
  fun saveBarcode(barcode: String) {
    val currentTime = Calendar.getInstance().time
    var scanResult = ScanResult(
      barcode = barcode,
      hasUploaded = false,
      employee = 1,
      operation = App.currentOperation,
      dateAndTime = currentTime
    )
    db?.scanResultsDao()?.insertScanResult(scanResult)

//    HomeActivity.adapter.refreshView(db?.scanResultsDao()?.getLast2001()!!)
//    HomeActivity.adapter.notifyDataSetChanged()
  }

  companion object {
    private const val TAG = "BarcodeProcessor"

    private fun logExtrasForTesting(barcode: Barcode?) {
      if (barcode != null) {
        Log.v(
          MANUAL_TESTING_LOG,
          "barcode raw value: " + barcode.rawValue
        )
      }
    }
  }
}
