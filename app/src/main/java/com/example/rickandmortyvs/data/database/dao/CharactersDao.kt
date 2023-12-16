package com.example.rickandmortyvs.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmortyvs.data.database.models.DBCharacter

@Dao
interface CharactersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCharactersList(characters: List<DBCharacter>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSpecificCharacter(character: DBCharacter)

    @Query("DELETE FROM ${DBCharacter.TABLE_NAME}")
    suspend fun clearAll()

    @Query("SELECT * FROM ${DBCharacter.TABLE_NAME} WHERE ${DBCharacter.CHARACTER_ID} = :id")
    suspend fun getSpecificCharacter(id: Int): DBCharacter?

    @Query("SELECT * FROM ${DBCharacter.TABLE_NAME} WHERE ${DBCharacter.CHARACTER_ID} IN (:ids)")
    suspend fun getMultipleCharacters(ids: List<Int>): List<DBCharacter>?

    @Query("""
        SELECT * FROM ${DBCharacter.TABLE_NAME}
        WHERE (${DBCharacter.CHARACTER_NAME} LIKE '%' || :name || '%')
        AND (${DBCharacter.CHARACTER_STATUS} = :status)
        AND (${DBCharacter.CHARACTER_SPECIES} LIKE '%' || :species || '%')
        AND (${DBCharacter.CHARACTER_TYPE} LIKE '%' || :type || '%')
        AND (${DBCharacter.CHARACTER_GENDER} = :gender)
        """)
    fun pagingSource(
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?
    ): PagingSource<Int, DBCharacter>

}
