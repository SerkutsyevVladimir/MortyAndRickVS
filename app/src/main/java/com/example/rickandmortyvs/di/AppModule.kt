package com.example.rickandmortyvs.di

import com.example.rickandmortyvs.data.network.api.RickAndMortyApi
import com.example.rickandmortyvs.domain.mappers.db.DBCharacterMapper
import com.example.rickandmortyvs.domain.mappers.rest.RestCharacterMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRickAndMortyApiInstance(): RickAndMortyApi = RickAndMortyApi.getInstance()

    @Provides
    @Singleton
    fun provideRestCharacterMapper(): RestCharacterMapper {
        return RestCharacterMapper()
    }

    @Provides
    @Singleton
    fun provideDBCharacterMapper(): DBCharacterMapper {
        return DBCharacterMapper()
    }
}