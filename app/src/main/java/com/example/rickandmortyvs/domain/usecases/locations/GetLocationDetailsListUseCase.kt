package com.example.rickandmortyvs.domain.usecases.locations

import androidx.paging.PagingData
import androidx.paging.map
import com.example.rickandmortyvs.domain.mappers.db.location.DBLocationDetailsMapper
import com.example.rickandmortyvs.domain.models.locations.LocationDetails
import com.example.rickandmortyvs.domain.repository.location.LocationDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLocationDetailsListUseCase @Inject constructor(
    private val locationDetailsRepositoryImpl: LocationDetailsRepository,
    private val dbLocationDetailsMapper: DBLocationDetailsMapper
) {

    suspend operator fun invoke(
        name: String?,
        dimension: String?,
        type: String?
    ): Flow<PagingData<LocationDetails>> {
        return locationDetailsRepositoryImpl
            .getLocationsList(
                name,
                dimension,
                type
            ).flow.map { it -> it.map { dbLocationDetailsMapper.map(it) } }
    }
}