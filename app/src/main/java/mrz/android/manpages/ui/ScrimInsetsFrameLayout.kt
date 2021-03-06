package mrz.android.manpages.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.widget.FrameLayout
import mrz.android.manpages.R

/**
 * A layout that draws something in the insets passed to [.fitSystemWindows], i.e. the area above UI chrome
 * (status and navigation bars, overlay action bars).
 */
public class ScrimInsetsFrameLayout : FrameLayout {
    private var mInsetForeground: Drawable? = null

    private var mInsets: Rect? = null
    private val mTempRect = Rect()
    private var mOnInsetsCallback: OnInsetsCallback? = null

    public constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    public constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0)
    }

    public constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs, defStyle)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ScrimInsetsView, defStyle, 0)
        if (a == null) {
            return
        }
        mInsetForeground = a.getDrawable(R.styleable.ScrimInsetsView_insetForeground)
        a.recycle()

        setWillNotDraw(true)
    }

    override fun fitSystemWindows(insets: Rect): Boolean {
        mInsets = Rect(insets)
        setWillNotDraw(mInsetForeground == null)
        ViewCompat.postInvalidateOnAnimation(this)
        if (mOnInsetsCallback != null) {
            mOnInsetsCallback!!.onInsetsChanged(insets)
        }
        return true // consume insets
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        val width = getWidth()
        val height = getHeight()
        if (mInsets != null && mInsetForeground != null) {
            val sc = canvas.save()
            canvas.translate(getScrollX().toFloat(), getScrollY().toFloat())

            // Top
            mTempRect.set(0, 0, width, mInsets!!.top)
            mInsetForeground!!.setBounds(mTempRect)
            mInsetForeground!!.draw(canvas)

            // Bottom
            mTempRect.set(0, height - mInsets!!.bottom, width, height)
            mInsetForeground!!.setBounds(mTempRect)
            mInsetForeground!!.draw(canvas)

            // Left
            mTempRect.set(0, mInsets!!.top, mInsets!!.left, height - mInsets!!.bottom)
            mInsetForeground!!.setBounds(mTempRect)
            mInsetForeground!!.draw(canvas)

            // Right
            mTempRect.set(width - mInsets!!.right, mInsets!!.top, width, height - mInsets!!.bottom)
            mInsetForeground!!.setBounds(mTempRect)
            mInsetForeground!!.draw(canvas)

            canvas.restoreToCount(sc)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (mInsetForeground != null) {
            mInsetForeground!!.setCallback(this)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mInsetForeground != null) {
            mInsetForeground!!.setCallback(null)
        }
    }

    /**
     * Allows the calling container to specify a callback for custom processing when insets change (i.e. when
     * [.fitSystemWindows] is called. This is useful for setting padding on UI elements based on
     * UI chrome insets (e.g. a Google Map or a ListView). When using with ListView or GridView, remember to set
     * clipToPadding to false.
     */
    public fun setOnInsetsCallback(onInsetsCallback: OnInsetsCallback) {
        mOnInsetsCallback = onInsetsCallback
    }

    public trait OnInsetsCallback {
        public fun onInsetsChanged(insets: Rect)
    }
}