package com.example.rickandmortyvs.domain.models.locations

data class LocationDetails(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>,
    val url: String,
    val created: String
)