package com.faithForward.media.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.commanComponents.PosterCardDto
import com.faithForward.media.player.PlayerDto
import com.faithForward.media.player.VideoPlayerDto
import com.faithForward.media.viewModel.uiModels.toVideoPlayerDto
import com.faithForward.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel : ViewModel() {

    var currentPosition by mutableLongStateOf(0L)
        private set

    var duration by mutableLongStateOf(1L)
        private set

    private val _videoPlayerDto: MutableStateFlow<Resource<PlayerDto>> =
        MutableStateFlow(Resource.Unspecified())
    val videoPlayerDto = _videoPlayerDto.asStateFlow()

    private val _isControlsVisible = MutableStateFlow(true)
    val isControlsVisible: StateFlow<Boolean> = _isControlsVisible.asStateFlow()

    private var autoHideJob: Job? = null

    var is_Playing by mutableStateOf(false)
        private set

    fun handleDuration(value: Long) {
        duration = value
    }

    fun handleCurrentPosition(value: Long) {
        currentPosition = value
    }

    fun showControls() {
        _isControlsVisible.value = true
        startAutoHideTimer() // Restart hide timer when controls are shown
    }

    fun hideControls() {
        _isControlsVisible.value = false
    }

    private fun startAutoHideTimer() {
        autoHideJob?.cancel() // Cancel any previous timers
        autoHideJob = viewModelScope.launch {
            delay(10000) // Auto-hide after 10 seconds
            _isControlsVisible.value = false
        }
    }

    fun handleIsPlaying(boolean: Boolean) {
        is_Playing = boolean
    }

    fun updateVideoPlayerDto(itemList: List<PosterCardDto>) {
        viewModelScope.launch {
            val videoPlayerDtoList = itemList.map {
                it.toVideoPlayerDto()
            }
            _videoPlayerDto.emit(
                Resource.Success(
                    PlayerDto(
                        videoPlayerDtoList = videoPlayerDtoList
                    )
                )
            )
        }
    }

}