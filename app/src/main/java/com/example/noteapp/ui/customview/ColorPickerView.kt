package com.example.noteapp.ui.customview

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.Dimension
import androidx.annotation.Dimension.Companion.DP
import com.example.noteapp.mvvm.model.data.entity.Color
import com.example.noteapp.utils.AndroidColorGetter
import com.example.noteapp.utils.dip

class ColorPickerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val PALETTE_ANIMATION_DURATION = 150L
        private const val HEIGHT = "height"
        private const val SCALE = "scale"
        @Dimension(DP) private const val COLOR_VIEW_PADDING = 8
    }

    var onColorClickListener: (color: Color) -> Unit = { }
    val isOpen: Boolean
        get() = measuredHeight > 0
    private var desiredHeight = 0

    private val colorGetter by lazy { AndroidColorGetter(context) }
    private val animator by lazy { ValueAnimator().apply {
            duration = PALETTE_ANIMATION_DURATION
            addUpdateListener(updateListener)
        }
    }

    private val updateListener by lazy {
        ValueAnimator.AnimatorUpdateListener { animator ->
            layoutParams.apply {
                height = animator.getAnimatedValue(HEIGHT) as Int
            }.let { layoutParams = it }
            val scaleFactor = animator.getAnimatedValue(SCALE) as Float
            for (i in 0 until childCount) {
                getChildAt(i).apply {
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                    alpha = scaleFactor
                }
            }
        }
    }

    init {
        colorGetter.getColors().forEach { color ->
            addView(
                ColorCircleView(context).apply {
                    fillColorRes = colorGetter.getColorRes(color)
                    dip(COLOR_VIEW_PADDING).let { setPadding(it.toInt(), it.toInt(), it.toInt(), it.toInt()) }
                    setOnClickListener { onColorClickListener(color) }
                }
            )
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        layoutParams.apply {
            desiredHeight = height
            height = 0
        }.let {
            layoutParams = it
        }
    }

    fun open() {
        animator.cancel()
        animator.setValues(
            PropertyValuesHolder.ofInt(HEIGHT, measuredHeight, desiredHeight),
            PropertyValuesHolder.ofFloat(SCALE, getChildAt(0).scaleX, 1f),
        )
        animator.start()
    }

    fun close() {
        animator.cancel()
        animator.setValues(
            PropertyValuesHolder.ofInt(HEIGHT, measuredHeight, 0),
            PropertyValuesHolder.ofFloat(SCALE, getChildAt(0).scaleX, 0f),
        )
        animator.start()
    }
}