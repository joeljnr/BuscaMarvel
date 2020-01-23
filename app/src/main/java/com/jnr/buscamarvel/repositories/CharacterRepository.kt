package com.jnr.buscamarvel.repositories

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jnr.buscamarvel.models.Character
import com.jnr.buscamarvel.models.Result
import com.jnr.buscamarvel.utils.Utils
import com.jnr.buscamarvel.utils.md5
import com.jnr.buscamarvel.views.CharactersActivity
import okhttp3.*
import java.util.*
import kotlin.collections.ArrayList
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.sql.Timestamp
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import kotlin.coroutines.coroutineContext

object CharacterRepository {

    private var dataset = Result()

    fun getCharacters(context: Context, completion: (MutableLiveData<ArrayList<Character>>) -> Unit) {
        val data = MutableLiveData<ArrayList<Character>>()

        setCharacters(context) {
            data.value = dataset.data.results
            completion(data)
        }
    }

    private fun setCharacters(context: Context, completion: () -> Unit) {
        val act = context as CharactersActivity

        val timestamp = (System.currentTimeMillis()/1000).toString()

        val hash: String = (timestamp + Utils.apiPrivateKey + Utils.apiPublicKey)

        val url = Utils.apiUrl + "/characters?ts=$timestamp&apikey=${Utils.apiPublicKey}&hash=${hash.md5()}"

        val request = Request.Builder().url(url).get().build()
        val client = OkHttpClient.Builder().build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                val gson = GsonBuilder().create()

                dataset = gson.fromJson(body, Result::class.java)

                if (dataset.status == "Ok") {
                    act.runOnUiThread {
                        completion()
                    }
                } else {
                    act.runOnUiThread {
                        Toast.makeText(context, "Algo deu errado!", Toast.LENGTH_LONG).show()
                        completion()
                    }
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("CharacterRepository", "Erro!: ${e.localizedMessage}")
                act.runOnUiThread {
                    Toast.makeText(context, "Algo deu errado!", Toast.LENGTH_LONG).show()
                    completion()
                }
            }

        })
    }
}