package com.faithForward.media.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.R
import com.faithForward.media.navigation.Routes
import com.faithForward.media.sidebar.SideBarItem
import kotlinx.coroutines.launch

class SideBarViewModel : ViewModel() {

    var sideBarItems = mutableStateListOf<SideBarItem>()
        private set

    var isSideBarFocusable by mutableStateOf(false)
        private set

    init {
        sideBarItems.addAll(
            listOf(
                SideBarItem("Search", R.drawable.search_ic, "search"),
                SideBarItem("Home", R.drawable.home_ic, Routes.Home.route),
                SideBarItem("MyList", R.drawable.plus_ic, "myList"),
                SideBarItem("Creators", R.drawable.group_person_ic, Routes.Creator.route),
                SideBarItem("Series", R.drawable.screen_ic, "series"),
                SideBarItem("Movies", R.drawable.film_ic, "movie"),
                SideBarItem("Tithe", R.drawable.fi_rs_hand_holding_heart, "tithe"),
            )
        )
    }

    fun changeSideBarFocusState(boolean: Boolean) {
        viewModelScope.launch {
            isSideBarFocusable = boolean
        }
    }

}