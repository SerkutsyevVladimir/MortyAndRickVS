package com.example.rickandmortyvs.di

import com.example.rickandmortyvs.data.database.AppDatabase
import com.example.rickandmortyvs.data.network.api.RickAndMortyApi
import com.example.rickandmortyvs.data.repository.CharactersRepositoryImpl
import com.example.rickandmortyvs.data.repository.episode.EpisodesRepositoryImpl
import com.example.rickandmortyvs.data.repository.locations.LocationDetailsRepositoryImpl
import com.example.rickandmortyvs.domain.mappers.db.DBCharacterMapper
import com.example.rickandmortyvs.domain.mappers.db.episode.DBEpisodeMapper
import com.example.rickandmortyvs.domain.mappers.db.location.DBLocationDetailsMapper
import com.example.rickandmortyvs.domain.mappers.rest.RestCharacterMapper
import com.example.rickandmortyvs.domain.mappers.rest.episode.RestEpisodeMapper
import com.example.rickandmortyvs.domain.mappers.rest.location.RestLocationDetailsMapper
import com.example.rickandmortyvs.domain.repository.CharactersRepository
import com.example.rickandmortyvs.domain.repository.episode.EpisodesRepository
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
        restCharacterMapper: RestCharacterMapper,
        appDatabase: AppDatabase,
        dbCharacterMapper: DBCharacterMapper
    ): CharactersRepository {
        return CharactersRepositoryImpl(
            rickAndMortyApi,
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

    @Provides
    @Singleton
    fun provideEpisodesRepository(
        rickAndMortyApi: RickAndMortyApi,
        restEpisodeMapper: RestEpisodeMapper,
        appDatabase: AppDatabase,
        dbEpisodeMapper: DBEpisodeMapper
    ): EpisodesRepository {
        return EpisodesRepositoryImpl(
            rickAndMortyApi,
            restEpisodeMapper,
            appDatabase,
            dbEpisodeMapper
        )
    }
}