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
            // The network load method takes an optional after=<user.id>
            // parameter. For every page after the first, pass the last user
            // ID to let it continue from where it left off. For REFRESH,
            // pass null to load the first page.
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()

                    // You must explicitly check if the last item is null when
                    // appending, since passing null to networkService is only
                    // valid for initial load. If lastItem is null it means no
                    // items were loaded after the initial REFRESH and there are
                    // no more items to load.
                    if (lastItem == null){
                        2
                    }else{
                        (lastItem.id / state.config.pageSize) + 1
                    }
                    /*{
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }

                    lastItem.id*/
                }
            }

            // Suspending network load via Retrofit. This doesn't need to be
            // wrapped in a withContext(Dispatcher.IO) { ... } block since
            // Retrofit's Coroutine CallAdapter dispatches on a worker
            // thread.
            val response = rickAndMortyApi.getEpisodesList(
                page = loadKey,
                name = name,
                episode = episode
            ).body()?.results

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    //charactersDao.clearAll()
                }

                // Insert new users into database, which invalidates the
                // current PagingData, allowing Paging to present the updates
                // in the DB.
                val test = response?.map { restEpisodeMapper.mapToDBModel(it) }
                if (
                //response != null
                    test?.isNotEmpty() == true
                ) {
                    episodeDao.addEpisodesList(
                        //response.map { restCharacterMapper.mapToDBModel(it) }
                        test
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