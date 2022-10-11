package com.inglass.android.utils.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.inglass.android.R

private const val COLOR_FOR_FILL = "#FF97A2"
private const val ROUNDING = 20F
private const val DEFAULT_COLOR = "#FFFFFF"

class SegmentedLoadingBar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attributeSet, defStyle) {

    private var segments = mutableListOf<Segment>()

    private var value: Float? = null

    private var rectBounds: RectF
    private var segmentRect: RectF

    private var fillPaint: Paint

    private var gapWidth = 0 //Ширина между сегментами
    private var segmentHeight = 0

    private val segmentsCount = 50

    init {
        segmentHeight = resources.getDimensionPixelSize(R.dimen.segment_height)
        gapWidth = resources.getDimensionPixelSize(R.dimen.segment_gap_width)

        fillPaint = Paint()
        fillPaint.style = Paint.Style.FILL
        rectBounds = RectF()
        segmentRect = RectF()

        for (i in 0..segmentsCount) {
            segments.add(Segment())
        }
    }

    fun setFill(pos: Int, start: Int, finish: Int) {
        val max = finish - start
        val currentPos = pos * segmentsCount / max
        for (i in 0..currentPos) {
            val emptySegment = segments[i]
            if (emptySegment.color != Color.parseColor(COLOR_FOR_FILL)) {
                val filledSegment = emptySegment.copy(color = Color.parseColor(COLOR_FOR_FILL))
                segments[i] = filledSegment
                invalidate()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val segmentsSize = segments.size

        for (segmentIndex in 0 until segmentsSize) {
            val segment = segments[segmentIndex]
            drawSegment(canvas, segment, segmentIndex, segmentsSize)
        }
    }

    private fun drawSegment(canvas: Canvas, segment: Segment, segmentIndex: Int, segmentsSize: Int) {
        val singleSegmentWidth = (getContentWidth() + gapWidth) / segmentsSize - gapWidth
        val segmentLeft = (singleSegmentWidth + gapWidth) * segmentIndex
        val segmentRight = segmentLeft + singleSegmentWidth

        rectBounds.set(
            segmentLeft.toFloat() + paddingLeft,
            paddingTop.toFloat(),
            segmentRight + paddingLeft.toFloat(),
            segmentHeight + paddingTop.toFloat()
        )
        fillPaint.color = segment.color

        canvas.drawRoundRect(rectBounds, ROUNDING, ROUNDING, fillPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val minWidth = paddingLeft + paddingRight
        val minHeight = segmentHeight + paddingBottom + paddingTop
        val width = resolveSizeAndState(minWidth, widthMeasureSpec, 0)
        val height = resolveSizeAndState(minHeight, heightMeasureSpec, 0)
        setMeasuredDimension(width, height)
    }

    private fun getContentWidth(): Int {
        return width - paddingLeft - paddingRight
    }

    data class Segment(
        val color: Int = Color.parseColor(DEFAULT_COLOR)
    )
}
