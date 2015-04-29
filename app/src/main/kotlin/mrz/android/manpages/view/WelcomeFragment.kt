package mrz.android.manpages.view

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.ViewFlipper
import com.hannesdorfmann.mosby.mvp.MvpFragment
import de.greenrobot.event.EventBus
import mrz.android.manpages.R
import mrz.android.manpages.entities.Archive
import mrz.android.manpages.entities.Project
import mrz.android.manpages.events.ProjectSelectedEvent
import mrz.android.manpages.presenter.WelcomePresenter
import timber.log.Timber
import kotlin.properties.Delegates

class WelcomeFragment() : MvpFragment<WelcomePresenter>(), WelcomeView {

    // ViewFlipper contants
    private val LOADING = 0
    private val LIST = 1

  /*private val archiveModel: ArchiveModel by Delegates.lazy {
        ArchiveModel(getActivity().getApplicationContext())
    }*/

    private val list: RecyclerView by Delegates.lazy {
        getView().findViewById(R.id.list) as RecyclerView
    }

    private val viewFlipper : ViewFlipper by Delegates.lazy {
        getView().findViewById(R.id.viewFlipper) as ViewFlipper
    }

    var projectAdapter = ListAdapter<Project?>()

    var archiveAdapter = ListAdapter<Archive?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super<MvpFragment>.onCreate(savedInstanceState)

        setRetainInstance(true)

        EventBus.getDefault().register(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super<MvpFragment>.onViewCreated(view, savedInstanceState)

        setupList()

        projectAdapter.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                EventBus.getDefault().post(ProjectSelectedEvent(projectAdapter.getItem(position)?.getName()!!))
            }
        })

        presenter.loadProjects()
    }

    override fun createPresenter(): WelcomePresenter? {
        return WelcomePresenter()
    }

    override fun setData(project: Project) {
        projectAdapter.addItem(project)
    }

    override fun setData(archive: Archive) {
        archiveAdapter.addItem(archive)
    }

    override fun showLoading(loading: Boolean) {
        viewFlipper.setDisplayedChild(if(loading) LOADING else LIST)
    }

    override fun showError(e: Throwable?) {
        // TODO
    }

    override fun showProjects() {
        showLoading(false)

        list.setAdapter(projectAdapter)
    }

    override fun showArchives() {
        showLoading(false)

        list.setAdapter(archiveAdapter)
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_welcome
    }

    private fun setupList() {
        list.setLayoutManager(LinearLayoutManager(getActivity()))
        list.addItemDecoration(DividerDecoration(getActivity()))
        list.setItemAnimator(DefaultItemAnimator())

        list.getItemAnimator()?.setAddDuration(1000);
        list.getItemAnimator()?.setChangeDuration(1000);
        list.getItemAnimator()?.setMoveDuration(1000);
        list.getItemAnimator()?.setRemoveDuration(1000);
    }

    fun onEvent(event: ProjectSelectedEvent) {
        presenter.loadArchives(event.project)
    }

/*    private fun generateDownloadURL(distribution: CharSequence, version: CharSequence): Uri {
        val uri = archiveModel.getArchive(distribution, version)
        return Uri.parse(uri?.getUri())
    }*/
}
