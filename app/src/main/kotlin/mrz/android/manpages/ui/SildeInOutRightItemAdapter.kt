package mrz.android.manpages.ui

import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.View

public class SlideInOutRightItemAnimator(recyclerView: RecyclerView) : RecyclerView.ItemAnimator() {
    override fun isRunning(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        throw UnsupportedOperationException()
    }

    override fun runPendingAnimations() {
        throw UnsupportedOperationException()
    }

    override fun endAnimation(item: RecyclerView.ViewHolder?) {
        throw UnsupportedOperationException()
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        throw UnsupportedOperationException()
    }

    override fun animateMove(holder: RecyclerView.ViewHolder?, fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        throw UnsupportedOperationException()
    }

    override fun animateChange(oldHolder: RecyclerView.ViewHolder?, newHolder: RecyclerView.ViewHolder?, fromLeft: Int, fromTop: Int, toLeft: Int, toTop: Int): Boolean {
        throw UnsupportedOperationException()
    }

    override fun endAnimations() {
        throw UnsupportedOperationException()
    }
}

