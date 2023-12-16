package com.example.rickandmortyvs.domain.usecases

import com.example.rickandmortyvs.domain.models.Characters
import com.example.rickandmortyvs.domain.repository.CharactersRepository
import javax.inject.Inject

class GetMultipleRestCharactersUseCase @Inject constructor(
    private val charactersRepositoryImpl: CharactersRepository
) {

    suspend operator fun invoke(ids: List<Int>): Result<List<Characters>?>{
        return runCatching {charactersRepositoryImpl.getMultipleRestCharacters(ids)}
    }
}