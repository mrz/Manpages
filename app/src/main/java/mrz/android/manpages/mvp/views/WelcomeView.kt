package mrz.android.manpages.view

import com.hannesdorfmann.mosby.mvp.MvpView
import mrz.android.manpages.model.entities.Archive
import mrz.android.manpages.model.entities.Project

trait WelcomeView : MvpView {

    fun setData(project: Project)

    fun setData(archive: Archive)

    fun showLoading(loading: Boolean)

    fun showError(e: Throwable?)

    fun showProjects()

    fun showArchives()
}
