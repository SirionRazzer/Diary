package com.sirionrazzer.diary.util

class StringUtils {
    companion object Factory {
        fun isValidEmail(email: String): Boolean {
            return if (email.isBlank()) false else android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}