package mrz.android.manpages.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import mrz.android.manpages.R

/**
 * ItemDecoration implementation that applies an inset margin
 * around each child of the RecyclerView. It also draws item dividers
 * that are expected from a vertical list implementation, such as
 * ListView.
 * Original code: https://github.com/devunwired/recyclerview-playground
 */
public open class DividerDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val mDivider: Drawable

    private val mInsets: Int

    init {
        mDivider = context.getDrawable(R.drawable.faded_list_divider)
        mInsets = context.getResources().getDimensionPixelSize(R.dimen.card_insets)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        drawVertical(c, parent)
    }

    /** Draw dividers underneath each child view  */
    public fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.getPaddingLeft()
        val right = parent.getWidth() - parent.getPaddingRight()
        val childCount = parent.getChildCount()
        for (i in 0..childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.getLayoutParams() as RecyclerView.LayoutParams
            val top = child.getBottom() + params.bottomMargin + mInsets
            val bottom = top + mDivider.getIntrinsicHeight()
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        //We can supply forced insets for each item view here in the Rect
        outRect.set(mInsets, mInsets, mInsets, mInsets)
    }
}