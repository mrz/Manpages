package mrz.android.manpages.view.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import mrz.android.manpages.MainApp
import mrz.android.manpages.R
import mrz.android.manpages.model.entities.Project
import javax.inject.Inject
import kotlin.properties.Delegates

public class ProjectAdapter : ListAdapter<Project>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder<Project>? {
        val inflater = LayoutInflater.from(parent.getContext())
        val root = inflater.inflate(R.layout.project_list_item, parent, false)
        return ProjectHolder(root, this)
    }
}

public class ProjectHolder(itemView: View, mAdapter: ProjectAdapter) : ItemHolder<Project>(itemView, mAdapter) {
    private val mProject: TextView
    private val mLogo: ImageView

    var mPicasso: Picasso by Delegates.notNull()
        [Inject] set

    init {
        MainApp.graph.inject(this)
        mProject = itemView.findViewById(R.id.project) as TextView
        mLogo = itemView.findViewById(R.id.logo) as ImageView
    }

    override fun bind(item: Project) {
        mProject.setText(item.getName())
        mPicasso.load(item.getLogo())
                .resizeDimen(R.dimen.list_icon_width, R.dimen.list_icon_height)
                .into(mLogo)
    }
}