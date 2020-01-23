package com.jnr.buscamarvel.utils

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.math.BigInteger
import java.security.MessageDigest

class Utils {
    companion object {
        var apiUrl = "https://gateway.marvel.com/v1/public"
        var apiPublicKey = "b1591dee2496141b0dd1421376a9095b"
        var apiPrivateKey = "857b74e66bcb2a5276978cab203b7a186fe63b30"
        var JSON = "application/json; charset=utf-8".toMediaTypeOrNull()

        fun isConnectedToInternet(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

            return isConnected
        }

        fun normalize(str: String): String {
            val original = arrayOf("á", "é", "í", "ó", "ú", "â", "ê", "ô", "ã", "õ", "ç")
            val normalized =  arrayOf("a", "e", "i", "o", "u", "a", "e", "o", "a", "o", "c")

            return str.map { it ->
                val index = original.indexOf(it.toString())
                if (index >= 0) normalized[index] else it
            }.joinToString("")
        }

    }
}

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}