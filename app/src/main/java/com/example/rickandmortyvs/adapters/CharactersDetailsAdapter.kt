package com.example.rickandmortyvs.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.rickandmortyvs.adapters.episodes.EpisodesViewHolder
import com.example.rickandmortyvs.databinding.EpisodesItemBinding
import com.example.rickandmortyvs.domain.models.episodes.Episode

class CharactersDetailsAdapter(
    private val onUserClicked: (Episode) -> Unit
) : ListAdapter<Episode, EpisodesViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EpisodesViewHolder(
            binding = EpisodesItemBinding.inflate(layoutInflater, parent, false),
            onUserClicked = onUserClicked
        )
    }

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        val episode = currentList[position]
        holder.bind(episode)
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Episode>() {
            override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                return oldItem == newItem
            }
        }
    }
}