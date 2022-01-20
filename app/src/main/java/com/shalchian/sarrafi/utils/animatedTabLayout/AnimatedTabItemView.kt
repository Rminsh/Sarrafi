package com.shalchian.sarrafi.utils.animatedTabLayout

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewOutlineProvider
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class AnimatedTabItemView(context: Context, attrs: AttributeSet? = null)
    : LinearLayout(context, attrs) {

    enum class State {
        COLLAPSED, EXPANDED
    }

    private var mSize: Float = 0.0f

    private var fromColor: Int = 0
    private var toColor: Int = 0

    private val iconFromColor: Int = ContextCompat.getColor(context, com.shalchian.sarrafi.R.color.atl_icon_from_color)
    private val iconToColor: Int = ContextCompat.getColor(context, com.shalchian.sarrafi.R.color.atl_icon_to_color)

    private var path: Path? = null
    private var rectF: RectF? = null
    private var maskPaint: Paint? = null
    private var state: State = State.COLLAPSED

    private var sizeAnimator: ValueAnimator? = null
    private var layoutColorAnimator: ValueAnimator? = null
    private var iconColorAnimator: ValueAnimator? = null
    private var animatorSet: AnimatorSet? = null

    private var mWidth: Float = 0.0f
    private var mHeight: Float = 0.0f
    private var radius: Float = 0.0f

    private var imageView: ImageView? = null

    init {
        path = Path()
        rectF = RectF(0f, 0f, 0f, 0f)
        maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        maskPaint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        gravity = Gravity.CENTER

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            outlineProvider = ClipOutlineProvider()
            clipToOutline = true
        }

        val imageViewLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        imageView = ImageView(context)
        imageView?.layoutParams = imageViewLayoutParams
        addView(imageView)

        setLayerType(LAYER_TYPE_HARDWARE, null)
        setBackgroundColor(fromColor)
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(mWidth.toInt(), mHeight.toInt())
    }

    override fun dispatchDraw(canvas: Canvas?) {
        val save = canvas!!.save()
        super.dispatchDraw(canvas)
        canvas.restoreToCount(save)
        path?.let {
            canvas.drawPath(it, maskPaint!!)
        }
    }

    override fun draw(canvas: Canvas?) {
        path?.let {
            canvas?.clipPath(it)
        }
        super.draw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path?.reset()
        rectF?.let { path?.addRoundRect(it, radius, radius, Path.Direction.CCW) }
    }

    fun setFromColor(fromColor: Int) {
        this.fromColor = fromColor
        setBackgroundColor(fromColor)
        requestLayout()
    }

    fun setToColor(toColor: Int) {
        this.toColor = toColor
        requestLayout()
    }

    fun setDrawable(drawable: Drawable?) {
        drawable?.colorFilter = PorterDuffColorFilter(iconFromColor, PorterDuff.Mode.SRC_IN)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageView?.background = drawable
        } else {
            imageView?.setBackgroundDrawable(drawable)
        }
    }

    fun setItemSize(mSize: Float) {
        initializeSize(mSize)
        initializeAnimators()
        requestLayout()
    }

    fun expand() {
        if (state == State.EXPANDED) {
            return
        }

        state = State.EXPANDED
        sizeAnimator?.setFloatValues(mSize, mSize * 2)
        layoutColorAnimator?.setObjectValues(fromColor, toColor)
        iconColorAnimator?.setObjectValues(iconFromColor, iconToColor)
        animatorSet?.start()
    }

    fun collapse() {
        if (state == State.COLLAPSED) {
            return
        }

        state = State.COLLAPSED
        sizeAnimator?.setFloatValues(mSize * 2, mSize)
        layoutColorAnimator?.setObjectValues(toColor, fromColor)
        iconColorAnimator?.setObjectValues(iconToColor, iconFromColor)
        animatorSet?.start()
    }

    private fun initializeAnimators() {
        createSizeAnimator()
        createColorAnimator()
        createAnimatorSet()
    }

    private fun initializeSize(mSize: Float) {
        this.mSize = mSize
        mWidth = mSize
        mHeight = mSize
        radius = mSize / 2
        rectF?.set(0f, 0f, mWidth, mHeight)
    }

    private fun createSizeAnimator() {
        sizeAnimator = ValueAnimator.ofFloat(0f, 0f).apply {
            duration = animDuration
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation -> calculateBorders(animation.animatedValue as Float) }
        }
    }

    private fun createColorAnimator() {
        layoutColorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor).apply {
            duration = animDuration
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                setBackgroundColor(animation.animatedValue as Int)
                postInvalidate()
            }
        }

        iconColorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), iconFromColor, iconToColor)
            .apply {
                duration = animDuration
                interpolator = DecelerateInterpolator()
                addUpdateListener { animation ->
                    val drawable = imageView?.background
                    drawable?.colorFilter = PorterDuffColorFilter(animation.animatedValue as Int, PorterDuff.Mode.SRC_IN)
                    postInvalidate()
                }
            }
    }

    private fun createAnimatorSet() {
        animatorSet = AnimatorSet().apply { playTogether(sizeAnimator, layoutColorAnimator, iconColorAnimator) }
    }

    private fun calculateBorders(value: Float) {
        val params = layoutParams
        params.width = value.toInt()
        layoutParams = params
        mWidth = value
        mHeight = (mSize + ((value - mSize) * multiplier)).toFloat()
        radius = mHeight / 2
        rectF?.set(0f, 0f, mWidth, mHeight)
        postInvalidate()
    }

    companion object {
        //constants
        const val animDuration: Long = 300
        const val multiplier: Double = 0.15
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private inner class ClipOutlineProvider : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, mWidth.toInt(),
                    mHeight.toInt(), radius)
        }
    }
}