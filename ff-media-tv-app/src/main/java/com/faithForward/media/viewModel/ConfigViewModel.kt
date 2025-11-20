package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.network.dto.ConfigData
import com.faithForward.preferences.ConfigManager
import com.faithForward.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel to handle config data loading on app launch.
 * This should be called first before any other initialization.
 * Loads config directly from API every time app opens.
 */
@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isConfigLoaded = MutableStateFlow(false)
    val isConfigLoaded: StateFlow<Boolean> = _isConfigLoaded.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    companion object {
        private const val TAG = "ConfigViewModel"
    }

    init {
        // Load config on ViewModel initialization
        loadConfig()
    }

    /**
     * Load config data directly from API.
     * Stores the result in ConfigManager singleton.
     */
    fun loadConfig() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null

                // Fetch config data from API
                Log.e(TAG, "Fetching config data from API")
                val response = withContext(Dispatchers.IO) {
                    networkRepository.getConfigData()
                }

                if (response.isSuccessful) {
                    val configResponse = response.body()
                    if (configResponse != null && configResponse.data != null) {
                        // Store in singleton
                        ConfigManager.setConfigData(configResponse.data)
                        _isConfigLoaded.value = true
                        Log.e(TAG, "Config data loaded successfully from API")
                    } else {
                        Log.e(TAG, "Config response body is null or data is null")
                        _errorMessage.value = "Config data is empty"
                        _isConfigLoaded.value = false
                    }
                } else {
                    Log.e(TAG, "Config API failed with code: ${response.code()}, message: ${response.message()}")
                    _errorMessage.value = "Failed to load config: ${response.message()}"
                    _isConfigLoaded.value = false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception while loading config: ${e.message}", e)
                _errorMessage.value = "Network error: ${e.localizedMessage}"
                _isConfigLoaded.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Get current config data from ConfigManager singleton.
     */
    fun getConfigData(): ConfigData? = ConfigManager.getConfigData()
}
