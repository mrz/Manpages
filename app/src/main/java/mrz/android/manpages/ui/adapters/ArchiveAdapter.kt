package mrz.android.manpages.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import mrz.android.manpages.R
import mrz.android.manpages.model.entities.Archive

public class ArchiveAdapter() : ListAdapter<Archive>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemHolder<Archive>? {
        val inflater = LayoutInflater.from(parent?.getContext())
        val root = inflater.inflate(R.layout.archive_list_item, parent, false)
        return ArchiveHolder(root, this)
    }
}

public class ArchiveHolder(itemView: View, mAdapter: ListAdapter<*>) : ItemHolder<Archive>(itemView, mAdapter) {
    private val mVersion: TextView

    // private var mLogo: ImageView

    init {
        mVersion = itemView.findViewById(R.id.name) as TextView // TODO name -> version
        // mLogo = itemView.findViewById(R.id.logo) as ImageView
    }

    override fun bind(item: Archive) {
        mVersion.setText(item.getVersion())
    }
}

