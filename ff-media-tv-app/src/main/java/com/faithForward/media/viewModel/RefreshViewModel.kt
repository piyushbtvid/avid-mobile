package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RefreshViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
) : ViewModel() {


    private var refreshJob: Job? = null

    private val _logoutEvent = MutableSharedFlow<Unit>(replay = 0)
    val logoutEvent = _logoutEvent.asSharedFlow()

    private val _isRefreshDataSaved = MutableSharedFlow<Unit>(replay = 1)
    val isRefreshDataSaved = _isRefreshDataSaved.asSharedFlow()

    private var isInitialRefresh = true

    init {
        Log.e(
            "REFRESH_TOKEN", "check refresh token called in INIT block in refresh ViewModel "
        )
        checkRefreshToken()
    }

    fun cancelRefreshJob() {
        refreshJob?.cancel()
        refreshJob = null
    }

    fun checkRefreshToken() {
        // Cancel any existing job
        cancelRefreshJob()

        refreshJob = viewModelScope.launch(Dispatchers.IO) {
            Log.e("REFRESH_TOKEN", "checkRefreshToken started")

            while (isActive) {
                try {
                    val session = networkRepository.getCurrentSession()
                    val season = session?.season
                    val refreshToken = season?.refreshToken
                    val expireTime = season?.expire_date ?: break
                    val currentTime = System.currentTimeMillis() / 1000 // in seconds
                    val timeLeft = expireTime - currentTime

                    if (refreshToken == null) {
                        break
                    }

                    if (timeLeft <= 3600) {
                        // Immediate refresh
                        refreshToken.let {
                            Log.e("REFRESH_TOKEN", "Refreshing now — time left: $timeLeft sec")
                            val success = handleRefresh(it)
                            if (success) {
                                if (isInitialRefresh) {
                                    _isRefreshDataSaved.emit(Unit)
                                    isInitialRefresh = false
                                }
                            } else {
                                if (isInitialRefresh) {
                                    _isRefreshDataSaved.emit(Unit)
                                    isInitialRefresh = false
                                }
                                return@launch
                            }
                        }
                    } else {
                        if (isInitialRefresh) {
                            Log.e("REFRESH_TOKEN", "is refresh data saved emit called in else ")
                            _isRefreshDataSaved.emit(Unit)
                            isInitialRefresh = false
                        }
                        val delayTime = (timeLeft - 3600) * 1000L // milliseconds
                        Log.e("REFRESH_TOKEN", "Delaying refresh for $delayTime ms")
                        delay(delayTime)

                        refreshToken.let {
                            Log.e("REFRESH_TOKEN", "Refreshing after delay")
                            val success = handleRefresh(it)
                            if (success) {
                                if (isInitialRefresh) {
                                    _isRefreshDataSaved.emit(Unit)
                                    isInitialRefresh = false // So we don’t emit again
                                }
                            }
                        }
                    }
                } catch (ex: Exception) {
                    Log.e("REFRESH_TOKEN", "Exception in checkRefreshToken: ${ex.message}")
                    ex.printStackTrace()
                    if (isInitialRefresh) {
                        _isRefreshDataSaved.emit(Unit)
                        isInitialRefresh = false
                    }
                    break // break the loop to avoid infinite failures
                }
            }

            Log.e("REFRESH_TOKEN", "Refresh Job ended or canceled")
        }
    }

    // Helper function to handle refresh logic
    private suspend fun handleRefresh(refreshToken: String): Boolean {
        return try {
            val refreshResponse = networkRepository.refreshToken(refreshToken)
            if (refreshResponse.isSuccessful) {
                val newUserData = refreshResponse.body()?.data
                if (newUserData != null) {
                    val update = networkRepository.updateTokenSeason(
                        newToken = newUserData.token,
                        newRefreshToken = newUserData.refresh_token,
                        newExpireTime = newUserData.expire_date,
                        newTokenType = newUserData.token_type
                    )
                    Log.e("REFRESH_TOKEN", "updateTokenSeason result: $update")
                    return update
                }
            } else {
                //  Tracking 401 here
                if (refreshResponse.code() == 401) {
                    Log.e("REFRESH_TOKEN", "401 Unauthorized detected during token refresh")
                    networkRepository.clearSession()
                    _logoutEvent.emit(Unit)
                } else {
                    Log.e("REFRESH_TOKEN", "Refresh API failed: ${refreshResponse.code()}")
                }
            }
            false
        } catch (e: Exception) {
            Log.e("REFRESH_TOKEN", "Exception in handleRefresh: ${e.message}")
            false
        }
    }


    override fun onCleared() {
        super.onCleared()
        cancelRefreshJob()
    }


}