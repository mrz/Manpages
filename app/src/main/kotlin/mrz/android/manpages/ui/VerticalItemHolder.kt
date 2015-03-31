package mrz.android.manpages.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import mrz.android.manpages.R

public class VerticalItemHolder(itemView: View, private val mAdapter: VerticalItemAdapter<*>) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private val mName: TextView

    {
        itemView.setOnClickListener(this)
        mName = itemView.findViewById(R.id.name) as TextView
    }

    override fun onClick(v: View) {
        mAdapter.onItemHolderClick(this)
    }

    public fun setName(name: CharSequence) {
        mName.setText(name)
    }
}