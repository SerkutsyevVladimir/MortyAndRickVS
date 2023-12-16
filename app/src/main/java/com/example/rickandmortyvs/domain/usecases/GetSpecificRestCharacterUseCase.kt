package com.example.rickandmortyvs.domain.usecases

import com.example.rickandmortyvs.domain.models.Characters
import com.example.rickandmortyvs.domain.repository.CharactersRepository
import javax.inject.Inject

class GetSpecificRestCharacterUseCase @Inject constructor(
    private val charactersRepositoryImpl: CharactersRepository
) {
    suspend operator fun invoke(id: Int): Result<Characters?>{
        return runCatching {charactersRepositoryImpl.getSpecificRestCharacter(id)}
    }
}