package com.inglass.android.utils.base.toolbar

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.inglass.android.databinding.ToolbarBinding
import com.inglass.android.utils.base.toolbar.ToolbarIconType.ICON_BACK
import com.inglass.android.utils.navigation.safePopBackStack
import com.inglass.android.utils.ui.doOnClick
import com.inglass.android.utils.ui.dp

const val TOOLBAR_COLOR_CHANGE_VALUE = 100f

class ToolbarConfig(
    var toolbar: ToolbarBinding,
    private var homeDrawableRes: ToolbarIconType? = null,
    private var firstIconDrawableRes: ToolbarIconType? = null,
    private var secondIconDrawableRes: ToolbarIconType? = null,
    var title: String = "",
    var minScrollOffset: Int = 2.dp()
) {

    init {
        toolbar.homeButton = homeDrawableRes?.drawable?.getDrawable()
        toolbar.firstIcon = firstIconDrawableRes?.drawable?.getDrawable()
        toolbar.secondIcon = secondIconDrawableRes?.drawable?.getDrawable()
        toolbar.title = title
        setupStandardNavigation()
    }

    private fun setupStandardNavigation() {
        if (homeDrawableRes == ICON_BACK) {
            setHomeButtonListener {
                toolbar.root.findNavController().safePopBackStack()
            }
        }
    }

    //Функции смена цвета иконок
    fun setHomeButtonTint(@ColorInt color: Int) {
        if (homeDrawableRes == null) return
        toolbar.homeImageView.setColorFilter(color)
    }

    fun setFirstIconTint(@ColorInt color: Int) {
        if (firstIconDrawableRes == null) return
        toolbar.firstIconImageView.setColorFilter(color)
    }

    fun setSecondIconTint(@ColorInt color: Int) {
        if (secondIconDrawableRes == null) return
        toolbar.secondIconImageView.setColorFilter(color)
    }

    //Функции кликов
    fun setHomeButtonListener(listener: () -> Unit) {
        if (homeDrawableRes == null) return
        toolbar.homeImageView.doOnClick(listener)
    }

    fun setFirstIconListener(listener: () -> Unit) {
        if (firstIconDrawableRes == null) return
        toolbar.firstIconImageView.doOnClick(listener)
    }

    fun setSecondIconListener(listener: () -> Unit) {
        if (secondIconDrawableRes == null) return
        toolbar.secondIconImageView.doOnClick(listener)
    }

    fun observeScroll(scrollContainer: View) {
        scrollContainer.setOnScrollChangeListener { _, _, y, _, _ ->
            toolbar.divider.alpha = (y / minScrollOffset).toFloat()
        }
    }

    fun observeRecyclerScroll(recyclerView: RecyclerView) {
        recyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
            toolbar.divider.alpha = (recyclerView.computeVerticalScrollOffset() / minScrollOffset).toFloat()
        }
    }

    fun changeColorWhenRecyclerScroll(
        recyclerView: RecyclerView,
        @ColorInt changeBackgroundColor: Int,
        @ColorInt changeIconColor: Int,
        @ColorInt startIconColor: Int
    ) {
        recyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
            if (recyclerView.computeVerticalScrollOffset() > minScrollOffset) {
                changeToolbarAndIconsColor(changeIconColor)
                toolbar.viewColorBackground.setBackgroundColor(changeBackgroundColor)
            } else {
                changeToolbarAndIconsColor(startIconColor)
            }
            toolbar.divider.alpha =
                recyclerView.computeVerticalScrollOffset() / TOOLBAR_COLOR_CHANGE_VALUE
            toolbar.viewColorBackground.alpha =
                recyclerView.computeVerticalScrollOffset() / TOOLBAR_COLOR_CHANGE_VALUE
        }
    }

    fun changeToolbarAndIconsColorWhenScrolling(
        scrollView: View,
        @ColorInt changeBackgroundColor: Int,
        @ColorInt changeIconColor: Int,
        @ColorInt startIconColor: Int
    ) {
        scrollView.setOnScrollChangeListener { _, _, y, _, _ ->
            if (y > minScrollOffset) {
                changeToolbarAndIconsColor(changeIconColor)
                toolbar.viewColorBackground.setBackgroundColor(changeBackgroundColor)
            } else {
                changeToolbarAndIconsColor(startIconColor)
            }
            toolbar.divider.alpha = y / TOOLBAR_COLOR_CHANGE_VALUE
            toolbar.viewColorBackground.alpha = y / TOOLBAR_COLOR_CHANGE_VALUE
        }
    }

    fun changeColorWhenScrolling(scrollContainer: View, colorChangeValue: Float) {
        scrollContainer.setOnScrollChangeListener { _, _, y, _, _ ->
            toolbar.viewColorBackground.alpha = y / colorChangeValue
            toolbar.divider.alpha = (y / minScrollOffset).toFloat()
        }
    }

    //Функции работы с тайтлом тулбара
    fun setTitleColor(@ColorInt color: Int) {
        if (title.isNotEmpty() && title.isNotBlank()) {
            toolbar.titleTextView.setTextColor(color)
        }
    }

    fun setTextStyle(typeface: Int) {
        if (title.isNotEmpty() && title.isNotBlank()) {
            toolbar.titleTextView.setTypeface(toolbar.titleTextView.typeface, typeface)
        }
    }

    fun setBackgroundColor(color: Int) {
        toolbar.viewColorBackground.alpha = 1f
        toolbar.viewColorBackground.setBackgroundColor(
            ContextCompat.getColor(
                toolbar.toolbarParentLayout.context,
                color
            )
        )
    }

    private fun Int.getDrawable(): Drawable? {
        return AppCompatResources.getDrawable(toolbar.root.context, this)
    }

    fun changeToolbarAndIconsColorWhenScrolling(
        scrollContainer: View,
        @ColorInt changeIconColor: Int,
        changeToolbarColorValue: Float,
        @ColorInt startIconColor: Int,
    ) {
        scrollContainer.setOnScrollChangeListener { _, _, y, _, _ ->
            if (y > minScrollOffset) {
                changeToolbarAndIconsColor(changeIconColor)
            } else {
                changeToolbarAndIconsColor(startIconColor)
            }
            toolbar.divider.alpha = y / changeToolbarColorValue
            toolbar.viewColorBackground.alpha = y / changeToolbarColorValue
        }
    }

    private fun changeToolbarAndIconsColor(@ColorInt color: Int) {
        setHomeButtonTint(color)
        setFirstIconTint(color)
        setSecondIconTint(color)
        setTitleColor(color)
    }
}
