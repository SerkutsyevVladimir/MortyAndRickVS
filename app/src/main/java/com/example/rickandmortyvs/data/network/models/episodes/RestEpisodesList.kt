package com.example.rickandmortyvs.data.network.models.episodes

import com.google.gson.annotations.SerializedName

data class RestEpisodesList(
    val info: RestEpisodesListInfo,
    val results: List<RestEpisode>
) {
    data class RestEpisodesListInfo(
        @SerializedName("count") val count: Int,
        @SerializedName("pages") val pages: Int,
        @SerializedName("next") val next: String?,
        @SerializedName("prev") val prev: String?
    )


    data class RestEpisode(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("air_date") val airDate: String,
        @SerializedName("episode") val episode: String,
        @SerializedName("characters") val characters: List<String>,
        @SerializedName("url") val url: String,
        @SerializedName("created") val created: String
    )
}