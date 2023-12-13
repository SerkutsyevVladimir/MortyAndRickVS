package com.example.rickandmortyvs.data.network.models

import com.google.gson.annotations.SerializedName

data class RestOrigin(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)