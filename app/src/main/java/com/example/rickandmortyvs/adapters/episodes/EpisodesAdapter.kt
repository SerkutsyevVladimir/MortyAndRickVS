package com.example.rickandmortyvs.adapters.episodes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.rickandmortyvs.databinding.EpisodesItemBinding
import com.example.rickandmortyvs.domain.models.episodes.Episode

class EpisodesAdapter(
    private val onUserClicked: (Episode) -> Unit
) :
    PagingDataAdapter<Episode, EpisodesViewHolder>(DIFF_UTIL) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EpisodesViewHolder(
            binding = EpisodesItemBinding.inflate(layoutInflater, parent, false),
            onUserClicked = onUserClicked
        )
    }

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        val episode = getItem(position)
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