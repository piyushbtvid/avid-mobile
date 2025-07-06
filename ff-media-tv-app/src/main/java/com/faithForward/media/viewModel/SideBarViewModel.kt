package com.faithForward.media.viewModel

import android.util.Log
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
import com.faithForward.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SideBarViewModel @Inject constructor(val networkRepository: NetworkRepository) : ViewModel() {

    var sideBarItems = mutableStateListOf<SideBarItem>()
        private set

    private
    val _sideBarState = mutableStateOf(SideBarState())
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
                SideBarItem("My Account", R.drawable.baseline_expand_less_24, "my_account"),
                SideBarItem("Log Out", R.drawable.baseline_expand_less_24, "log_out"),
            )
        )
    }

    fun onEvent(event: SideBarEvent) {
        viewModelScope.launch {
            try {
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

                    is SideBarEvent.LogoutClick -> {
                        logout()
                    }
                }
            } catch (ex: Exception) {
                Log.e("LOG", "${ex.printStackTrace()}")
            }

        }
    }

    private fun logout() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val response = networkRepository.logoutUser()
                    if (response.isSuccessful) {
                        Log.e("Logout", "logout is success with ${response.message()}")
                        networkRepository.clearSession()
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    Log.e("Logout", "logout exception is ${ex.message}")
                }
            }
        }
    }
}