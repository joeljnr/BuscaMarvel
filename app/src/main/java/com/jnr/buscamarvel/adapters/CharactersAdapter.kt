package com.jnr.buscamarvel.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jnr.buscamarvel.R
import com.jnr.buscamarvel.models.Character
import kotlinx.android.synthetic.main.row_character.view.*

class CharactersAdapter(val characters: ArrayList<Character>): RecyclerView.Adapter<CharacterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.row_character, parent, false)
        return CharacterViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        if (characters.size < 4) {
            return characters.size
        } else {
            return 4
        }
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.view.textCharacterName.text = characters[position].name
        val imgUrl = characters[position].thumbnail.path + "/standard_medium." + characters[position].thumbnail.extension
        Glide.with(holder.view)
            .load(imgUrl.replace("http", "https"))
            .centerCrop()
            .placeholder(R.drawable.marvel)
            .into(holder.view.imgCharacter)
    }

}

class CharacterViewHolder(var view: View): RecyclerView.ViewHolder(view) {

}