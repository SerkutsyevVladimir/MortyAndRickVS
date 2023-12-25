package com.example.rickandmortyvs.data.network.api

import com.example.rickandmortyvs.data.network.models.RestCharactersList
import com.example.rickandmortyvs.data.network.models.episodes.RestEpisodesList
import com.example.rickandmortyvs.data.network.models.locations.RestLocationsList
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("api/character")
    suspend fun getCharactersList(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("status") status: String?,
        @Query("species") species: String?,
        @Query("type") type: String?,
        @Query("gender") gender: String?
    ): Response<RestCharactersList>

    @GET("api/character/{id}")
    suspend fun getSpecificCharacter(
        @Path("id") id: Int
    ): Response<RestCharactersList.RestCharacter>

    @GET("api/character/{ids}")
    suspend fun getMultipleCharacters(
        @Path("ids") ids: String
    ): Response<List<RestCharactersList.RestCharacter>>


    @GET("api/location/{id}")
    suspend fun getSpecificLocation(
        @Path("id") id: Int
    ): Response<RestLocationsList.RestLocationDetails>

    @GET("api/location/{ids}")
    suspend fun getMultipleLocations(
        @Path("ids") ids: String
    ): Response<List<RestLocationsList.RestLocationDetails>>



    @GET("api/location")
    suspend fun getLocationsList(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("type") type: String?,
        @Query("dimension") dimension: String?
    ): Response<RestLocationsList>

    @GET("api/episode")
    suspend fun getEpisodesList(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("episode") episode: String?
    ): Response<RestEpisodesList>


    @GET("api/episode/{id}")
    suspend fun getSpecificEpisode(
        @Path("id") id: Int
    ): Response<RestEpisodesList.RestEpisode>

    @GET("api/episode/{ids}")
    suspend fun getMultipleEpisodes(
        @Path("ids") ids: String
    ): Response<List<RestEpisodesList.RestEpisode>>


    companion object {

        private const val BASE_URL = "https://rickandmortyapi.com/"
    fun getInstance(): RickAndMortyApi {

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create()
        }
    }
}