package com.example.rickandmortyvs.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmortyvs.databinding.CharactersItemBinding
import com.example.rickandmortyvs.domain.models.Characters

class CharactersViewHolder(
    private val binding: CharactersItemBinding,
    private val onUserClicked: (Characters) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(character: Characters?) = with(binding) {
        if (character != null) {
            Glide.with(charactersImageView.context)
                .load(character.image)
                .into(charactersImageView)

            charactersName.text = character.name
            charactersSpecies.text = character.species
            charactersStatus.text = character.status
            charactersGender.text = character.gender

            charactersCardView.setOnClickListener {
                onUserClicked(character)
            }
        }
    }
}