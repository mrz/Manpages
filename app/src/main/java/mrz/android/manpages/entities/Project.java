package mrz.android.manpages.entities;

import io.realm.RealmObject;

public class Project extends RealmObject {

    private String name;

    private String logo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Project() {}
}
