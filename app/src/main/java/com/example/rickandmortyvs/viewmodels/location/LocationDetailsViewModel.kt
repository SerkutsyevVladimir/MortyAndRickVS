package com.example.rickandmortyvs.viewmodels.location

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyvs.domain.models.locations.LocationDetails
import com.example.rickandmortyvs.domain.usecases.locations.GetSpecificDBLocationDetailsUseCase
import com.example.rickandmortyvs.domain.usecases.locations.GetSpecificRestLocationDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LocationDetailsViewModel @Inject constructor(
    private val getSpecificDBLocationDetailsUseCase: GetSpecificDBLocationDetailsUseCase,
    private val getSpecificRestLocationDetailsUseCase: GetSpecificRestLocationDetailsUseCase
) :ViewModel() {

    private val _networkStateFlow = MutableStateFlow(false)
    private val networkStateFlow: StateFlow<Boolean> = _networkStateFlow

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


    suspend fun getLocationDetails(id: Int) : LocationDetails? {
        return withContext(Dispatchers.IO) {
            if (networkStateFlow.value) {
                getLocationDetailsFromApi(id)
            } else {
                getLocationDetailsFromDB(id)
            }
        }
    }

    private suspend fun getLocationDetailsFromApi(id: Int): LocationDetails?{
        return withContext(Dispatchers.IO){
            getSpecificRestLocationDetailsUseCase.invoke(id).getOrNull()
        }
    }

    private suspend fun getLocationDetailsFromDB(id: Int) : LocationDetails?{
        return withContext(Dispatchers.IO){
            getSpecificDBLocationDetailsUseCase.invoke(id).getOrNull()
        }

    }
}