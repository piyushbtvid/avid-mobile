package com.faithForward.media.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.R
import com.faithForward.media.navigation.Routes
import com.faithForward.media.sidebar.SideBarEvent
import com.faithForward.media.sidebar.SideBarItem
import com.faithForward.media.sidebar.SideBarState
import kotlinx.coroutines.launch

class SideBarViewModel : ViewModel() {

    var sideBarItems = mutableStateListOf<SideBarItem>()
        private set

    private val _sideBarState = mutableStateOf(SideBarState())
    val sideBarState: State<SideBarState> = _sideBarState


    init {
        sideBarItems.addAll(
            listOf(
                SideBarItem("Search", R.drawable.search_ic, Routes.Search.route),
                SideBarItem("Home", R.drawable.home_ic, Routes.Home.route),
                SideBarItem("MyList", R.drawable.plus_ic, Routes.MyList.route),
                SideBarItem("Creators", R.drawable.group_person_ic, Routes.Creator.route),
                SideBarItem("Series", R.drawable.screen_ic, Routes.Series.route),
                SideBarItem("Movies", R.drawable.film_ic, Routes.Movies.route),
                SideBarItem("Tithe", R.drawable.fi_rs_hand_holding_heart, "tithe"),
            )
        )
    }

    fun onEvent(event: SideBarEvent) {
        viewModelScope.launch {
            when (event) {
                is SideBarEvent.ChangeFocusState -> {
                    _sideBarState.value = _sideBarState.value.copy(
                        isSideBarFocusable = event.isFocusable
                    )
                }

                is SideBarEvent.ChangeFocusedIndex -> {
                    _sideBarState.value = _sideBarState.value.copy(
                        sideBarFocusedIndex = event.index
                    )
                }

                is SideBarEvent.ChangeSelectedIndex -> {
                    _sideBarState.value = _sideBarState.value.copy(
                        sideBarSelectedPosition = event.index
                    )
                }
            }
        }
    }
}