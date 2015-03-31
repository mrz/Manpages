package mrz.android.manpages

import android.app.DownloadManager
import android.app.DownloadManager.Request
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.ActionBarActivity
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import de.greenrobot.event.EventBus
import io.realm.Realm
import io.realm.RealmObject
import mrz.android.manpages.entities.Archive
import mrz.android.manpages.events.FileDownloadedEvent
import mrz.android.manpages.events.StartDownloadEvent
import timber.log.Timber
import java.io.InputStreamReader
import java.lang.reflect.Type
import kotlin.properties.Delegates

open class WelcomeActivity : ActionBarActivity() {
    private var downloadReceiver: DownloadReceiver? = null

    private var downloadReceiverRegistered: Boolean = false

    private val mRealm: Realm? by Delegates.lazy {
        Realm.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_welcome)

        if (!PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(
                "database_populated", false)) {
            //populateDatabase()
        } else {
            // Navigate to main activity
        }

        val fragment: WelcomeFragment = WelcomeFragment()
        getFragmentManager().beginTransaction().add(R.id.container, fragment).commit()

        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)

        if (downloadReceiverRegistered == true)
            unregisterReceiver(downloadReceiver)
    }

    public fun onEvent(event: StartDownloadEvent) {
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = Request(event.uri)
        request.setDestinationInExternalFilesDir(getApplicationContext(), null, event.filename)
        val downloadId = downloadManager.enqueue(request)

        Timber.i("${event.uri} download enqueued - destination ${event.filename}")

        downloadReceiver = DownloadReceiver(getApplicationContext(), downloadId)

        registerReceiver(downloadReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        downloadReceiverRegistered = true
    }

    public fun onEvent(event: FileDownloadedEvent) {
        var intent = Intent(getApplicationContext(), javaClass<MainActivity>())
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun populateDatabase() {
        val stream = getAssets().open("archives.json")
        // Note there is a bug in GSON 2.3.1 that can cause it to StackOverflow when working with RealmObjects.
        // To work around this, use the ExclusionStrategy below or downgrade to 1.7.1
        // See more here: https://code.google.com/p/google-gson/issues/detail?id=440
        val gson: Gson = GsonBuilder().setExclusionStrategies(object : ExclusionStrategy {
            override fun shouldSkipClass(clazz: Class<out Any?>?): Boolean {
                return false
            }

            override fun shouldSkipField(f: FieldAttributes?): Boolean {
                return f?.getDeclaringClass()!!.equals(javaClass<RealmObject>())
            }

        }).create()

        val json: JsonElement = JsonParser().parse(InputStreamReader(stream))
        val collectionType: Type = object : TypeToken<MutableList<Archive>>() {}.getType()!!
        val archives: MutableList<Archive> = gson.fromJson(json, collectionType)

        mRealm?.beginTransaction()
        mRealm?.copyToRealm(archives)
        mRealm?.commitTransaction()

        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(
                "database_populated", true).commit()
    }
}
