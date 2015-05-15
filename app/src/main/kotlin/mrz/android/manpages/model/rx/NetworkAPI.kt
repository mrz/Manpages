package mrz.android.manpages.model.rx

import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener
import mrz.android.manpages.MainApp
import mrz.android.manpages.model.entities.Archive
import mrz.android.manpages.model.entities.Project
import rx.Observable
import rx.Subscriber
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

class NetworkAPI {

    var firebase: Firebase by Delegates.notNull()
        [Inject] set

    init {
        MainApp.graph.inject(this)
    }

    public fun getProjects(): Observable<Project> {
        return get("Projects")
                .map { value -> value.getValue(javaClass<Project>()) }
    }

    public fun getVersions(what: CharSequence): Observable<Archive> {
        return get(what)
                .map { value -> value.getValue(javaClass<Archive>()) }
    }

    public fun get(what: CharSequence): Observable<DataSnapshot> {
        return Observable
                .create { subscriber: Subscriber<in DataSnapshot>? ->
                    val dataRef = firebase.child(what.toString())

                    dataRef.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(firebaseError: FirebaseError?) {
                            Timber.e("${firebaseError?.getCode()}")
                            subscriber?.onError(null)
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot?) {
                            subscriber?.onNext(dataSnapshot)
                            subscriber?.onCompleted()
                        }
                    })
                }
                .flatMap { dataSnapshot ->
                    Observable.from(dataSnapshot.getChildren())
                }
    }
}