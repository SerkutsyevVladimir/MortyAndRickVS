package com.example.rickandmortyvs.data.repository.locations

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.rickandmortyvs.data.database.AppDatabase
import com.example.rickandmortyvs.data.database.models.location.DBLocationDetails
import com.example.rickandmortyvs.data.network.api.RickAndMortyApi
import com.example.rickandmortyvs.data.paging.location.LocationDetailsRemoteMediator
import com.example.rickandmortyvs.domain.mappers.db.location.DBLocationDetailsMapper
import com.example.rickandmortyvs.domain.mappers.rest.location.RestLocationDetailsMapper
import com.example.rickandmortyvs.domain.models.locations.LocationDetails
import com.example.rickandmortyvs.domain.repository.location.LocationDetailsRepository
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class LocationDetailsRepositoryImpl @Inject constructor(
    private val rickAndMortyApi: RickAndMortyApi,
    private val restLocationDetailsMapper: RestLocationDetailsMapper,
    private val appDatabase: AppDatabase,
    private val dbLocationDetailsMapper: DBLocationDetailsMapper
) : LocationDetailsRepository {
    override suspend fun getLocationsList(
        name: String?,
        dimension: String?,
        type: String?
    ): Pager<Int, DBLocationDetails> {
        val charactersRemoteMediator = LocationDetailsRemoteMediator(
            name,
            dimension,
            type,
            appDatabase,
            rickAndMortyApi,
            restLocationDetailsMapper
        )

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = charactersRemoteMediator,
            pagingSourceFactory = {
                appDatabase.getLocationsDao().pagingSource(
                    name,
                    dimension,
                    type
                )
            }
        )


    }

    override suspend fun getSpecificRestLocationDetails(id: Int): LocationDetails? {
        val response = rickAndMortyApi.getSpecificLocation(id)
        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            appDatabase.getLocationsDao()
                .addSpecificLocation(restLocationDetailsMapper.mapToDBModel(responseBody))
            return restLocationDetailsMapper.map(responseBody)
        } else {
            return null
        }
    }

    override suspend fun getMultipleRestLocationDetails(ids: List<Int>): List<LocationDetails>? {
        return if (ids.isNotEmpty()) {
            val restLocations = rickAndMortyApi.getMultipleLocations(ids.joinToString(",")).body()
            if (restLocations != null) {
                appDatabase.getLocationsDao()
                    .addLocationsList(restLocations.map { restLocationDetailsMapper.mapToDBModel(it) })
                restLocations.map { restLocationDetailsMapper.map(it) }
            } else {
                null
            }
        } else {
            null
        }

    }

    override suspend fun getSpecificDBLocationDetails(id: Int): LocationDetails? {
        val dbLocation = appDatabase.getLocationsDao().getSpecificLocation(id)
        return if (dbLocation != null) {
            dbLocationDetailsMapper.map(dbLocation)
        } else {
            null
        }
    }

    override suspend fun getMultipleDBLocationDetails(ids: List<Int>): List<LocationDetails>? {
        return if (ids.isNotEmpty()) {
            val dbLocations = appDatabase.getLocationsDao().getMultipleLocations(ids)
            dbLocations?.map { dbLocationDetailsMapper.map(it) }
        } else {
            null
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}