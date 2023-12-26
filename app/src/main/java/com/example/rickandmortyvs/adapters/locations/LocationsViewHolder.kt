package com.example.rickandmortyvs.adapters.locations

import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortyvs.databinding.LoactionsItemBinding
import com.example.rickandmortyvs.domain.models.locations.LocationDetails

class LocationsViewHolder(
    private val binding: LoactionsItemBinding,
    private val onUserClicked: (LocationDetails) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(location: LocationDetails?) = with(binding) {
        if (location != null) {
            locationsName.text = location.name
            locationsDimension.text = location.dimension
            locationsType.text = location.type

            charactersCardView.setOnClickListener {
                onUserClicked(location)
            }
        }
    }
}