package com.example.rickandmortyvs.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmortyvs.domain.models.Characters
import com.example.rickandmortyvs.domain.models.CharactersParameters
import com.example.rickandmortyvs.domain.models.CharactersSearchOptions
import com.example.rickandmortyvs.domain.models.Gender
import com.example.rickandmortyvs.domain.models.SearchParameters
import com.example.rickandmortyvs.domain.models.Status
import com.example.rickandmortyvs.domain.usecases.GetCharactersListUseCase
import com.example.rickandmortyvs.domain.usecases.GetMultipleDBCharactersUseCase
import com.example.rickandmortyvs.domain.usecases.GetMultipleRestCharactersUseCase
import com.example.rickandmortyvs.domain.usecases.GetSpecificDBCharacterUseCase
import com.example.rickandmortyvs.domain.usecases.GetSpecificRestCharacterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersListUseCase: GetCharactersListUseCase,
    private val getMultipleDBCharactersUseCase: GetMultipleDBCharactersUseCase,
    private val getMultipleRestCharactersUseCase: GetMultipleRestCharactersUseCase,
    private val getSpecificDBCharacterUseCase: GetSpecificDBCharacterUseCase,
    private val getSpecificRestCharacterUseCase: GetSpecificRestCharacterUseCase
) : ViewModel() {

    private val _nameStateFlow = MutableStateFlow<String?>("")
    val nameStateFlow: StateFlow<String?> = _nameStateFlow

    private val _statusStateFlow = MutableStateFlow(Status.EMPTY)
    val statusStateFlow: StateFlow<Status> = _statusStateFlow

    private val _speciesStateFlow = MutableStateFlow<String?>("")
    val speciesStateFlow: StateFlow<String?> = _speciesStateFlow

    private val _typeStateFlow = MutableStateFlow<String?>("")
    val typeStateFlow: StateFlow<String?> = _typeStateFlow

    private val _genderStateFlow = MutableStateFlow(Gender.EMPTY)
    val genderStateFlow: StateFlow<Gender> = _genderStateFlow

    private val _charactersStateFlow = MutableStateFlow(CharactersParameters())
    val charactersStateFlow: StateFlow<CharactersParameters> = _charactersStateFlow

    init {
        viewModelScope.launch {
            getCharactersList().collect {
                _charactersStateFlow.value = _charactersStateFlow.value.copy(
                    characters = it
                )
            }
        }
    }

    fun getCharactersList(): Flow<PagingData<Characters>> {
        return combine(
            nameStateFlow,
            statusStateFlow,
            speciesStateFlow,
            typeStateFlow,
            genderStateFlow
        ) { name, status, species, type, gender ->
            SearchParameters(
                name = name,
                status = status,
                species = species,
                type = type,
                gender = gender
            )
        }.flatMapLatest { searchParameters ->
            getCharactersListUseCase.invoke(
                searchParameters.name,
                searchParameters.status.statusType,
                searchParameters.species,
                searchParameters.type,
                searchParameters.gender.ganderType
            )
        }.cachedIn(viewModelScope)
    }

    fun addGenderFilter(gender: Gender) {
        _genderStateFlow.value = gender
        _charactersStateFlow.value = _charactersStateFlow.value.copy(isFilteringEnabled = true)
    }

    fun addStatusFilter(status: Status) {
        _statusStateFlow.value = status
        _charactersStateFlow.value = _charactersStateFlow.value.copy(isFilteringEnabled = true)
    }

    fun removeFilters() {
        _genderStateFlow.value = Gender.EMPTY
        _statusStateFlow.value = Status.EMPTY
        _charactersStateFlow.value = _charactersStateFlow.value.copy(isFilteringEnabled = false)
    }

    fun clearSearch() {
        _nameStateFlow.value = null
        _speciesStateFlow.value = null
        _typeStateFlow.value = null
    }

    fun setSearchParameters(charactersSearchOptions: CharactersSearchOptions, searchText: String) {
        when (charactersSearchOptions) {
            CharactersSearchOptions.NAME -> _nameStateFlow.value = searchText
            CharactersSearchOptions.SPECIES -> _speciesStateFlow.value = searchText
            CharactersSearchOptions.TYPE -> _typeStateFlow.value = searchText
        }
    }


}