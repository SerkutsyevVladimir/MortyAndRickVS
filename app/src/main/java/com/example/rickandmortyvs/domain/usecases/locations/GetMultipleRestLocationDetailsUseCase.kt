package com.example.rickandmortyvs.domain.usecases.locations

import com.example.rickandmortyvs.domain.models.locations.LocationDetails
import com.example.rickandmortyvs.domain.repository.location.LocationDetailsRepository
import javax.inject.Inject

class GetMultipleRestLocationDetailsUseCase @Inject constructor(
    private val locationDetailsRepositoryImpl: LocationDetailsRepository
) {

    suspend operator fun invoke(ids: List<Int>): Result<List<LocationDetails>?> {
        return runCatching { locationDetailsRepositoryImpl.getMultipleRestLocationDetails(ids) }
    }
}