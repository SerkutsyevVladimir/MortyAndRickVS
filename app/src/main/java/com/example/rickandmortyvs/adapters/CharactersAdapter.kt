package com.example.rickandmortyvs.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.rickandmortyvs.databinding.CharactersItemBinding
import com.example.rickandmortyvs.domain.models.Characters

class CharactersAdapter(
    private val onUserClicked: (Characters) -> Unit
) :
    PagingDataAdapter<Characters, CharactersViewHolder>(DIFF_UTIL) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CharactersViewHolder(
            binding = CharactersItemBinding.inflate(layoutInflater, parent, false),
            onUserClicked = onUserClicked
        )
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bind(photo)
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
