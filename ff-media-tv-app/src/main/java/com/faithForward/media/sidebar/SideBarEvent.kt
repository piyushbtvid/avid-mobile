package com.faithForward.media.sidebar

sealed class SideBarEvent {
    data class ChangeFocusState(val isFocusable: Boolean) : SideBarEvent()
    data class ChangeFocusedIndex(val index: Int) : SideBarEvent()
    data class ChangeSelectedIndex(val index: Int) : SideBarEvent()
}