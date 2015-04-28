package mrz.android.manpages.view

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import android.widget.ProgressBar
import com.hannesdorfmann.mosby.mvp.MvpFragment
import mrz.android.manpages.R
import mrz.android.manpages.entities.Archive
import mrz.android.manpages.entities.Project
import mrz.android.manpages.presenter.WelcomePresenter
import timber.log.Timber
import kotlin.properties.Delegates

class WelcomeFragment() : MvpFragment<WelcomePresenter>(), WelcomeView {

  /*private val archiveModel: ArchiveModel by Delegates.lazy {
        ArchiveModel(getActivity().getApplicationContext())
    }*/

    private val list: RecyclerView by Delegates.lazy {
        getView().findViewById(R.id.list) as RecyclerView
    }

    private val progressBar: ProgressBar by Delegates.lazy {
        getView().findViewById(R.id.load_progress) as ProgressBar
    }

    var projectAdapter = ListAdapter<Project?>()

    var archiveAdapter = ListAdapter<Archive?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super<MvpFragment>.onCreate(savedInstanceState)

        setRetainInstance(true)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super<MvpFragment>.onViewCreated(view, savedInstanceState)

        setupList()

        //presenter.loadProjects()
    }

    override fun createPresenter(): WelcomePresenter? {
        return WelcomePresenter()
    }

    override fun setData(project: Project) {
        Timber.d("Received ${project.getName()}")

        projectAdapter.addItem(project)
    }

    override fun setData(archive: Archive) {
        Timber.d("Received ${archive.getProject()} ${archive.getVersion()}")

        archiveAdapter.addItem(archive)
    }

    override fun showLoading() {
        progressBar.setVisibility(View.VISIBLE)
    }

    override fun showError(e: Throwable?) {
        // TODO
    }

    override fun showProjects() {
        progressBar.setVisibility(View.GONE)

        list.setAdapter(projectAdapter)

        projectAdapter.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                Timber.d("Clicked on ${projectAdapter.getItem(position)?.getName()}")

                presenter.loadArchives("Linux")
            }
        })
    }

    override fun showArchives() {
        progressBar.setVisibility(View.GONE)
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

/*    private fun generateDownloadURL(distribution: CharSequence, version: CharSequence): Uri {
        val uri = archiveModel.getArchive(distribution, version)
        return Uri.parse(uri?.getUri())
    }*/
}
