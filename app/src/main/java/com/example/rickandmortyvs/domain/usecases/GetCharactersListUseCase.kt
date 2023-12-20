package com.example.rickandmortyvs.domain.usecases

import androidx.paging.PagingData
import androidx.paging.map
import com.example.rickandmortyvs.domain.mappers.db.DBCharacterMapper
import com.example.rickandmortyvs.domain.models.Characters
import com.example.rickandmortyvs.domain.repository.CharactersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCharactersListUseCase @Inject constructor(
    private val charactersRepositoryImpl: CharactersRepository,
    private val dbCharacterMapper: DBCharacterMapper
) {

    suspend operator fun invoke(
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ): Flow<PagingData<Characters>> {
        return charactersRepositoryImpl
            .getCharactersList(
                name,
                status,
                species,
                type,
                gender
            ).flow.map { it -> it.map { dbCharacterMapper.map(it) }}
        }
    }

