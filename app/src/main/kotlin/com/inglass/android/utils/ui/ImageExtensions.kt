package com.inglass.android.utils.ui

import android.graphics.Rect
import android.media.Image
import com.inglass.android.presentation.scan.ScannerRectToPreviewViewRelation

fun Image.getCropRectAccordingToRotation(scannerRect: ScannerRectToPreviewViewRelation, rotation: Int): Rect {
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