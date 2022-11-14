package com.inglass.android.presentation.scan.overlay

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Size
import android.view.View
import androidx.core.graphics.withScale
import androidx.core.graphics.withTranslation
import com.google.mlkit.vision.text.Text
import com.inglass.android.utils.ui.px
import kotlin.math.min

private const val MAX_WIDTH_PORTRAIT = 1200f

class ScannerOverlayImpl @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), ScannerOverlay {

    private var imageWidth = 0
    private var imageHeight = 0

    private val transparentPaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
    }

    private val strokePaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
            strokeWidth = context.px(3f)
            style = Paint.Style.STROKE
        }
    }

    var drawGreenRect: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    private var graphicBlocks: List<GraphicBlock>? = null

    init {
        setWillNotDraw(false)
    }

    fun setHeightAndWidth(height: Int, width: Int) {
        imageHeight = height
        imageWidth = width
    }

    fun getImageWidth() = imageWidth

    fun getImageHeight() = imageHeight

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.parseColor("#88000000"))

        val radius = context.px(4f)
        val rectF = scanRect
        canvas.drawRoundRect(rectF, radius, radius, transparentPaint)
        strokePaint.color = if (drawGreenRect) Color.GREEN else Color.WHITE
        canvas.drawRoundRect(rectF, radius, radius, strokePaint)

        graphicBlocks?.forEach { block ->
            val scaleX = scanRect.width() / block.bitmapSize.width
            val scaleY = scanRect.height() / block.bitmapSize.height

            canvas.withTranslation(scanRect.left, scanRect.top) {
                withScale(scaleX, scaleY) {
                    drawRoundRect(RectF(block.textBlock.boundingBox), radius, radius, strokePaint)
                }
            }
        }
        graphicBlocks = null
    }

    override val size: Size
        get() = Size(width, height)

    override val scanRect: RectF
        get() {
            val size = min(width * 0.6f, MAX_WIDTH_PORTRAIT)
            val l = (width - size) / 2
            val r = width - l
            val t = height * 0.15f
            val b = t + size
            return RectF(l, t, r, b)
        }

    data class GraphicBlock(val textBlock: BlockWrapper, val bitmapSize: Size)
}

class BlockWrapper(gmsTextBLock: Text.TextBlock? = null) {

    val boundingBox: Rect = gmsTextBLock?.boundingBox ?: Rect(0, 0, 0, 0)
    val text: String = gmsTextBLock?.text ?: ""
}