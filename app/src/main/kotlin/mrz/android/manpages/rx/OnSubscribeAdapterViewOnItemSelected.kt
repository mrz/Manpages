package mrz.android.manpages.rx

import android.view.View
import android.widget.AdapterView
import rx.Observable
import rx.Subscriber
import rx.android.internal.Assertions

class OnSubscribeAdapterViewOnItemSelected(val adapterView: AdapterView<*>) : Observable.OnSubscribe<OnItemSelectedEvent> {

    override fun call(observer: Subscriber<in OnItemSelectedEvent>?) {
        Assertions.assertUiThread();

        val listener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                observer?.onNext(OnItemSelectedEvent(parent, view, position, id))
            }
        };

        adapterView.setOnItemSelectedListener(listener)

    }
}

