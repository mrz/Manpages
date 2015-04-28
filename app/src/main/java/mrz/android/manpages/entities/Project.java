package mrz.android.manpages.entities;

import io.realm.RealmObject;

public class Project extends RealmObject {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project() {}
}
