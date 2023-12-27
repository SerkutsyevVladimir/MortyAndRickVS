package com.example.rickandmortyvs.data.paging.episode

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.rickandmortyvs.data.database.AppDatabase
import com.example.rickandmortyvs.data.database.models.episode.DBEpisode
import com.example.rickandmortyvs.data.network.api.RickAndMortyApi
import com.example.rickandmortyvs.domain.mappers.rest.episode.RestEpisodeMapper
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class EpisodeRemoteMediator(
    private val name: String?,
    private val episode: String?,
    private val appDatabase: AppDatabase,
    private val rickAndMortyApi: RickAndMortyApi,
    private val restEpisodeMapper: RestEpisodeMapper
) : RemoteMediator<Int, DBEpisode>() {
    private val episodeDao = appDatabase.getEpisodesDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DBEpisode>
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

            val response = rickAndMortyApi.getEpisodesList(
                page = loadKey,
                name = name,
                episode = episode
            ).body()?.results

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {

                }

                val dbModels = response?.map { restEpisodeMapper.mapToDBModel(it) }
                if (!dbModels.isNullOrEmpty()) {
                    episodeDao.addEpisodesList(
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