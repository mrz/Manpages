package mrz.android.manpages

import android.support.v7.app.ActionBarActivity
import android.os.Bundle
import mrz.android.manpages.events.StartDownloadEvent
import android.content.Context
import android.app.DownloadManager
import android.app.DownloadManager.Request
import android.content.IntentFilter
import kotlin.properties.Delegates
import io.realm.Realm
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import io.realm.RealmObject
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.InputStreamReader
import mrz.android.manpages.model.Archive
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.io.FileInputStream

open class WelcomeActivity : ActionBarActivity() {

    private var mDownloadReceiver: DownloadReceiver? = null

    private val mRealm: Realm? by Delegates.lazy {
        Realm.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_welcome)

        if (!PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(
                "database_populated", false)) {
            populateDatabase()
        } else {
            // Navigate to main activity
        }

        val fragment: WelcomeFragment = WelcomeFragment()
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        //unregisterReceiver(mDownloadReceiver)
    }

    public fun onEvent(event: StartDownloadEvent) {
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = Request(event.uri)
        request.setDestinationInExternalFilesDir(getApplicationContext(), null,
                event.uri.getLastPathSegment())
        val downloadId = downloadManager.enqueue(request)

        mDownloadReceiver = DownloadReceiver(getApplicationContext(), downloadId)

        registerReceiver(mDownloadReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
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
                return f?.getDeclaringClass().equals(javaClass<RealmObject>())
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
