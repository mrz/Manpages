package mrz.android.manpages.view

import com.hannesdorfmann.mosby.mvp.MvpView
import mrz.android.manpages.entities.Archive
import mrz.android.manpages.entities.Project

trait WelcomeView : MvpView {

    fun setData(project: Project)

    fun setData(archive: Archive)

    fun showLoading()

    fun showError(e: Throwable?)

    fun showProjects()

    fun showArchives()
}
