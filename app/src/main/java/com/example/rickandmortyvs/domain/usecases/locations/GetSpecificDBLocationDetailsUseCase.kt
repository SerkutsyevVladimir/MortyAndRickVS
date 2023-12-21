package com.example.rickandmortyvs.domain.usecases.locations

import com.example.rickandmortyvs.domain.models.locations.LocationDetails
import com.example.rickandmortyvs.domain.repository.location.LocationDetailsRepository
import javax.inject.Inject

class GetSpecificDBLocationDetailsUseCase @Inject constructor(
    private val locationDetailsRepositoryImpl: LocationDetailsRepository
) {

    suspend operator fun invoke(id: Int): Result<LocationDetails?>{
        return runCatching {locationDetailsRepositoryImpl.getSpecificDBLocationDetails(id)}
    }
}