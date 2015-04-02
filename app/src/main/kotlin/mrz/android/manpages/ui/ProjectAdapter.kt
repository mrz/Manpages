package mrz.android.manpages.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import mrz.android.manpages.R
import java.util.ArrayList

public open class ProjectAdapter<T>() : RecyclerView.Adapter<ItemHolder>() {
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

    public fun addItem(what: T) {
        mItems.add(what)
        notifyItemInserted(mItems.size() - 1)
    }

    public fun clearItems() {
        val size = mItems.size()
        mItems.clear()
        notifyItemRangeRemoved(0, size)
    }

    public fun addItems(items: List<T>, clear: Boolean = false) {
        var positionStart = mItems.size()

        if (clear) {
            clearItems()
            positionStart = 0
        }

        if (mItems.addAll(items))
            notifyItemRangeInserted(positionStart, mItems.size() - 1)
    }

    public fun removeItem(position: Int) {
        if (position >= mItems.size()) return
        mItems.remove(position)
        notifyItemRemoved(position)
    }

    public fun getItem(position: Int): T {
        return mItems.get(position)
    }

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ItemHolder {
        val inflater = LayoutInflater.from(container.getContext())
        val root = inflater.inflate(R.layout.list_item, container, false)
        return ItemHolder(root, this)
    }

    override fun onBindViewHolder(itemHolder: ItemHolder, position: Int) {
        val item = mItems.get(position)

        itemHolder.setName(item as CharSequence)
    }

    override fun getItemCount(): Int {
        return mItems.size()
    }

    public fun setOnItemClickListener(onItemClickListener: AdapterView.OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    public fun onItemHolderClick(itemHolder: ItemHolder) {
        mOnItemClickListener?.onItemClick(null, itemHolder.itemView, itemHolder.getPosition(), itemHolder.getItemId())
    }
}

public class ItemHolder(itemView: View, private val mAdapter: ProjectAdapter<*>) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
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