package com.example.rickandmortyvs.domain.models

data class SearchParameters(
    val name: String? = null,
    val status: Status = Status.EMPTY,
    val species: String? = null,
    val type: String? = null,
    val gender: Gender = Gender.EMPTY
)