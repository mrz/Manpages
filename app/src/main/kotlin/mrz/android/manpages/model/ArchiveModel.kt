package mrz.android.manpages.model

import android.content.Context
import io.realm.Realm
import mrz.android.manpages.entities.Archive

public class ArchiveModel(val context: Context) {
    val realm = Realm.getInstance(context)

    fun getArchives(): List<Archive> {
        return realm.where(javaClass<Archive>()).findAll()
    }

    fun getArchivesByProject(project: String): List<Archive> {
        return realm.where(javaClass<Archive>()).equalTo("project", project).findAll()
    }

    fun getArchive(project: String, version: String): Archive? {
        return realm.where(javaClass<Archive>()).equalTo("project", project).equalTo("version", version).findFirst()
    }
}

