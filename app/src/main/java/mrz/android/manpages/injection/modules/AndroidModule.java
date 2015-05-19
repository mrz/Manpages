package mrz.android.manpages.injection.modules;

import android.content.Context;
import android.net.Uri;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mrz.android.manpages.BaseApplication;
import mrz.android.manpages.ForApplication;
import timber.log.Timber;

/**
 * A module for Android-specific dependencies which require a {@link android.content.Context} or
 * {@link android.app.Application} to create.
 */
@Module
public class AndroidModule {

    private final BaseApplication mApplication;

    public AndroidModule(BaseApplication application) {
        mApplication = application;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    @ForApplication
    Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    Picasso providePicasso() {
        return new Picasso.Builder(mApplication)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Timber.e(exception, "Failed to load image: %s", uri);
                    }
                })
                .build();
    }
}

