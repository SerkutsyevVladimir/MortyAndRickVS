package com.example.rickandmortyvs.data.database.dao.location

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickandmortyvs.data.database.models.location.DBLocationDetails

@Dao
interface LocationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSpecificLocation(location: DBLocationDetails)

    @Query("DELETE FROM ${DBLocationDetails.TABLE_NAME}")
    suspend fun clearAll()

    @Query("SELECT * FROM ${DBLocationDetails.TABLE_NAME} WHERE ${DBLocationDetails.LOCATION_ID} = :id")
    suspend fun getSpecificLocation(id: Int): DBLocationDetails?

    @Query("""
        SELECT * FROM ${DBLocationDetails.TABLE_NAME}
        WHERE (:name IS NULL OR ${DBLocationDetails.LOCATION_NAME} LIKE '%' || :name || '%')
        AND (:dimension IS NULL OR ${DBLocationDetails.LOCATION_DIMENSION} LIKE '%' || :dimension || '%')
        AND (:type IS NULL OR ${DBLocationDetails.LOCATION_TYPE} LIKE '%' || :type || '%')
        """)
    fun pagingSource(
        name: String?,
        dimension: String?,
        type: String?
    ): PagingSource<Int, DBLocationDetails>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLocationsList(locations: List<DBLocationDetails>)

    @Query("SELECT * FROM ${DBLocationDetails.TABLE_NAME} WHERE ${DBLocationDetails.LOCATION_ID} IN (:ids)")
    suspend fun getMultipleLocations(ids: List<Int>): List<DBLocationDetails>?
}