package com.example.rickandmortyvs.viewmodels

import com.example.rickandmortyvs.domain.usecases.GetSpecificDBCharacterUseCase
import com.example.rickandmortyvs.domain.usecases.GetSpecificRestCharacterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val getSpecificDBCharacterUseCase: GetSpecificDBCharacterUseCase,
    private val getSpecificRestCharacterUseCase: GetSpecificRestCharacterUseCase
) {
}