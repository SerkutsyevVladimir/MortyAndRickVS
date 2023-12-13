package com.example.rickandmortyvs.data.network.models

import com.google.gson.annotations.SerializedName

data class RestLocation(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)