package mrz.android.manpages

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import de.greenrobot.event.EventBus
import mrz.android.manpages.entities.Archive
import mrz.android.manpages.events.StartDownloadEvent
import mrz.android.manpages.model.ArchiveModel
import kotlin.properties.Delegates

open class WelcomeFragment : Fragment() {

    private val archiveModel: ArchiveModel by Delegates.lazy {
        ArchiveModel(getActivity().getApplicationContext())
    }

    private val projectSpinner: Spinner? by Delegates.lazy {
        getView().findViewById(R.id.project_spinner) as Spinner?
    }

    private val versionSpinner: Spinner? by Delegates.lazy {
        getView().findViewById(R.id.version_spinner) as Spinner?
    }

    private val confirmButton: Button? by Delegates.lazy {
        getView().findViewById(R.id.confirm_button) as Button?
    }

    private val progressBar: ProgressBar? by Delegates.lazy {
        getView().findViewById(R.id.progressBar) as ProgressBar?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_welcome, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val projectAdapter: SpinnerAdapter = SpinnerAdapter(getActivity())
        projectAdapter.setItems(
                listOf(*getActivity().getResources().getTextArray(R.array.distributions_array)))

        val versionAdapter: SpinnerAdapter = SpinnerAdapter(getActivity())

        projectSpinner?.setAdapter(projectAdapter)
        versionSpinner?.setAdapter(versionAdapter)

        // Avoid triggering an onItemSelected when setting the adapter
        listOf(projectSpinner, versionSpinner) map { s ->
            s?.setSelection(0, false)
        }

        projectSpinner?.setOnItemSelectedListener(
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<out Adapter>?, view: View?,
                            position: Int, id: Long) {
                        populateAdapter(versionAdapter,
                                archiveModel.getArchivesByProject(
                                        parent?.getItemAtPosition(position) as String))
                    }

                    override fun onNothingSelected(parent: AdapterView<out Adapter>?) {
                    }
                })

        versionSpinner?.setOnItemSelectedListener(
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<out Adapter>?, view: View?,
                            position: Int, id: Long) {
                        confirmButton?.setVisibility(View.VISIBLE)
                    }

                    override fun onNothingSelected(parent: AdapterView<out Adapter>?) {
                    }
                })

        confirmButton?.setOnClickListener {
            val distribution = projectSpinner?.getSelectedItem() as String
            val version = versionSpinner?.getSelectedItem() as String

            val downloadURL: Uri = generateDownloadURL(distribution, version)

            progressBar?.setVisibility(View.VISIBLE)

            EventBus.getDefault().post(StartDownloadEvent(downloadURL,
                    archiveModel.getArchive(distribution, version)!!.getFilename()))
        }
    }

    private fun populateAdapter(adapter: SpinnerAdapter, items: List<Archive>?) {
        adapter.clear()
        adapter.setItems(items?.map { it -> it.getVersion() })
        adapter.notifyDataSetChanged()
    }

    private fun generateDownloadURL(distribution: String, version: String): Uri {
        val uri = archiveModel.getArchive(distribution, version)

        return Uri.parse(uri?.getUri())
    }
}
