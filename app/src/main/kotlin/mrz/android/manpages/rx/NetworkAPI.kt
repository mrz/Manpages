package mrz.android.manpages.rx

import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener
import mrz.android.manpages.entities.Archive
import mrz.android.manpages.entities.Project
import rx.Observable
import rx.Subscriber
import timber.log.Timber

class NetworkAPI {

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
                    val firebase = Firebase("https://manpages.firebaseio.com/${what}")

                    firebase.addValueEventListener(object : ValueEventListener {
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

