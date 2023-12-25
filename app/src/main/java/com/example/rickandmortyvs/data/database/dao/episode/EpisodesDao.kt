package com.example.rickandmortyvs.data.database.dao.episode

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmortyvs.data.database.models.episode.DBEpisode

@Dao
interface EpisodesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSpecificEpisode(episode: DBEpisode)

    @Query("DELETE FROM ${DBEpisode.TABLE_NAME}")
    suspend fun clearAll()

    @Query("SELECT * FROM ${DBEpisode.TABLE_NAME} WHERE ${DBEpisode.EPISODE_ID} = :id")
    suspend fun getSpecificEpisode(id: Int): DBEpisode?

    @Query("""
        SELECT * FROM ${DBEpisode.TABLE_NAME}
        WHERE (:name IS NULL OR ${DBEpisode.EPISODE_NAME} LIKE '%' || :name || '%')
        AND (:episode IS NULL OR ${DBEpisode.EPISODE_EPISODE} LIKE '%' || :episode || '%')
        """)
    fun pagingSource(
        name: String?,
        episode: String?
    ): PagingSource<Int, DBEpisode>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addEpisodesList(episodes: List<DBEpisode>)

    @Query("SELECT * FROM ${DBEpisode.TABLE_NAME} WHERE ${DBEpisode.EPISODE_ID} IN (:ids)")
    suspend fun getMultipleEpisodes(ids: List<Int>): List<DBEpisode>?
}