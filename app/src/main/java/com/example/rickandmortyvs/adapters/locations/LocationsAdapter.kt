package com.example.rickandmortyvs.adapters.locations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.rickandmortyvs.databinding.LoactionsItemBinding
import com.example.rickandmortyvs.domain.models.locations.LocationDetails

class LocationsAdapter(
    private val onUserClicked: (LocationDetails) -> Unit
) :
    PagingDataAdapter<LocationDetails, LocationsViewHolder>(DIFF_UTIL) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return LocationsViewHolder(
            binding = LoactionsItemBinding.inflate(layoutInflater, parent, false),
            onUserClicked = onUserClicked
        )
    }

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        val location = getItem(position)
        holder.bind(location)
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<LocationDetails>() {
            override fun areItemsTheSame(oldItem: LocationDetails, newItem: LocationDetails): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: LocationDetails, newItem: LocationDetails): Boolean {
                return oldItem == newItem
            }
        }
    }
}