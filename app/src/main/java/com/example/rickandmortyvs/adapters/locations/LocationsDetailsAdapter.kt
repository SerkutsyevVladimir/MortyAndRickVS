package com.example.rickandmortyvs.adapters.locations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.rickandmortyvs.adapters.CharactersViewHolder
import com.example.rickandmortyvs.databinding.CharactersItemBinding
import com.example.rickandmortyvs.domain.models.Characters

class LocationsDetailsAdapter(
    private val onUserClicked: (Characters) -> Unit
) : ListAdapter<Characters, CharactersViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CharactersViewHolder(
            binding = CharactersItemBinding.inflate(layoutInflater, parent, false),
            onUserClicked = onUserClicked
        )
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val character = currentList[position]
        holder.bind(character)
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Characters>() {
            override fun areItemsTheSame(oldItem: Characters, newItem: Characters): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Characters, newItem: Characters): Boolean {
                return oldItem == newItem
            }
        }
    }
}