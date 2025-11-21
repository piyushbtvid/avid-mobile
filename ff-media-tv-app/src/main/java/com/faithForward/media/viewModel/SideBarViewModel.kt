package com.faithForward.media.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faithForward.media.R
import com.faithForward.media.ui.navigation.Routes
import com.faithForward.media.ui.navigation.sidebar.SideBarEvent
import com.faithForward.media.ui.navigation.sidebar.SideBarItem
import com.faithForward.media.ui.navigation.sidebar.SideBarState
import com.faithForward.preferences.ConfigManager
import com.faithForward.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _logoutEvent = MutableSharedFlow<Unit>(replay = 0)
    val logoutEvent = _logoutEvent.asSharedFlow()


    init {
        buildSideBarItems()
    }

    /**
     * Rebuild sidebar items based on current config.
     * Call this after config is loaded to ensure items are built correctly.
     */
    fun rebuildSideBarItems() {
        buildSideBarItems()
    }

    /**
     * Build sidebar items based on config.
     * If login/qrlogin is disabled, exclude MyList, MyAccount, and Logout items.
     */
    private fun buildSideBarItems() {
        val configData = ConfigManager.getConfigData()
        val isLoginEnabled = configData?.enable_login == true
        val isQrLoginEnabled = configData?.enable_qrlogin == true
        // Show user items if at least one login method is enabled
        // Hide user items only if BOTH are disabled
        val shouldShowUserItems = isLoginEnabled || isQrLoginEnabled

        val items = mutableListOf<SideBarItem>()

        items.add(SideBarItem("Search", R.drawable.search_ic, Routes.Search.route))
        items.add(SideBarItem("Home", R.drawable.home_ic, Routes.Home.route))

        if (shouldShowUserItems) {
            items.add(SideBarItem("MyList", R.drawable.plus_ic, Routes.MyList.route))
        }

        // Only add Creators item if enable_creator is true
        if (configData?.enable_creator == true) {
            items.add(SideBarItem("Creators", R.drawable.group_person_ic, Routes.Creator.route))
        }

        items.add(SideBarItem("Series", R.drawable.screen_ic, Routes.Series.route))
        items.add(SideBarItem("Movies", R.drawable.film_ic, Routes.Movies.route))

        if (shouldShowUserItems) {
            items.add(
                SideBarItem(
                    "My Account",
                    R.drawable.baseline_expand_less_24,
                    Routes.MyAccount.route
                )
            )
            items.add(SideBarItem("Log Out", R.drawable.baseline_expand_less_24, "log_out"))
        }

        sideBarItems.clear()
        sideBarItems.addAll(items)
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