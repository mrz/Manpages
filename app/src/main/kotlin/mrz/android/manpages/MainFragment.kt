package mrz.android.manpages

import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.app.Activity
import android.widget.ProgressBar
import kotlin.properties.Delegates

public class MainFragment : Fragment() {

    private val progressBar: ProgressBar? by Delegates.lazy {
        getView().findViewById(R.id.progressBar) as ProgressBar?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar?.setVisibility(View.VISIBLE)
    }
}
