package com.faithForward.media.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.R
import com.faithForward.media.ui.navigation.Routes
import com.faithForward.media.ui.navigation.sidebar.SideBarEvent
import com.faithForward.media.ui.navigation.sidebar.SideBarItem
import com.faithForward.media.ui.navigation.sidebar.SideBarState
import com.faithForward.media.util.Util.isTvDevice
import com.faithForward.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SideBarViewModel @Inject constructor(
    private val application: Application,
    val networkRepository: NetworkRepository,
) : AndroidViewModel(application) {

    var sideBarItems = mutableStateListOf<SideBarItem>()
        private set

    private
    val _sideBarState = mutableStateOf(SideBarState())
    val sideBarState: State<SideBarState> = _sideBarState

    private val _logoutEvent = MutableSharedFlow<Unit>(replay = 0)
    val logoutEvent = _logoutEvent.asSharedFlow()


    init {
        val isTv = application.applicationContext.isTvDevice()
        sideBarItems.addAll(
            listOfNotNull(
                if (isTv) SideBarItem(
                    "Search",
                    R.drawable.search_ic,
                    Routes.Search.route
                ) else null,
                SideBarItem("Home", R.drawable.home_ic, Routes.Home.route),
                SideBarItem("MyList", R.drawable.plus_ic, Routes.MyList.route),
                SideBarItem("Creators", R.drawable.group_person_ic, Routes.Creator.route),
                SideBarItem("Series", R.drawable.screen_ic, Routes.Series.route),
                SideBarItem("Movies", R.drawable.film_ic, Routes.Movies.route),
//                SideBarItem("Tithe", R.drawable.fi_rs_hand_holding_heart, "tithe"),
                SideBarItem(
                    if (isTv) {
                        "My Account"
                    } else "Me",
                    R.drawable.baseline_expand_less_24,
                    Routes.MyAccount.route
                ),
                if (isTv) SideBarItem(
                    "Log Out",
                    R.drawable.baseline_expand_less_24,
                    "log_out"
                ) else null,
            )
        )
    }

    fun onEvent(event: SideBarEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is SideBarEvent.ChangeFocusState -> {
                        Log.e(
                            "FOCUSED_INDEX",
                            "chnage focused index called with ${event.isFocusable}"
                        )
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
                        Log.e("LOGOUT_COLLECT", "logout is success with ${response.message()}")
                        networkRepository.clearSession()
                        _logoutEvent.emit(Unit)
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    Log.e("LOGOUT_COLLECT", "logout exception is ${ex.message}")
                }
            }
        }
    }
}