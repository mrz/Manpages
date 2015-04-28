package mrz.android.manpages

import android.app.Application
import android.util.Log
import com.firebase.client.Firebase
import timber.log.Timber

public class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()

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
