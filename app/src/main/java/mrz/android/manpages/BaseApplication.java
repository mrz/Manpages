package mrz.android.manpages;

import android.app.Application;

import mrz.android.manpages.injection.components.ApplicationComponent;
import mrz.android.manpages.injection.components.DaggerApplicationComponent;
import mrz.android.manpages.injection.modules.AndroidModule;
import mrz.android.manpages.injection.modules.ApiModule;

public abstract class BaseApplication extends Application {

    protected ApplicationComponent createApplicationComponent() {
        return DaggerApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .apiModule(new ApiModule())
                .build();
    }
}
