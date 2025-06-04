package com.faithForward.media.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.player.PlayerDto
import com.faithForward.media.viewModel.uiModels.PlayerEvent
import com.faithForward.media.viewModel.uiModels.PlayerState
import com.faithForward.media.viewModel.uiModels.toVideoPlayerDto
import com.faithForward.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel : ViewModel() {
    private val _state = MutableStateFlow(PlayerState())
    val state: StateFlow<PlayerState> = _state.asStateFlow()

    private var autoHideJob: Job? = null

    fun handleEvent(event: PlayerEvent) {
        when (event) {
            is PlayerEvent.UpdateDuration -> {
                _state.value = _state.value.copy(duration = event.value)
            }
            is PlayerEvent.UpdateCurrentPosition -> {
                _state.value = _state.value.copy(currentPosition = event.value)
            }
            is PlayerEvent.ShowControls -> {
                _state.value = _state.value.copy(isControlsVisible = true)
                startAutoHideTimer()
            }
            is PlayerEvent.HideControls -> {
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
            delay(10000)
            _state.value = _state.value.copy(isControlsVisible = false)
        }
    }
}