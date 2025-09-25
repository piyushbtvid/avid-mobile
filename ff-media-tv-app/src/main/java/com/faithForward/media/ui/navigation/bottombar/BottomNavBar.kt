package com.faithForward.media.ui.navigation.bottombar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.faithForward.media.ui.navigation.sidebar.SideBarItem
import com.faithForward.media.ui.sections.my_account.profile_menu.InitialNameItem
import com.faithForward.media.ui.sections.my_account.profile_menu.UserInfoItemDto


@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    navItems: List<SideBarItem>,
    selectedPosition: Int,
    onSelectedPositionChange: (Int) -> Unit,
    onItemClick: (SideBarItem) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Color.Black.copy(alpha = 0.8f),
                RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEachIndexed { index, item ->
                BottomNavItemMobile(
                    modifier = Modifier.clickable {
                        onSelectedPositionChange(index)
                        onItemClick(item)
                    },
                    txt = item.name,
                    img = item.img,
                    isSelected = index == selectedPosition
                )
            }
        }
    }
}

@Composable
fun BottomNavItemMobile(
    modifier: Modifier,
    txt: String,
    img: Int?,
    isSelected: Boolean,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (img != null) {
            Image(
                painter = painterResource(id = img),
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                colorFilter = ColorFilter.tint(
                    if (isSelected) Color.Green else Color.Gray
                )
            )
        } else {
            // Account tab: show user initials sized to match icon height
            InitialNameItem(name = "ME", size = 22.dp, textSize = 10.sp)
        }
        Text(
            text = txt,
            fontSize = 12.sp,
            color = if (isSelected) Color.Green else Color.Gray
        )
    }
}


@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    val items = listOf(
        SideBarItem("Home", android.R.drawable.ic_menu_view, "home"),
        SideBarItem("Search", android.R.drawable.ic_menu_search, "search"),
        SideBarItem("Profile", android.R.drawable.ic_menu_myplaces, "profile"),
        SideBarItem("Settings", android.R.drawable.ic_menu_preferences, "settings")
    )

    var selectedIndex by remember { mutableStateOf(0) }

    BottomNavBar(
        navItems = items,
        selectedPosition = selectedIndex,
        onSelectedPositionChange = { selectedIndex = it },
        onItemClick = { clickedItem ->
            println("Clicked: ${clickedItem.tag}") // only for preview logging
        }
    )
}