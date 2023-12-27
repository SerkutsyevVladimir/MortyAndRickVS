package com.example.rickandmortyvs.viewmodels.episode

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyvs.domain.models.Characters
import com.example.rickandmortyvs.domain.models.episodes.Episode
import com.example.rickandmortyvs.domain.usecases.GetMultipleDBCharactersUseCase
import com.example.rickandmortyvs.domain.usecases.GetMultipleRestCharactersUseCase
import com.example.rickandmortyvs.domain.usecases.episodes.GetSpecificDBEpisodeUseCase
import com.example.rickandmortyvs.domain.usecases.episodes.GetSpecificRestEpisodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailsViewModel @Inject constructor(
    private val getSpecificDBEpisodeUseCase: GetSpecificDBEpisodeUseCase,
    private val getSpecificRestEpisodeUseCase: GetSpecificRestEpisodeUseCase,
    private val getMultipleRestCharactersUseCase: GetMultipleRestCharactersUseCase,
    private val getMultipleDBCharactersUseCase: GetMultipleDBCharactersUseCase
) : ViewModel() {

    private val _networkStateFlow = MutableStateFlow(false)
    private val networkStateFlow: StateFlow<Boolean> = _networkStateFlow

    private val _charactersStateFlow = MutableStateFlow<List<Characters>?>(emptyList())
    val charactersStateFlow: StateFlow<List<Characters>?> = _charactersStateFlow

    fun checkNetworkAvailability(context: Context) {
        viewModelScope.launch {
            _networkStateFlow.value = isOnline(context)
        }
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        }
        return false
    }


    suspend fun getEpisodeDetails(id: Int): Episode? {
        return withContext(Dispatchers.IO) {
            if (networkStateFlow.value) {
                getEpisodeFromApi(id)
            } else {
                getEpisodeFromDB(id)
            }
        }
    }

    private suspend fun getEpisodeFromApi(id: Int): Episode? {
        return withContext(Dispatchers.IO) {
            getSpecificRestEpisodeUseCase.invoke(id).getOrNull()
        }
    }

    private suspend fun getEpisodeFromDB(id: Int): Episode? {
        return withContext(Dispatchers.IO) {
            getSpecificDBEpisodeUseCase.invoke(id).getOrNull()
        }

    }

    suspend fun getMultipleCharacters(characters: List<String>?) {
        if (characters != null) {
            val ids = characters.map { characterUrl -> characterUrl.split("/").last().toInt() }
            withContext(Dispatchers.IO) {
                if (networkStateFlow.value) {
                    _charactersStateFlow.value = getMultipleCharactersFromApi(ids)
                } else {
                    _charactersStateFlow.value = getMultipleCharactersFromDB(ids)
                }
            }
        }
    }

    private suspend fun getMultipleCharactersFromApi(ids: List<Int>): List<Characters>? {
        return withContext(Dispatchers.IO) {
            getMultipleRestCharactersUseCase.invoke(ids).getOrNull()
        }
    }

    private suspend fun getMultipleCharactersFromDB(ids: List<Int>): List<Characters>? {
        return withContext(Dispatchers.IO) {
            getMultipleDBCharactersUseCase.invoke(ids).getOrNull()
        }

    }
}