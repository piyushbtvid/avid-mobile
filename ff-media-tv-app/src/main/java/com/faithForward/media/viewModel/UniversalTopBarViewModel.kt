package com.faithForward.media.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.faithForward.media.ui.top_bar.TopBarItemDto
import com.faithForward.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UniversalTopBarViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
)  : ViewModel(){

  var topBarItems = mutableStateListOf<TopBarItemDto>()
      private  set

    init {
        topBarItems.addAll(
            listOf(
                TopBarItemDto(
                    name = "STREAM", tag = "stream"
                ),
                TopBarItemDto(
                    name = "LIVE", tag = "live"
                ),
                TopBarItemDto(
                    name = "BROWSE", tag = "browse"
                )
            )
        )

    }


}