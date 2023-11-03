package com.example.eldarwallet.feature_card.utils

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CryptoManager @Inject constructor(
   @ApplicationContext private val context: Context
) {
    private val alias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    fun encrypt(textToEncrypt: String): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val uniqueFileName = "encrypted_data_$timestamp"
        val file = File(context.filesDir, uniqueFileName)
        val encryptedFile = EncryptedFile.Builder(
            file,
            context,
            alias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        encryptedFile.openFileOutput().use { outputStream ->
            outputStream.write(textToEncrypt.toByteArray(Charset.defaultCharset()))
        }

        return file.absolutePath
    }

    fun decrypt(encryptedFilePath: String): String {
        val encryptedFile = EncryptedFile.Builder(
            File(encryptedFilePath),
            context,
            alias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        val stringBuilder = StringBuilder()
        encryptedFile.openFileInput().use { inputStream ->
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                stringBuilder.append(String(buffer, 0, bytesRead, Charset.defaultCharset()))
            }
        }

        return stringBuilder.toString()
    }
}
