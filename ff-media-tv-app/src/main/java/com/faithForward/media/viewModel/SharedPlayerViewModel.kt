package com.faithForward.media.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.viewModel.uiModels.PlayerPlayingState
import com.faithForward.media.viewModel.uiModels.SharedPlayerEvent
import com.faithForward.media.viewModel.uiModels.SharedPlayerViewModelState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SharedPlayerViewModel() : ViewModel() {

    private val _state = MutableStateFlow(SharedPlayerViewModelState())
    val state: StateFlow<SharedPlayerViewModelState> = _state.asStateFlow()

    var playerState by mutableStateOf(PlayerPlayingState.IDLE)
        private set

    private var lastPlaybackState: PlayerPlayingState = PlayerPlayingState.PLAYING

    private val _interactionFlow = MutableSharedFlow<Unit>(replay = 0)
    val interactionFlow = _interactionFlow.asSharedFlow()

    private var autoHideJob: Job? = null

    fun handleEvent(event: SharedPlayerEvent) {

        when (event) {

            is SharedPlayerEvent.ShowControls -> {
                Log.e(
                    "IS_VISIBLE",
                    "show controls changed called in viewModel and isContolesVisible is true"
                )
                _state.value = _state.value.copy(isControlsVisible = true)
                startAutoHideTimer()
            }

            is SharedPlayerEvent.HideControls -> {
                _state.value = _state.value.copy(isControlsVisible = false)
            }

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

    fun onUserInteraction(from: String) {
        viewModelScope.launch {
            Log.e("PLAYER_UI", "on use interction is called  in viewModel from $from")
            _interactionFlow.emit(Unit)
        }
    }

    fun startAutoHideTimer() {
        Log.e("IS_VISIBLE", "Starting auto-hide timer function")
        autoHideJob?.cancel()
        autoHideJob = viewModelScope.launch {
            Log.e("IS_VISIBLE", "Starting auto-hide timer")
            delay(10000)
            Log.e("IS_VISIBLE", "Setting controls invisible after timer")
            _state.value = _state.value.copy(isControlsVisible = false)
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
            PlayerPlayingState.MUTE_UN_MUTE -> {}
        }
    }

}