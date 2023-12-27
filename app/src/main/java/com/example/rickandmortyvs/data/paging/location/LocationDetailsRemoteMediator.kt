package com.example.rickandmortyvs.data.paging.location

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.rickandmortyvs.data.database.AppDatabase
import com.example.rickandmortyvs.data.database.models.location.DBLocationDetails
import com.example.rickandmortyvs.data.network.api.RickAndMortyApi
import com.example.rickandmortyvs.domain.mappers.rest.location.RestLocationDetailsMapper
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class LocationDetailsRemoteMediator(
    private val name: String?,
    private val dimension: String?,
    private val type: String?,
    private val appDatabase: AppDatabase,
    private val rickAndMortyApi: RickAndMortyApi,
    private val restLocationDetailsMapper: RestLocationDetailsMapper
) : RemoteMediator<Int, DBLocationDetails>() {
    private val locationDao = appDatabase.getLocationsDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DBLocationDetails>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        2
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }

            val response = rickAndMortyApi.getLocationsList(
                page = loadKey,
                name = name,
                type = type,
                dimension = dimension
            ).body()?.results

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {

                }

                val dbModels = response?.map { restLocationDetailsMapper.mapToDBModel(it) }
                if (!dbModels.isNullOrEmpty()) {
                    locationDao.addLocationsList(
                        dbModels
                    )
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = response?.isEmpty() == true
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}