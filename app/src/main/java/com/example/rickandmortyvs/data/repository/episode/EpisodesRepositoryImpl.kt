package com.example.rickandmortyvs.data.repository.episode

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.rickandmortyvs.data.database.AppDatabase
import com.example.rickandmortyvs.data.database.models.episode.DBEpisode
import com.example.rickandmortyvs.data.network.api.RickAndMortyApi
import com.example.rickandmortyvs.data.paging.episode.EpisodeRemoteMediator
import com.example.rickandmortyvs.domain.mappers.db.episode.DBEpisodeMapper
import com.example.rickandmortyvs.domain.mappers.rest.episode.RestEpisodeMapper
import com.example.rickandmortyvs.domain.models.episodes.Episode
import com.example.rickandmortyvs.domain.repository.episode.EpisodesRepository
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class EpisodesRepositoryImpl @Inject constructor(
    private val rickAndMortyApi: RickAndMortyApi,
    private val restEpisodeMapper: RestEpisodeMapper,
    private val appDatabase: AppDatabase,
    private val dbEpisodeMapper: DBEpisodeMapper
) : EpisodesRepository {
    override suspend fun getEpisodesList(
        name: String?,
        episode: String?
    ): Pager<Int, DBEpisode> {
        val episodesRemoteMediator = EpisodeRemoteMediator(
            name,
            episode,
            appDatabase,
            rickAndMortyApi,
            restEpisodeMapper
        )

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = episodesRemoteMediator,
            pagingSourceFactory = {
                appDatabase.getEpisodesDao().pagingSource(
                    name,
                    episode
                )
            }
        )


    }

    override suspend fun getSpecificRestEpisode(id: Int): Episode? {
        val response = rickAndMortyApi.getSpecificEpisode(id)
        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            appDatabase.getEpisodesDao()
                .addSpecificEpisode(restEpisodeMapper.mapToDBModel(responseBody))
            return restEpisodeMapper.map(responseBody)
        } else {
            return null
        }
    }

    override suspend fun getMultipleRestEpisodes(ids: List<Int>): List<Episode>? {

        return if (ids.isNotEmpty()) {
            if (ids.size == 1) {
                val restEpisode = rickAndMortyApi.getSpecificEpisode(ids[0]).body()
                if (restEpisode != null) {
                    appDatabase.getEpisodesDao()
                        .addSpecificEpisode(restEpisodeMapper.mapToDBModel(restEpisode))
                    return mutableListOf(restEpisodeMapper.map(restEpisode))
                }
            }
            val restEpisodes = rickAndMortyApi.getMultipleEpisodes(ids.joinToString(",")).body()
            if (restEpisodes != null) {
                appDatabase.getEpisodesDao()
                    .addEpisodesList(restEpisodes.map { restEpisodeMapper.mapToDBModel(it) })
                restEpisodes.map { restEpisodeMapper.map(it) }
            } else {
                null
            }
        } else {
            return null
        }

    }

    override suspend fun getSpecificDBEpisode(id: Int): Episode? {
        val dbEpisode = appDatabase.getEpisodesDao().getSpecificEpisode(id)
        return if (dbEpisode != null) {
            dbEpisodeMapper.map(dbEpisode)
        } else {
            null
        }
    }

    override suspend fun getMultipleDBEpisodes(ids: List<Int>): List<Episode>? {
        return if (ids.isNotEmpty()) {
            val dbEpisodes = appDatabase.getEpisodesDao().getMultipleEpisodes(ids)
            dbEpisodes?.map { dbEpisodeMapper.map(it) }
        } else {
            null
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}