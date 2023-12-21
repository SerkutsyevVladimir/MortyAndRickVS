package com.example.rickandmortyvs.di

import com.example.rickandmortyvs.data.database.AppDatabase
import com.example.rickandmortyvs.data.database.dao.CharactersDao
import com.example.rickandmortyvs.data.network.api.RickAndMortyApi
import com.example.rickandmortyvs.data.repository.CharactersRepositoryImpl
import com.example.rickandmortyvs.data.repository.locations.LocationDetailsRepositoryImpl
import com.example.rickandmortyvs.domain.mappers.db.DBCharacterMapper
import com.example.rickandmortyvs.domain.mappers.db.location.DBLocationDetailsMapper
import com.example.rickandmortyvs.domain.mappers.rest.RestCharacterMapper
import com.example.rickandmortyvs.domain.mappers.rest.location.RestLocationDetailsMapper
import com.example.rickandmortyvs.domain.repository.CharactersRepository
import com.example.rickandmortyvs.domain.repository.location.LocationDetailsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideCharactersRepository(
        rickAndMortyApi: RickAndMortyApi,
        charactersDao: CharactersDao,
        restCharacterMapper: RestCharacterMapper,
        appDatabase: AppDatabase,
        dbCharacterMapper: DBCharacterMapper
    ): CharactersRepository {
        return CharactersRepositoryImpl(
            rickAndMortyApi,
            charactersDao,
            restCharacterMapper,
            appDatabase,
            dbCharacterMapper
        )
    }

    @Provides
    @Singleton
    fun provideLocationDetailsRepository(
        rickAndMortyApi: RickAndMortyApi,
        restLocationDetailsMapper: RestLocationDetailsMapper,
        appDatabase: AppDatabase,
        dbLocationDetailsMapper: DBLocationDetailsMapper
    ): LocationDetailsRepository {
        return LocationDetailsRepositoryImpl(
            rickAndMortyApi,
            restLocationDetailsMapper,
            appDatabase,
            dbLocationDetailsMapper
        )
    }

}