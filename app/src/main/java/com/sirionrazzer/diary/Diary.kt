package com.sirionrazzer.diary

import android.app.Application
import android.content.SharedPreferences
import com.jakewharton.threetenabp.AndroidThreeTen
import com.sirionrazzer.diary.system.dagger.ApiModule
import com.sirionrazzer.diary.system.dagger.AppComponent
import com.sirionrazzer.diary.system.dagger.DaggerAppComponent
import com.sirionrazzer.diary.system.dagger.StorageModule
import com.sirionrazzer.diary.util.Result
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.exceptions.RealmFileException
import io.realm.exceptions.RealmMigrationNeededException
import main.java.com.sirionrazzer.diary.system.MyRealmMigration
import java.io.File
import java.lang.IllegalStateException
import java.lang.NullPointerException
import javax.inject.Inject
import javax.inject.Named

class Diary : Application() {

    @Inject
    @field:Named("user_prefs")
    lateinit var prefs: SharedPreferences

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
        Realm.init(this)
        Realm.removeDefaultConfiguration()
        app = this
        app.appComponent.inject(this)
    }

    fun installEncryptedRealm(key: ByteArray) {
        // HACK: store fb filename in shared prefs.. realm somehow can't remember it
        val realmFileName = prefs.getString("realm_file", null) ?: "diary.realm"
        Realm.setDefaultConfiguration(buildRealmConfiguration(key, realmFileName))
    }

    /**
     * Keys have to be 64-byte long
     *
     * @param oldKey
     * @param newKey
     */
    fun reencryptRealm(newKey: ByteArray): Result<String> {
        val newName = System.currentTimeMillis().toString() + ".realm"
        val newFile = File(applicationContext.filesDir, newName)
        prefs.edit().putString("realm_file", newName).apply()
        if (Realm.getDefaultConfiguration() != null) {
            try {
                val realm = Realm.getInstance(Realm.getDefaultConfiguration()!!)
                realm.writeEncryptedCopyTo(newFile, newKey)
                realm.close()
                Realm.deleteRealm(Realm.getDefaultConfiguration()!!)
                Realm.removeDefaultConfiguration()
                Realm.setDefaultConfiguration(buildRealmConfiguration(newKey, newName))
            } catch (e: RealmMigrationNeededException) {
                return Result.Error(e)
            } catch (e: RealmFileException) {
                return Result.Error(e)
            } catch (e: IllegalArgumentException) {
                return Result.Error(e)
            } catch (e: IllegalStateException) {
                return Result.Error(e)
            }
            return Result.Success("Encryption OK")
        } else {
            return Result.Error(NullPointerException())
        }
    }

    private fun buildRealmConfiguration(key: ByteArray, name: String): RealmConfiguration {
        return RealmConfiguration.Builder()
            .name(name)
            .encryptionKey(key)
            .schemaVersion(REALM_MIGRATION_VERSION)
            .migration(MyRealmMigration())
            .build()
    }

    companion object {
        lateinit var app: Diary
        private const val REALM_MIGRATION_VERSION = 1L
    }
}
