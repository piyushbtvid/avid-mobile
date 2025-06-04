package com.faithForward.media.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.player.PlayerDto
import com.faithForward.media.viewModel.uiModels.PlayerEvent
import com.faithForward.media.viewModel.uiModels.PlayerPlayingState
import com.faithForward.media.viewModel.uiModels.PlayerState
import com.faithForward.media.viewModel.uiModels.toVideoPlayerDto
import com.faithForward.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel : ViewModel() {
    private val _state = MutableStateFlow(PlayerState())
    val state: StateFlow<PlayerState> = _state.asStateFlow()

    private var autoHideJob: Job? = null

    var playerState by mutableStateOf(PlayerPlayingState.IDLE)
        private set

    private var lastPlaybackState: PlayerPlayingState = PlayerPlayingState.PLAYING

    private val _interactionFlow = MutableSharedFlow<Unit>()
    val interactionFlow = _interactionFlow.asSharedFlow()

    fun handleEvent(event: PlayerEvent) {
        when (event) {
            is PlayerEvent.UpdateDuration -> {
                _state.value = _state.value.copy(duration = event.value)
            }

            is PlayerEvent.UpdateCurrentPosition -> {
                _state.value = _state.value.copy(currentPosition = event.value)
            }

            is PlayerEvent.ShowControls -> {
                Log.e("PLAYER", "Handling ShowControls event")
                _state.value = _state.value.copy(isControlsVisible = true)
                startAutoHideTimer()
            }

            is PlayerEvent.HideControls -> {
                Log.e("PLAYER", "Handling HideControls event")
                _state.value = _state.value.copy(isControlsVisible = false)
            }

            is PlayerEvent.UpdateIsPlaying -> {
                _state.value = _state.value.copy(isPlaying = event.isPlaying)
            }

            is PlayerEvent.UpdateVideoPlayerDto -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoading = true)
                    val videoPlayerDtoList = event.itemList.map {
                        it.toVideoPlayerDto()
                    }
                    _state.value = _state.value.copy(
                        videoPlayerDto = Resource.Success(
                            PlayerDto(videoPlayerDtoList = videoPlayerDtoList)
                        ),
                        isLoading = false
                    )
                }
            }

            is PlayerEvent.UpdatePlayerBuffering -> {
                _state.value = _state.value.copy(isPlayerBuffering = event.isBuffering)
            }
        }
    }

    private fun startAutoHideTimer() {
        autoHideJob?.cancel()
        autoHideJob = viewModelScope.launch {
            Log.e("PLAYER", "Starting auto-hide timer")
            delay(10000)
            Log.e("PLAYER", "Setting controls invisible after timer")
            _state.value = _state.value.copy(isControlsVisible = false)
        }
    }

    fun togglePlayPause() {
        playerState = if (lastPlaybackState == PlayerPlayingState.PLAYING) {
            lastPlaybackState = PlayerPlayingState.PAUSED
            PlayerPlayingState.PAUSED
        } else {
            lastPlaybackState = PlayerPlayingState.PLAYING
            PlayerPlayingState.PLAYING
        }
    }

    fun handlePlayerAction(action: PlayerPlayingState) {
        when (action) {
            PlayerPlayingState.PLAYING, PlayerPlayingState.PAUSED -> {
                lastPlaybackState = action  // Update last playback state
                playerState = action
            }

            PlayerPlayingState.REWINDING, PlayerPlayingState.FORWARDING -> {
                playerState = action

                // Reset to IDLE after a short delay
                viewModelScope.launch {
                    delay(200) // Adjust delay as needed
                    playerState = PlayerPlayingState.IDLE
                }
            }

            PlayerPlayingState.IDLE -> Unit // No action needed
        }
    }

    fun onUserInteraction() {
        viewModelScope.launch {
            Log.e("USER_INTERCTION", "on use interction is called  in viewModel")
            _interactionFlow.emit(Unit)
        }
    }
}