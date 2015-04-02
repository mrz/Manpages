package mrz.android.manpages

import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener
import de.greenrobot.event.EventBus
import mrz.android.manpages.events.StartDownloadEvent
import mrz.android.manpages.model.ArchiveModel
import mrz.android.manpages.ui.DividerDecoration
import mrz.android.manpages.ui.RecyclerViewFragment
import mrz.android.manpages.ui.ProjectAdapter
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        setLayoutManager(LinearLayoutManager(getActivity()))
        setItemDecoration(DividerDecoration(getActivity()))
        setItemAnimator(DefaultItemAnimator())

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
        adapter.addItems(listOf("Linux", "FreeBSD"))

        adapter.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                Timber.d("Clicked on ${adapter.getItem(position)}")

                val what = adapter.getItem(position)
                adapter.clearItems()

                showVersions(what)
            }
        })

        setListAdapter(adapter)
    }

    private fun showVersions(what: CharSequence) {
        Observable.create { subscriber: Subscriber<in DataSnapshot>? ->
            val firebase = Firebase("https://manpages.firebaseio.com/${what}")

            firebase.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(firebaseError: FirebaseError?) {
                    Timber.e("${firebaseError?.getCode()}")
                }

                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    subscriber?.onNext(dataSnapshot)
                }
            })
        }
                .flatMap { dataSnapshot ->
                    Observable.create { subscriber: Subscriber<in CharSequence>? ->
                        for (i in dataSnapshot?.getChildren()?.iterator()) {
                            subscriber?.onNext(i.getValue().toString())
                        }
                        subscriber?.onCompleted()
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CharSequence> {
                    override fun onCompleted() {
/*                        adapter.notifyDataSetChanged()
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
                        Timber.d("$t")
                        adapter.addItem("$t")
                    }
                })
    }
}
