package com.faithForward.media.sidebar

data class SideBarState(
    val isSideBarFocusable: Boolean = false,
    val sideBarFocusedIndex: Int = -1,
    val sideBarSelectedPosition: Int = -1
)