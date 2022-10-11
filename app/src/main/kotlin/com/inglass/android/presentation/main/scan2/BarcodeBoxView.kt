package com.inglass.android.presentation.main.scan2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View


class BarcodeBoxView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            this(context, attrs, defStyleAttr, 0)

    private val paint = Paint()
    private val rects = mutableListOf<RectF>()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val cornerRadius = 10f

        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        paint.strokeWidth = 5f

        rects.forEach { rect ->
            canvas?.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
        }
    }

    fun clearRects() {
        rects.clear()
        invalidate()
        requestLayout()
    }

    fun addRect(rect: RectF) {
        rects.add(rect)
        invalidate()
        requestLayout()
    }
}