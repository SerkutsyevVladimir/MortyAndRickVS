package com.example.rickandmortyvs.data.network.models

import com.google.gson.annotations.SerializedName

data class RestCharactersList(
    val info: RestCharactersListInfo,
    val results: List<RestCharacter>
) {
    data class RestCharactersListInfo(
        @SerializedName("count") val count: Int,
        @SerializedName("pages") val pages: Int,
        @SerializedName("next") val next: String?,
        @SerializedName("prev") val prev: String?
    )


    data class RestCharacter(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("status") val status: String,
        @SerializedName("species") val species: String,
        @SerializedName("type") val type: String,
        @SerializedName("gender") val gender: String,
        @SerializedName("origin") val origin: RestOrigin,
        @SerializedName("location") val location: RestLocation,
        @SerializedName("image") val image: String,
        @SerializedName("episode") val episode: List<String>,
        @SerializedName("url") val url: String,
        @SerializedName("created") val created: String
    )

    data class RestLocation(
        @SerializedName("name") val name: String,
        @SerializedName("url") val url: String
    )

    data class RestOrigin(
        @SerializedName("name") val name: String,
        @SerializedName("url") val url: String
    )
}