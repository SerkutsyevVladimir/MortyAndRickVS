package com.example.rickandmortyvs.adapters.episodes

import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortyvs.databinding.EpisodesItemBinding
import com.example.rickandmortyvs.domain.models.episodes.Episode

class EpisodesViewHolder(
    private val binding: EpisodesItemBinding,
    private val onUserClicked: (Episode) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(episode: Episode?) = with(binding) {
        if (episode != null) {
            episodeName.text = episode.name
            episodeCode.text = episode.episode
            episodeAirDate.text = episode.airDate

            episodesCardView.setOnClickListener {
                onUserClicked(episode)
            }
        }
    }
}