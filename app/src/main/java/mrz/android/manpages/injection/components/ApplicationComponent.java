package mrz.android.manpages.injection.components;

import javax.inject.Singleton;

import dagger.Component;
import mrz.android.manpages.MainApp;
import mrz.android.manpages.injection.modules.AndroidModule;
import mrz.android.manpages.injection.modules.ApiModule;
import mrz.android.manpages.model.rx.NetworkAPI;

@Singleton
@Component(modules = {
        AndroidModule.class,
        ApiModule.class
})

public interface ApplicationComponent {
    void inject(MainApp application);
    void inject(NetworkAPI api);
}
