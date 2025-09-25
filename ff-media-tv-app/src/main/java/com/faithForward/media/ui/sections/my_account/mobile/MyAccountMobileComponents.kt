package com.faithForward.media.ui.sections.my_account.mobile

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.sections.common_ui.content.ContentRow
import com.faithForward.media.ui.sections.common_ui.content.PosterRowDto
import com.faithForward.media.ui.sections.my_account.WatchSectionUiModel
import com.faithForward.media.ui.sections.my_account.comman.WatchSectionItemDto
import com.faithForward.media.ui.sections.my_account.setting.Setting
import com.faithForward.media.ui.sections.my_account.setting.SettingDto
import com.faithForward.media.viewModel.uiModels.toPosterCardDto

@Composable
fun ContinueWatchingRowMobile(
    modifier: Modifier = Modifier,
    section: WatchSectionUiModel,
    onItemClick: (WatchSectionItemDto) -> Unit,
) {
    val posterList = (section.items ?: emptyList()).map { it.toPosterCardDto() }
    val focusRequesters = mutableMapOf<Pair<Int, Int>, androidx.compose.ui.focus.FocusRequester>()
    var lastFocusedItem by rememberSaveable { mutableStateOf(Pair(0, 0)) }

    ContentRow(
        modifier= modifier,
        posterRowDto = PosterRowDto(
            heading = section.title,
            rowId = "continue_watching",
            dtos = posterList
        ),
        onItemClick = { posterCard, _, _ ->
            val original = section.items?.firstOrNull { it.id == posterCard.id }
            if (original != null) onItemClick(original)
        },
        rowIndex = 0,
        focusRequesters = focusRequesters,
        onItemFocused = { newFocus -> lastFocusedItem = newFocus },
        lastFocusedItem = lastFocusedItem,
        listState = rememberLazyListState(),
        showContentOfCard = true,
        onChangeContentRowFocusedIndex = { }
    )
}

@Composable
fun MyListRowMobile(
    modifier: Modifier = Modifier,
    section: WatchSectionUiModel,
    onItemClick: (WatchSectionItemDto) -> Unit,
) {
    val posterList = (section.items ?: emptyList()).map { it.toPosterCardDto() }
    val focusRequesters = mutableMapOf<Pair<Int, Int>, androidx.compose.ui.focus.FocusRequester>()
    var lastFocusedItem by rememberSaveable { mutableStateOf(Pair(0, 0)) }

    ContentRow(
        posterRowDto = PosterRowDto(
            heading = section.title,
            rowId = "my_list",
            dtos = posterList
        ),
        onItemClick = { posterCard, _, _ ->
            val original = section.items?.firstOrNull { it.id == posterCard.id }
            if (original != null) onItemClick(original)
        },
        rowIndex = 1,
        focusRequesters = focusRequesters,
        onItemFocused = { newFocus -> lastFocusedItem = newFocus },
        lastFocusedItem = lastFocusedItem,
        listState = rememberLazyListState(),
        showContentOfCard = true,
        onChangeContentRowFocusedIndex = { }
    )
}

@Composable
fun AccountSettingsMobile(
    modifier: Modifier = Modifier,
    setting: SettingDto,
    onSwitchProfile: () -> Unit,
    onLogout: () -> Unit,
) {
    Column(
        modifier = modifier.padding(start = 25.dp, end = 16.dp, top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Setting(
            settingItemDto = setting,
            onSwitchProfile = onSwitchProfile,
            expandToFullHeight = false,
            centerSwitchProfile = true
        )
        
        // Logout button
        androidx.compose.material.Divider(
            color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.5f),
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .padding(vertical = 16.dp)
        )
        
        com.faithForward.media.ui.commanComponents.CategoryCompose(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .focusable(),
            categoryComposeDto = com.faithForward.media.ui.commanComponents.CategoryComposeDto(
                btnText = "Logout", 
                id = ""
            ),
            backgroundFocusedColor = com.faithForward.media.ui.theme.focusedMainColor,
            textFocusedStyle = com.faithForward.media.ui.theme.detailNowTextStyle,
            backgroundUnFocusedColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.35f),
            textUnFocusedStyle = com.faithForward.media.ui.theme.detailNowUnFocusTextStyle,
            onCategoryItemClick = { _ -> onLogout() },
            focusState = com.faithForward.media.util.FocusState.UNFOCUSED
        )
    }
}

// Previews

private fun sampleWatchSectionUiModel(title: String): WatchSectionUiModel {
    val items = listOf(
        WatchSectionItemDto(
            contentType = "Movie",
            id = "1",
            contentSlug = "slug-1",
            title = "Sample 1",
            description = "Desc 1",
            progress = 120,
            duration = 3600,
            timeLeft = "58 m left",
            image = "",
            seriesSlug = null
        ),
        WatchSectionItemDto(
            contentType = "Movie",
            id = "2",
            contentSlug = "slug-2",
            title = "Sample 2",
            description = "Desc 2",
            progress = 0,
            duration = 5400,
            timeLeft = "",
            image = "",
            seriesSlug = null
        )
    )
    return WatchSectionUiModel(title = title, items = items)
}

private fun sampleSetting(): SettingDto = SettingDto(
    email = "abc@gmail.com",
    passwordTxt = "Last Changes 3 months ago",
    phoneNumber = "00000011111",
    language = "English",
    autoPlay = "Play next episode automatically",
    notification = "Get notified about new releases",
    subtitles = "Show subtitles by default",
)

@Preview(showBackground = true)
@Composable
fun Preview_ContinueWatchingRowMobile() {
    ContinueWatchingRowMobile(
        section = sampleWatchSectionUiModel("Continue Watching"),
        onItemClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun Preview_MyListRowMobile() {
    MyListRowMobile(
        section = sampleWatchSectionUiModel("My List"),
        onItemClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun Preview_AccountSettingsMobile() {
    AccountSettingsMobile(
        setting = sampleSetting(),
        onSwitchProfile = {},
        onLogout = {}
    )
}


