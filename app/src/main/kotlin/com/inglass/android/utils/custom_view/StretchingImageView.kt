package com.inglass.android.utils.custom_view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.max

class StretchingImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) :
    AppCompatImageView(context, attrs, defStyle) {

    private var maxViewWidth = 0
    private var maxViewHeight = 0

    init {
        scaleType = ScaleType.MATRIX
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        recomputeImgMatrix()
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        recomputeImgMatrix()
        return super.setFrame(l, t, r, b)
    }

    private fun recomputeImgMatrix() {
        if (drawable == null) return
        val matrix = imageMatrix
        val viewWidth = width - paddingLeft - paddingRight
        val viewHeight = height - paddingTop - paddingBottom
        val drawableWidth = drawable.intrinsicWidth
        val drawableHeight = drawable.intrinsicHeight
        maxViewWidth = max(maxViewWidth, viewWidth)
        maxViewHeight = max(maxViewHeight, viewHeight)
        val scaleX = maxViewWidth.toFloat() / drawableWidth
        val scaleY = maxViewHeight.toFloat() / drawableHeight
        matrix.setScale(scaleX, scaleY)
        imageMatrix = matrix
    }
}
