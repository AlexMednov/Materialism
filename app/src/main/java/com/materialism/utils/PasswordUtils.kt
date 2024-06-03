package com.materialism.utils

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

object PasswordUtils {

    private const val SALT_LENGTH = 16

    // Generates a random salt
    fun generateSalt(): ByteArray {
        val salt = ByteArray(SALT_LENGTH)
        SecureRandom().nextBytes(salt)
        return salt
    }

    // Hashes a password with a salt
    fun hashPassword(password: String, salt: ByteArray): String {
        val md = MessageDigest.getInstance("SHA-256")
        md.update(salt)
        val hashedPassword = md.digest(password.toByteArray())
        return Base64.getEncoder().encodeToString(salt + hashedPassword)
    }

    // Verifies if the provided password matches the stored hashed password
    fun verifyPassword(providedPassword: String, storedHash: String): Boolean {
        val decodedHash = Base64.getDecoder().decode(storedHash)
        val salt = decodedHash.copyOfRange(0, SALT_LENGTH)
        val hashedProvidedPassword = hashPassword(providedPassword, salt)
        return hashedProvidedPassword == storedHash
    }
}
