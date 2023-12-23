package com.example.rickandmortyvs.viewmodels.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmortyvs.domain.models.locations.LocationDetails
import com.example.rickandmortyvs.domain.models.locations.LocationSearchParameters
import com.example.rickandmortyvs.domain.models.locations.LocationsSearchOptions
import com.example.rickandmortyvs.domain.usecases.locations.GetLocationDetailsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val getLocationDetailsListUseCase: GetLocationDetailsListUseCase
) : ViewModel() {

    private val _nameStateFlow = MutableStateFlow<String?>("")
    val nameStateFlow: StateFlow<String?> = _nameStateFlow

    private val _dimensionStateFlow = MutableStateFlow<String?>("")
    val dimensionStateFlow: StateFlow<String?> = _dimensionStateFlow

    private val _typeStateFlow = MutableStateFlow<String?>("")
    val typeStateFlow: StateFlow<String?> = _typeStateFlow


    fun getLocationDetailsList(): Flow<PagingData<LocationDetails>> {
        return combine(
            nameStateFlow,
            dimensionStateFlow,
            typeStateFlow
        ) { name, dimension, type ->
            LocationSearchParameters(
                name = name,
                dimension = dimension,
                type = type
            )
        }.flatMapLatest { searchParameters ->
            getLocationDetailsListUseCase.invoke(
                searchParameters.name,
                searchParameters.dimension,
                searchParameters.type
            )
        }.cachedIn(viewModelScope)
    }

    fun clearSearch() {
        _nameStateFlow.value = null
        _dimensionStateFlow.value = null
        _typeStateFlow.value = null
    }

    fun setSearchParameters(charactersSearchOptions: LocationsSearchOptions, searchText: String) {
        when (charactersSearchOptions) {
            LocationsSearchOptions.NAME -> _nameStateFlow.value = searchText
            LocationsSearchOptions.DIMENSION -> _dimensionStateFlow.value = searchText
            LocationsSearchOptions.TYPE -> _typeStateFlow.value = searchText
        }
    }


}