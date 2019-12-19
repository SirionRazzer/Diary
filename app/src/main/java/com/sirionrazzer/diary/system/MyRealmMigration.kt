package main.java.com.sirionrazzer.diary.system

import io.realm.DynamicRealm
import io.realm.RealmMigration

class MyRealmMigration : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {}

    // HashCode and Equals are set due to the renaming, maybe can be removed now?
    override fun hashCode(): Int {
        return 37
    }

    override fun equals(other: Any?): Boolean {
        return other is MyRealmMigration
    }
}