package com.sirionrazzer.diary

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.sirionrazzer.diary.system.dagger.ApiModule
import com.sirionrazzer.diary.system.dagger.AppComponent
import com.sirionrazzer.diary.system.dagger.DaggerAppComponent
import com.sirionrazzer.diary.system.dagger.StorageModule
import io.realm.Realm
import io.realm.RealmConfiguration
import main.java.com.sirionrazzer.diary.system.MyRealmMigration
import java.io.File

class Diary : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .apiModule(ApiModule())
            .storageModule(StorageModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        app = this
    }

    fun installEncryptedRealm(key: ByteArray) {
        // TODO remove any realm if already installed and initiated
        Realm.init(this)
        Realm.setDefaultConfiguration(buildRealmConfiguration(key, null))
    }

    /**
     * Keys have to be 64-byte long
     *
     * @param oldKey
     * @param newKey
     */
    fun reencryptRealm(newKey: ByteArray) {
        val newName = System.currentTimeMillis().toString() + ".db"
        val realm = Realm.getDefaultInstance()
        realm.writeEncryptedCopyTo(
            File(
                applicationContext.filesDir, newName
            ), newKey
        )
        val config = Realm.getDefaultConfiguration()
        realm.close()
        Realm.deleteRealm(config!!)
        Realm.setDefaultConfiguration(buildRealmConfiguration(newKey, newName))
        // TODO sync db with Firebase after reencryption
    }

    private fun buildRealmConfiguration(key: ByteArray, name: String?): RealmConfiguration {
        if (name != null) {
            return RealmConfiguration.Builder()
                .name(name)
                .encryptionKey(key)
                .schemaVersion(REALM_MIGRATION_VERSION)
                .migration(MyRealmMigration())
                .build()
        } else {
            return RealmConfiguration.Builder()
                .encryptionKey(key)
                .schemaVersion(REALM_MIGRATION_VERSION)
                .migration(MyRealmMigration())
                .build()
        }
    }

    companion object {
        lateinit var app: Diary
        private const val REALM_MIGRATION_VERSION = 0L
    }
}
