package mrz.android.manpages.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import mrz.android.manpages.R
import java.util.ArrayList
import java.util.Random

public open class VerticalItemAdapter<T>() : RecyclerView.Adapter<VerticalItemAdapter.VerticalItemHolder>() {
    private val mItems: ArrayList<T> = ArrayList()
    private var mOnItemClickListener: AdapterView.OnItemClickListener? = null

    /*
    * Inserting a new item at the head of the list. This uses a specialized
    * RecyclerView method, notifyItemInserted(), to trigger any enabled item
    * animations in addition to updating the view.
    */
    public fun addItem(what: T, position: Int) {
        if (position >= mItems.size()) return
        mItems.add(position, what)
        notifyItemInserted(position)
    }

    /*
    * Inserting a new item at the head of the list. This uses a specialized
    * RecyclerView method, notifyItemRemoved(), to trigger any enabled item
    * animations in addition to updating the view.
    */
    public fun removeItem(position: Int) {
        if (position >= mItems.size()) return
        mItems.remove(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): VerticalItemHolder {
        val inflater = LayoutInflater.from(container.getContext())
        val root = inflater.inflate(R.layout.list_item, container, false)
        return VerticalItemHolder(root, this)
    }

    override fun onBindViewHolder(itemHolder: VerticalItemHolder, position: Int) {
        val item = mItems.get(position)

        itemHolder.setName(item as CharSequence)
    }

    override fun getItemCount(): Int {
        return mItems.size()
    }

    public fun setOnItemClickListener(onItemClickListener: AdapterView.OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    private fun onItemHolderClick(itemHolder: VerticalItemHolder) {
        mOnItemClickListener?.onItemClick(null, itemHolder.itemView, itemHolder.getPosition(), itemHolder.getItemId())
    }

    public class VerticalItemHolder(itemView: View, private val mAdapter: VerticalItemAdapter<CharSequence>) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
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
}