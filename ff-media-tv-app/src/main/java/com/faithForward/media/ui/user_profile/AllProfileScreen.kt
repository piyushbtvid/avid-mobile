package com.faithForward.media.ui.user_profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.ui.commanComponents.CategoryCompose
import com.faithForward.media.ui.commanComponents.CategoryComposeDto
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.detailNowTextStyle
import com.faithForward.media.ui.theme.detailNowUnFocusTextStyle
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.pageBlackBackgroundColor
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.util.FocusState
import com.faithForward.media.viewModel.ProfileScreenViewModel
import com.faithForward.media.viewModel.uiModels.ProfileEvent
import com.faithForward.util.Resource

@Composable
fun AllProfileScreen(
    modifier: Modifier = Modifier,
    profileScreenViewModel: ProfileScreenViewModel,
    onAddProfileClick: () -> Unit,
    onManageProfileClick: () -> Unit,
    onSetProfileSuccess: () -> Unit,
) {


    LaunchedEffect(Unit) {
        profileScreenViewModel.onEvent(ProfileEvent.GetAllProfiles)
    }

    val context = LocalContext.current

    val userProfileResponse by profileScreenViewModel.allProfiles.collectAsState()

    val uiEvent by profileScreenViewModel.uiEvent.collectAsStateWithLifecycle(null)


    if (userProfileResponse is Resource.Unspecified || userProfileResponse is Resource.Error || userProfileResponse is Resource.Loading) {
        return
    }

    var isManageFocused by remember { mutableStateOf(false) }

    LaunchedEffect(uiEvent) {
        uiEvent?.let { event ->
           // Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            onSetProfileSuccess.invoke()
        }
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(pageBlackBackgroundColor),
        contentAlignment = Alignment.Center
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TitleText(
                text = "Who's Watching?",
                color = whiteMain,
                fontWeight = FontWeight.ExtraBold,
                textSize = 26,
                lineHeight = 26
            )
            if (userProfileResponse.data != null) {
                AllProfileScreenRow(
                    userProfileList = userProfileResponse.data!!,
                    shouldShowAddProfileButton = true,
                    onAddProfileClick = onAddProfileClick,
                    onItemClick = { item ->
                        profileScreenViewModel.onEvent(
                            ProfileEvent.SetProfile(
                                item.id
                            )
                        )
                    }
                )
            }


            Spacer(modifier = Modifier.height(30.dp))

            CategoryCompose(modifier = Modifier
                .onFocusChanged {
                    isManageFocused = it.hasFocus
                }
                .focusable(),
                categoryComposeDto = CategoryComposeDto(btnText = "Profile", id = ""),
                backgroundFocusedColor = focusedMainColor,
                textFocusedStyle = detailNowTextStyle,
                backgroundUnFocusedColor = Color.White.copy(alpha = 0.35f),
                textUnFocusedStyle = detailNowUnFocusTextStyle,
                onCategoryItemClick = { id ->
                    onManageProfileClick.invoke()
                },
                focusState = if (isManageFocused) FocusState.FOCUSED else FocusState.UNFOCUSED
            )

        }
    }


}