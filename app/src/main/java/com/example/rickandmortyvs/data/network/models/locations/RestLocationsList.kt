package com.example.rickandmortyvs.data.network.models.locations

import com.google.gson.annotations.SerializedName

data class RestLocationsList(
    val info: RestLocationsListInfo,
    val results: List<RestLocationDetails>
) {
    data class RestLocationsListInfo(
        @SerializedName("count") val count: Int,
        @SerializedName("pages") val pages: Int,
        @SerializedName("next") val next: String?,
        @SerializedName("prev") val prev: String?
    )


    data class RestLocationDetails(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("type") val type: String,
        @SerializedName("dimension") val dimension: String,
        @SerializedName("residents") val residents: List<String>,
        @SerializedName("url") val url: String,
        @SerializedName("created") val created: String
    )
}