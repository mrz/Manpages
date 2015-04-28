package mrz.android.manpages.presenter

import com.hannesdorfmann.mosby.mvp.MvpPresenter
import mrz.android.manpages.entities.Archive
import mrz.android.manpages.entities.Project
import mrz.android.manpages.rx.NetworkAPI
import mrz.android.manpages.view.WelcomeView
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.lang.ref.WeakReference

class WelcomePresenter() : MvpPresenter<WelcomeView> {

    var viewRef: WeakReference<WelcomeView>? = null

    override fun detachView(view: Boolean) {
        viewRef ?: viewRef?.clear()
        viewRef = null
    }

    override fun attachView(view: WelcomeView?) {
        viewRef = WeakReference<WelcomeView>(view)
    }

    fun isViewAttached(): Boolean {
        return viewRef ?: viewRef?.get() != null
    }

    fun getView(): WelcomeView? {
        return viewRef?.get()
    }

    fun loadProjects() {
        if (isViewAttached())
            getView()?.showLoading()

        NetworkAPI().getProjects()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Project> {
                    override fun onNext(t: Project) {
                        if(isViewAttached())
                            getView()?.setData(t)
                    }

                    override fun onCompleted() {
                        if (isViewAttached())
                            getView()?.showProjects()
                    }

                    override fun onError(e: Throwable?) {
                        Timber.e(e, "${e.toString()}")

                        if (isViewAttached())
                            getView()?.showError(e)
                    }
                })
    }

    fun loadArchives(project: String) {
        if (isViewAttached())
            getView()?.showLoading()

        NetworkAPI().getVersions(project)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Archive> {
                    override fun onCompleted() {
                        if (isViewAttached())
                            getView()?.showArchives()
                    }

                    override fun onError(e: Throwable?) {
                        Timber.e(e, "${e.toString()}")

                        if (isViewAttached())
                            getView()?.showError(e)
                    }

                    override fun onNext(t: Archive) {
                        if(isViewAttached())
                            getView()?.setData(t)
                    }
                })
    }
}

