package mrz.android.manpages.ui

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

public open class RecyclerViewFragment(val layoutId: Int, val listId: Int = android.R.id.list): Fragment() {

    private var adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>? = null
    protected var list: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutId, container, false)

        list = view.findViewById(listId) as RecyclerView?

        list?.getItemAnimator()?.setAddDuration(1000);
        list?.getItemAnimator()?.setChangeDuration(1000);
        list?.getItemAnimator()?.setMoveDuration(1000);
        list?.getItemAnimator()?.setRemoveDuration(1000);

        return view
    }

    fun setListAdapter(adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>) {
        this.adapter = adapter
        list?.setAdapter(adapter)
    }

    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        list?.setLayoutManager(layoutManager)
    }

    fun setItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
        list?.addItemDecoration(itemDecoration)
    }
}

