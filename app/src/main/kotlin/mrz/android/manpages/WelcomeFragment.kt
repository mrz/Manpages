package mrz.android.manpages

import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ProgressBar
import mrz.android.manpages.model.ArchiveModel
import mrz.android.manpages.rx.NetworkAPI
import mrz.android.manpages.ui.DividerDecoration
import mrz.android.manpages.ui.ProjectAdapter
import mrz.android.manpages.ui.RecyclerViewFragment
import rx.Observable
import rx.Observer
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
import kotlin.properties.Delegates

open class WelcomeFragment : RecyclerViewFragment(R.layout.fragment_welcome, R.id.list) {

    var adapter = ProjectAdapter<CharSequence>()

    private val archiveModel: ArchiveModel by Delegates.lazy {
        ArchiveModel(getActivity().getApplicationContext())
    }

    private val progressBar: ProgressBar by Delegates.lazy {
        getView().findViewById(R.id.load_progress) as ProgressBar
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        setLayoutManager(LinearLayoutManager(getActivity()))
        setItemDecoration(DividerDecoration(getActivity()))
        setItemAnimator(DefaultItemAnimator())

        adapter.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                Timber.d("Clicked on ${adapter.getItem(position)}")

                val what = adapter.getItem(position)
                adapter.clearItems()

                showVersions(what)
            }
        })

        setListAdapter(adapter)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProjects()
    }

    private fun generateDownloadURL(distribution: CharSequence, version: CharSequence): Uri {
        val uri = archiveModel.getArchive(distribution, version)
        return Uri.parse(uri?.getUri())
    }

    private fun showProjects() {
        NetworkAPI().getProjects()
                .flatMap { value ->
                    Observable.create { subscriber: Subscriber<in CharSequence>? ->
                        // subscriber?.onNext(/* {name=Linux} -> Linux */)
                        subscriber?.onCompleted()
                    }
                }
                .subscribe(object : Observer<CharSequence> {
                    override fun onNext(t: CharSequence?) {
                        Timber.d("$t")
                        adapter.addItem("$t")
                    }

                    override fun onCompleted() {
                        //
                    }

                    override fun onError(e: Throwable?) {
                        Timber.e(e.toString(), e)
                    }

                })
    }

    private fun showVersions(what: CharSequence) {
        progressBar.setVisibility(View.VISIBLE)

        val o = NetworkAPI().getVersions(what)
                .observeOn(AndroidSchedulers.mainThread())

        o.take(1).subscribe(object : Observer<CharSequence> {
            override fun onCompleted() {
                //
            }

            override fun onNext(t: CharSequence?) {
                progressBar.setVisibility(View.GONE)
            }

            override fun onError(e: Throwable?) {
                Timber.e(e, e.toString())
            }

        })

        o.subscribe(object : Observer<CharSequence> {
            override fun onCompleted() {
                /*
                adapter.setOnItemClickListener(object : AdapterView.OnItemClickListener {
                    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        val downloadURL = generateDownloadURL(what, adapter.getItem(position))
                        EventBus.getDefault().post(StartDownloadEvent(downloadURL,
                                archiveModel.getArchive(what, adapter.getItem(position))!!.getFilename()))
                    }
                })*/
            }

            override fun onError(e: Throwable?) {
                Timber.e(e.toString(), e)
            }

            override fun onNext(t: CharSequence?) {
                adapter.addItem("$t")
            }
        })
    }
}
