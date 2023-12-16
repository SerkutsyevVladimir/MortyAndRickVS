package com.example.rickandmortyvs.domain.repository

import androidx.paging.Pager
import com.example.rickandmortyvs.data.database.models.DBCharacter
import com.example.rickandmortyvs.domain.models.Characters

interface CharactersRepository {

    suspend fun getCharactersList(
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ):Pager<Int,DBCharacter>

    suspend fun getSpecificRestCharacter(id: Int) : Characters?

    suspend fun getMultipleRestCharacters(ids: List<Int>) : List<Characters>?

    suspend fun getSpecificDBCharacter(id: Int) : Characters?

    suspend fun getMultipleDBCharacters(ids: List<Int>) : List<Characters>?

}