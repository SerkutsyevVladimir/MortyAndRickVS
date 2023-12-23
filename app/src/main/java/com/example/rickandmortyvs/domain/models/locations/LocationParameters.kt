package com.example.rickandmortyvs.domain.models.locations

import androidx.paging.PagingData

data class LocationParameters(
    val locations: PagingData<LocationDetails>? = PagingData.empty()
)