package com.example.rickandmortyvs.viewmodels

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyvs.domain.models.Characters
import com.example.rickandmortyvs.domain.models.episodes.Episode
import com.example.rickandmortyvs.domain.usecases.GetSpecificDBCharacterUseCase
import com.example.rickandmortyvs.domain.usecases.GetSpecificRestCharacterUseCase
import com.example.rickandmortyvs.domain.usecases.episodes.GetMultipleDBEpisodesUseCase
import com.example.rickandmortyvs.domain.usecases.episodes.GetMultipleRestEpisodesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val getSpecificDBCharacterUseCase: GetSpecificDBCharacterUseCase,
    private val getSpecificRestCharacterUseCase: GetSpecificRestCharacterUseCase,
    private val getMultipleRestEpisodesUseCase: GetMultipleRestEpisodesUseCase,
    private val getMultipleDBEpisodesUseCase: GetMultipleDBEpisodesUseCase
) : ViewModel() {

    private val _networkStateFlow = MutableStateFlow(false)
    private val networkStateFlow: StateFlow<Boolean> = _networkStateFlow

    private val _episodesStateFlow = MutableStateFlow<List<Episode>?>(emptyList())
    val episodeStateFlow: StateFlow<List<Episode>?> = _episodesStateFlow

    private val _characterStateFlow = MutableStateFlow<Characters?>(null)
    val characterStateFlow: StateFlow<Characters?> = _characterStateFlow

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


    suspend fun getCharacter(id: Int): Characters? {
        return withContext(Dispatchers.IO) {
            if (networkStateFlow.value) {
                getCharacterFromApi(id)
            } else {
                getCharacterFromDB(id)
            }
        }
    }

    private suspend fun getCharacterFromApi(id: Int): Characters? {
        return withContext(Dispatchers.IO) {
            getSpecificRestCharacterUseCase.invoke(id).getOrNull()
        }
    }

    private suspend fun getCharacterFromDB(id: Int): Characters? {
        return withContext(Dispatchers.IO) {
            getSpecificDBCharacterUseCase.invoke(id).getOrNull()
        }

    }

    suspend fun getMultipleEpisodes(episodes: List<String>?) {
        if (episodes != null) {
            val ids = episodes.map { episodeUrl -> episodeUrl.split("/").last().toInt() }
            withContext(Dispatchers.IO) {
                if (networkStateFlow.value) {
                    _episodesStateFlow.value = getMultipleEpisodesFromApi(ids)
                } else {
                    _episodesStateFlow.value = getMultipleEpisodesFromDB(ids)
                }
            }
        }
    }

    private suspend fun getMultipleEpisodesFromApi(ids: List<Int>): List<Episode>? {
        return withContext(Dispatchers.IO) {
            getMultipleRestEpisodesUseCase.invoke(ids).getOrNull()
        }
    }

    private suspend fun getMultipleEpisodesFromDB(ids: List<Int>): List<Episode>? {
        return withContext(Dispatchers.IO) {
            getMultipleDBEpisodesUseCase.invoke(ids).getOrNull()
        }

    }
}