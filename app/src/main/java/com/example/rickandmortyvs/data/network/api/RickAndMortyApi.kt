package com.example.rickandmortyvs.data.network.api

import com.example.rickandmortyvs.data.network.models.RestCharactersList
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