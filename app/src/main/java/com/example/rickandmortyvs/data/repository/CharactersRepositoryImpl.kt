package com.example.rickandmortyvs.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.rickandmortyvs.data.database.AppDatabase
import com.example.rickandmortyvs.data.database.dao.CharactersDao
import com.example.rickandmortyvs.data.database.models.DBCharacter
import com.example.rickandmortyvs.data.network.api.RickAndMortyApi
import com.example.rickandmortyvs.data.paging.CharactersRemoteMediator
import com.example.rickandmortyvs.domain.mappers.db.DBCharacterMapper
import com.example.rickandmortyvs.domain.mappers.rest.RestCharacterMapper
import com.example.rickandmortyvs.domain.models.Characters
import com.example.rickandmortyvs.domain.repository.CharactersRepository
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CharactersRepositoryImpl @Inject constructor(
    private val rickAndMortyApi: RickAndMortyApi,
    private val charactersDao: CharactersDao,
    private val restCharacterMapper: RestCharacterMapper,
    private val appDatabase: AppDatabase,
    private val dbCharacterMapper: DBCharacterMapper
) : CharactersRepository {
    override suspend fun getCharactersList(
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ): Pager<Int, DBCharacter> {
        val charactersRemoteMediator = CharactersRemoteMediator(
            name,
            status,
            gender,
            type,
            gender,
            appDatabase,
            rickAndMortyApi,
            restCharacterMapper
        )

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = charactersRemoteMediator,
            pagingSourceFactory = {
                appDatabase.getCharactersDao().pagingSource(
                    name,
                    status,
                    species,
                    type,
                    gender
                )
            }
        )


    }

    override suspend fun getSpecificRestCharacter(id: Int): Characters? {
        val response = rickAndMortyApi.getSpecificCharacter(id)
        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            appDatabase.getCharactersDao()
                .addSpecificCharacter(restCharacterMapper.mapToDBModel(responseBody))
            return restCharacterMapper.map(responseBody)
        } else {
            return null
        }
    }

    override suspend fun getMultipleRestCharacters(ids: List<Int>): List<Characters>? {
        return if (ids.isNotEmpty()) {
            val restCharacters = rickAndMortyApi.getMultipleCharacters(ids.joinToString(",")).body()
            if (restCharacters != null) {
                appDatabase.getCharactersDao()
                    .addCharactersList(restCharacters.map { restCharacterMapper.mapToDBModel(it) })
                restCharacters.map { restCharacterMapper.map(it) }
            } else {
                null
            }
        } else {
            null
        }

    }

    override suspend fun getSpecificDBCharacter(id: Int): Characters? {
        val dbCharacter = appDatabase.getCharactersDao().getSpecificCharacter(id)
        return if (dbCharacter != null) {
            dbCharacterMapper.map(dbCharacter)
        } else {
            null
        }
    }

    override suspend fun getMultipleDBCharacters(ids: List<Int>): List<Characters>? {
        return if (ids.isNotEmpty()) {
            val dbCharacters = appDatabase.getCharactersDao().getMultipleCharacters(ids)
            dbCharacters?.map { dbCharacterMapper.map(it) }
        } else {
            null
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}