package mrz.android.manpages

import android.app.Application
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

    class CrashReportingTree : Timber.HollowTree() {
        override fun i(message: String?, vararg args: Any?) {
            // Crashlytics.log(message)  // TODO
        }

        override fun i(t: Throwable?, message: String?, vararg args: Any?) {
            // Crashlytics.logException(t)
            i(message, args)
        }

        override fun e(message: String?, vararg args: Any?) {
            i("ERROR: ${message}", args)
        }

        override fun e(t: Throwable?, message: String?, vararg args: Any?) {
            // Crashlytics.logException(t)
            e(message, args)
        }
    }
}
