package mrz.android.manpages

import android.app.Application
import android.util.Log
import com.firebase.client.Firebase
import mrz.android.manpages.injection.components.ApplicationComponent
import timber.log.Timber
import kotlin.properties.Delegates

public class MainApp : BaseApplication() {

    companion object {
        public var graph : ApplicationComponent by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()

        graph = createApplicationComponent()
        graph.inject(this)

        Firebase.setAndroidContext(this)

        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        else
            Timber.plant(CrashReportingTree())
    }

    class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {
            when (priority) {
                Log.DEBUG, Log.VERBOSE -> return
            }

            // TODO Route to Crashlytics or whatever
        }
    }
}
