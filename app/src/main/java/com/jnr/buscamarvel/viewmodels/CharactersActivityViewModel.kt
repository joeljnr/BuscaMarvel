package com.jnr.buscamarvel.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jnr.buscamarvel.repositories.CharacterRepository
import com.jnr.buscamarvel.models.Character
import com.jnr.buscamarvel.utils.Utils

class CharactersActivityViewModel: ViewModel() {
    private lateinit var characters: ArrayList<Character>
    private var mIsUpdating: MutableLiveData<Boolean> = MutableLiveData()
    private var mPageCounter: MutableLiveData<Int> = MutableLiveData()
    private lateinit var mVisibleCharacters: MutableLiveData<ArrayList<Character>>

    fun initialize(context: Context, completion: () -> Unit) {
        if (!::mVisibleCharacters.isInitialized) {
            mIsUpdating.value = true
            CharacterRepository.getCharacters(context) {
                characters = it.value!!
                mVisibleCharacters = it
                mPageCounter.value = (it.value!!.size / 4)
                mIsUpdating.postValue(false)
                completion()
            }
        }
    }

    fun getCharacters(page: Int): LiveData<ArrayList<Character>> {
        println("PAGE: " + page + "\nCOUNT: " + characters.size)
        if ((page >= 0 || page < characters.size) && page * 4 < characters.size) {
            mVisibleCharacters.value =
                ArrayList(characters.subList(page * 4, characters.size))
        } else {
            mVisibleCharacters = MutableLiveData()
        }

        return mVisibleCharacters

    }

    fun searchCharacters(text: String): LiveData<ArrayList<Character>> {
        val s = Utils.normalize(text)
        mVisibleCharacters = MutableLiveData()
        mVisibleCharacters.value = ArrayList()
        for (c in characters) {
            val name = Utils.normalize(c.name)

            if (name.contains(s, ignoreCase = true)) {
                mVisibleCharacters.value!!.add(c)
            }
        }

        return mVisibleCharacters
    }

    fun getIsUpdating(): LiveData<Boolean> {
        return mIsUpdating
    }

    fun getPages(): LiveData<Int> {
        mPageCounter.value = (mVisibleCharacters.value!!.size / 4)
        return mPageCounter
    }
}