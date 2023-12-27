package com.example.rickandmortyvs.domain.repository.location

import androidx.paging.Pager
import com.example.rickandmortyvs.data.database.models.location.DBLocationDetails
import com.example.rickandmortyvs.domain.models.locations.LocationDetails

interface LocationDetailsRepository {
    suspend fun getLocationsList(
        name: String?,
        dimension: String?,
        type: String?
    ): Pager<Int, DBLocationDetails>

    suspend fun getSpecificRestLocationDetails(id: Int): LocationDetails?

    suspend fun getMultipleRestLocationDetails(ids: List<Int>): List<LocationDetails>?

    suspend fun getSpecificDBLocationDetails(id: Int): LocationDetails?

    suspend fun getMultipleDBLocationDetails(ids: List<Int>): List<LocationDetails>?
}