package com.example.rickandmortyvs.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.rickandmortyvs.data.database.AppDatabase
import com.example.rickandmortyvs.data.database.models.DBCharacter
import com.example.rickandmortyvs.data.network.api.RickAndMortyApi
import com.example.rickandmortyvs.domain.mappers.rest.RestCharacterMapper
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CharactersRemoteMediator(
    private val name: String?,
    private val status: String?,
    private val species: String?,
    private val type: String?,
    private val gender: String?,
    private val appDatabase: AppDatabase,
    private val rickAndMortyApi: RickAndMortyApi,
    private val restCharacterMapper: RestCharacterMapper
) : RemoteMediator<Int, DBCharacter>() {
    private val charactersDao = appDatabase.getCharactersDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DBCharacter>
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

            val response = rickAndMortyApi.getCharactersList(
                page = loadKey,
                name = name,
                status = status,
                species = species,
                type = type,
                gender = gender
            ).body()?.results

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {

                }

                val dbModels = response?.map { restCharacterMapper.mapToDBModel(it) }
                if (!dbModels.isNullOrEmpty()) {
                    charactersDao.addCharactersList(
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