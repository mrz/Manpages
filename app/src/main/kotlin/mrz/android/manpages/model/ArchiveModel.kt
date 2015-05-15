package mrz.android.manpages.model

import android.content.Context
import io.realm.Realm
import mrz.android.manpages.model.entities.Archive

public class ArchiveModel(val context: Context) {
    val realm = Realm.getInstance(context)

    fun getArchives(): List<Archive> {
        return realm.where(javaClass<Archive>()).findAll()
    }

    fun getArchivesByProject(project: String): List<Archive> {
        return realm.where(javaClass<Archive>()).equalTo("project", project).findAll()
    }

    fun getArchive(project: CharSequence, version: CharSequence): Archive? {
        return realm.where(javaClass<Archive>()).equalTo("project", project.toString()).equalTo("version", version.toString()).findFirst()
    }
}

