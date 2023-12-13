package com.example.rickandmortyvs.data.network.models

import com.google.gson.annotations.SerializedName

data class RestCharactersList(
    val info: RestCharactersListInfo,
    val characters: List<RestCharacter>
)