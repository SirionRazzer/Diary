package com.sirionrazzer.diary.boarding

import android.util.Base64
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.sirionrazzer.diary.Diary
import com.sirionrazzer.diary.models.UserStorage
import java.security.MessageDigest
import java.security.SecureRandom
import javax.inject.Inject


interface AuthInterface {
    fun register(email: String, pw: String)
    fun anonymousRegister()
    fun linkAnonymousToPassword(email: String, pw: String)
    fun login(email: String, pw: String)
    fun logout()
    fun changePassword(oldPw: String, newPw: String, newPwAgain: String)
}

class AuthViewModel : ViewModel(), AuthInterface {
    @Inject
    lateinit var userStorage: UserStorage

    val isLoggedIn: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isNewcomer: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val isAnonymous: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    val authError: MutableLiveData<String?> by lazy { MutableLiveData<String?>(null) }
    val encrError: MutableLiveData<String?> by lazy { MutableLiveData<String?>(null) }
    val accountCreated: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        Diary.app.appComponent.inject(this)
        val user = firebaseAuth.currentUser
        if (user != null) {
            isAnonymous.value = user.isAnonymous
            isLoggedIn.value = true
        }
        accountCreated.value = userStorage.userSettings.accountCreated
        isNewcomer.value = userStorage.userSettings.isNewcomer
    }

    override fun register(email: String, pw: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, pw)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    try {
                        storeHashedPassword(pw)
                        updateEmail(email)
                        userStorage.updateSettings { lus ->
                            lus.isNewcomer = true
                            lus.accountCreated = true
                        }
                        accountCreated.value = true
                        isNewcomer.value = true
                        isAnonymous.value = false
                        isLoggedIn.value = true
                    } catch (e: Error) {
                        failedEncryption(e.message)
                    }
                } else {
                    failLogin(task.exception?.message)
                }
            }
            .addOnFailureListener { failure ->
                failLogin(failure.message)
            }
    }

    override fun login(email: String, pw: String) {
        firebaseAuth.signInWithEmailAndPassword(email, pw)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    try {
                        storeHashedPassword(pw)
                        updateEmail(email)
                        userStorage.updateSettings { lus ->
                            lus.isNewcomer = false
                            lus.accountCreated = true
                        }
                        accountCreated.value = true
                        isAnonymous.value = false
                        isLoggedIn.value = true
                    } catch (e: Exception) {
                        failedEncryption(e.message)
                    }
                } else {
                    failLogin(task.exception?.message)
                }
            }
            .addOnFailureListener { failure ->
                failLogin(failure.message)
            }
    }

    override fun anonymousRegister() {
        firebaseAuth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    try {
                        storeHashedPassword(null)
                        userStorage.updateSettings { lus ->
                            lus.isNewcomer = true
                            lus.accountCreated = true
                        }
                        accountCreated.value = true
                        isNewcomer.value = true
                        isAnonymous.value = true
                        isLoggedIn.value = true
                    } catch (e: Exception) {
                        failedEncryption(e.message)
                    }
                } else {
                    failLogin(task.exception?.message)
                }
            }
    }

    override fun linkAnonymousToPassword(email: String, pw: String) {
        val credential = EmailAuthProvider.getCredential(email, pw)
        firebaseAuth.currentUser?.linkWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    try {
                        storeHashedPassword(pw)
                        updateEmail(email)
                        userStorage.updateSettings { lus ->
                            lus.accountCreated = true
                        }
                        isAnonymous.value = false
                        isLoggedIn.value = true
                    } catch (e: Exception) {
                        failedEncryption(e.message)
                    }
                } else {
                    failLogin(task.exception?.message)
                }
            }
            ?.addOnFailureListener { failure ->
                failLogin(failure.message)
            }
    }

    private fun failedEncryption(encrError: String?) {
        isLoggedIn.value = false
        encrError?.let { this.encrError.value = "Encryption error ${it}" }
    }

    private fun failLogin(authError: String?) {
        isLoggedIn.value = false
        authError?.let { this.authError.value = "Authentication error ${it}" }
    }

    override fun logout() {
        userStorage.clearPasswordDigest()
        isLoggedIn.value = false
    }

    override fun changePassword(oldPw: String, newPw: String, newPwAgain: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        // TODO reencrypt Realm
    }

    /**
     * Set password and store its hashed version
     * Create new temporary password for anonymouse user
     * @param pw is password, if null then generate temporary password
     */
    @Throws(java.lang.Exception::class)
    private fun storeHashedPassword(pw: String?) {
        var key = ByteArray(64)
        if (pw == null) {
            SecureRandom().nextBytes(key)
            MessageDigest.getInstance("SHA-256").digest(key, 0, key.size)
            userStorage.storePasswordDigest(key)
        } else {
            Base64.decode(pw, Base64.NO_WRAP).let {
                (it.indices).forEach { i ->
                    // TODO this doesn't work good
                    key[i] = it[i]
                }
            }
            MessageDigest.getInstance("SHA-256").digest(key, 0, key.size)
            userStorage.storePasswordDigest(key)
        }
    }

    private fun updateEmail(email: String) {
        userStorage.updateSettings { lus ->
            lus.email = email
        }
    }

    fun getEncryptedPassword(): ByteArray {
        return userStorage.loadPasswordDigest()
    }
}