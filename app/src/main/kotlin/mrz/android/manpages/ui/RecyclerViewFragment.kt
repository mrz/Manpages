package mrz.android.manpages.ui

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

public class RecyclerViewFragment(val layoutId: Int, val listId: Int = android.R.id.list): Fragment() {

    var mAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>? = null
    var mList : RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutId, container, false)

        mList = view.findViewById(listId) as RecyclerView?

        mList?.getItemAnimator()?.setAddDuration(1000);
        mList?.getItemAnimator()?.setChangeDuration(1000);
        mList?.getItemAnimator()?.setMoveDuration(1000);
        mList?.getItemAnimator()?.setRemoveDuration(1000);

        return view
    }

    fun setListAdapter(adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>) {
        mAdapter = adapter
        mList?.setAdapter(mAdapter)
    }

    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        mList?.setLayoutManager(layoutManager)
    }

    fun setItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
        mList?.addItemDecoration(itemDecoration)
    }
}

