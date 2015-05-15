package mrz.android.manpages.injection.modules;

import com.firebase.client.Firebase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * A module for API related dependencies dependencies.
 */
@Module
public class ApiModule {

    @Provides
    @Singleton
    Firebase provideFirebase() {
        return new Firebase("https://manpages.firebaseio.com");
    }
}
