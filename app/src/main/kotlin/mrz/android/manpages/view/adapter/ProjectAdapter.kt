package mrz.android.manpages.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import mrz.android.manpages.R
import mrz.android.manpages.entities.Project

public class ProjectAdapter : ListAdapter<Project>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemHolder<Project>? {
        val inflater = LayoutInflater.from(parent?.getContext())
        val root = inflater.inflate(R.layout.list_item, parent, false)
        return ProjectHolder(root, this)
    }
}

public class ProjectHolder(itemView: View, mAdapter: ListAdapter<*>) : ItemHolder<Project>(itemView, mAdapter) {
    private val mName: TextView

    init {
        mName = itemView.findViewById(R.id.name) as TextView
    }

    override fun bind(item: Project) {
        mName.setText(item.getName())
    }
}


