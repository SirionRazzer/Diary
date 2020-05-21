package main.java.com.sirionrazzer.diary.system

import io.realm.DynamicRealm
import io.realm.RealmMigration

class MyRealmMigration : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema
        var tOldVersion = oldVersion

        if (tOldVersion == 0L) {
            schema.get("TrackItemTemplate")
                ?.renameField("deleted", "archived")
                ?.addField("selected", Boolean::class.java)
                ?.addField("description", String::class.java)
                ?.setRequired("description", true)
                ?.addField("hasPictureField", Boolean::class.java)

            schema.get("TrackItem")
                ?.renameField("deleted", "archived")
                ?.addField("description", String::class.java)
                ?.setRequired("description", true)
                ?.addField("hasPictureField", Boolean::class.java)
                ?.addField("pictureField", String::class.java)
                ?.setRequired("pictureField", false)
        }
    }
}