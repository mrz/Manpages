package mrz.android.manpages.rx

import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener
import rx.Observable
import rx.Subscriber
import timber.log.Timber

class NetworkAPI {

    public fun getProjects(): Observable<CharSequence> {
        return get("Projects")
    }

    public fun getVersions(what: CharSequence): Observable<CharSequence> {
        return get(what)
    }

    public fun get(what: CharSequence): Observable<CharSequence> {
        return Observable.create { subscriber: Subscriber<in DataSnapshot>? ->
            val firebase = Firebase("https://manpages.firebaseio.com/${what}")

            firebase.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(firebaseError: FirebaseError?) {
                    Timber.e("${firebaseError?.getCode()}")
                }

                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    subscriber?.onNext(dataSnapshot)
                }
            })
        }
                .flatMap { dataSnapshot ->
                    Observable.create { subscriber: Subscriber<in CharSequence>? ->
                        for (i in dataSnapshot?.getChildren()?.iterator()) {
                            subscriber?.onNext(i.getValue().toString())
                        }
                        subscriber?.onCompleted()
                    }
                }
    }
}

