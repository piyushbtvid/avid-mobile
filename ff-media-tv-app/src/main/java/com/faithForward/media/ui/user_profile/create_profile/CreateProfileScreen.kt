package com.faithForward.media.ui.user_profile.create_profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithForward.media.ui.commanComponents.CategoryCompose
import com.faithForward.media.ui.commanComponents.CategoryComposeDto
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.sections.search.custom_keyboard.KeyboardMode
import com.faithForward.media.ui.sections.search.custom_keyboard.NewKeyboardActionState
import com.faithForward.media.ui.theme.detailNowTextStyle
import com.faithForward.media.ui.theme.detailNowUnFocusTextStyle
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.pageBlackBackgroundColor
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.ui.user_profile.comman.AvatarItem
import com.faithForward.media.ui.user_profile.comman.CustomKeyBoard
import com.faithForward.media.ui.user_profile.comman.SelectAvatarRow
import com.faithForward.media.util.FocusState
import com.faithForward.media.viewModel.ProfileScreenViewModel
import com.faithForward.media.viewModel.uiModels.ProfileEvent
import com.faithForward.util.Resource
import kotlinx.coroutines.delay

@Composable
fun CreateProfileScreen(
    modifier: Modifier = Modifier,
    profileScreenViewModel: ProfileScreenViewModel,
) {

    LaunchedEffect(Unit) {
        profileScreenViewModel.onEvent(ProfileEvent.GetAllAvatars)
    }

    val uiEvent by profileScreenViewModel.uiEvent.collectAsStateWithLifecycle(null)

    val context =  LocalContext.current

    val allAvatarsResponse by profileScreenViewModel.allAvatars.collectAsState()

    if (allAvatarsResponse is Resource.Unspecified || allAvatarsResponse is Resource.Error) {
        return
    }

    val allAvatarsList = allAvatarsResponse.data ?: emptyList()

    var currentKeyboardMode by remember { mutableStateOf(KeyboardMode.ALPHABET) }

    var isSubmitFocused by remember { mutableStateOf(false) }

    var textFieldValue by remember { mutableStateOf("") }

    var currentKeyBoardAlphabetSize by remember { mutableStateOf(NewKeyboardActionState.large) }

    var selectedAvatarId by remember { mutableStateOf(-1) }

    // Showing Toast when uiEvent changes
    LaunchedEffect(uiEvent) {
        uiEvent?.let { event ->
            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            delay(200)
            profileScreenViewModel.resetUiEvent()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(pageBlackBackgroundColor),
        contentAlignment = Alignment.Center
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TitleText(
                text = "Create Profile",
                color = whiteMain,
                fontWeight = FontWeight.ExtraBold,
                textSize = 26,
                lineHeight = 26
            )


            SelectAvatarRow(
                avatarList = allAvatarsList,
                onSelectProfileClick = { avatarId ->
                    selectedAvatarId = avatarId
                }
            )

            // tex field
            Box(
                modifier = Modifier
                    .width(280.dp)
                    .height(34.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.35f), // adjust background as needed
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 5.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = if (textFieldValue.isNotEmpty()) textFieldValue else "Enter profile name",
                    color = if (textFieldValue.isNotEmpty()) Color.White else Color.White.copy(alpha = 0.5f),
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }


            CustomKeyBoard(
                onKeyClick = { string ->
                    textFieldValue += string
                },
                currentKeyboardMode = currentKeyboardMode,
                onCurrentKeyBoardModeChange = { keyboardMode ->
                    currentKeyboardMode = keyboardMode
                },
                keyboardActionState = currentKeyBoardAlphabetSize,
                onKeyBoardActionButtonClick = { state ->

                    when (state) {
                        NewKeyboardActionState.space -> {
                            textFieldValue += " "
                        }

                        NewKeyboardActionState.clear -> {
                            if (textFieldValue.isNotEmpty()) {
                                textFieldValue = textFieldValue.dropLast(1)
                            }
                        }

                        NewKeyboardActionState.number -> {
                            // switch to number keyboard layout
                            currentKeyboardMode = KeyboardMode.NUMBER
                        }

                        NewKeyboardActionState.alphabet -> {
                            // switch to alphabet keyboard layout
                            currentKeyboardMode = KeyboardMode.ALPHABET
                        }

                        NewKeyboardActionState.clearAll -> {
                            textFieldValue = ""
                        }

                        NewKeyboardActionState.small -> {
                            Log.e("ON_UP", "on up click with small")
                            currentKeyBoardAlphabetSize = NewKeyboardActionState.small
                        }

                        NewKeyboardActionState.large -> {
                            Log.e("ON_UP", "on up click with large")
                            currentKeyBoardAlphabetSize = NewKeyboardActionState.large
                        }
                    }

                }
            )


            CategoryCompose(modifier = Modifier
                .onFocusChanged {
                    isSubmitFocused = it.hasFocus
                }
                .focusable(),
                categoryComposeDto = CategoryComposeDto(btnText = "Submit", id = ""),
                backgroundFocusedColor = focusedMainColor,
                textFocusedStyle = detailNowTextStyle,
                backgroundUnFocusedColor = Color.White.copy(alpha = 0.35f),
                textUnFocusedStyle = detailNowUnFocusTextStyle,
                onCategoryItemClick = { id ->
                    profileScreenViewModel.onEvent(
                        ProfileEvent.CreateProfile(
                            name = textFieldValue,
                            avatarId = selectedAvatarId
                        )
                    )
                },
                focusState = if (isSubmitFocused) FocusState.FOCUSED else FocusState.UNFOCUSED
            )

        }


    }


}


@Preview(device = "id:tv_1080p")
@Composable
private fun CreateProfileScreenPrevew() {

    val avatarItem = AvatarItem(
        id = 0,
        imgSrc = ""
    )

    val list = listOf(
        avatarItem, avatarItem,
        avatarItem, avatarItem, avatarItem, avatarItem
    )

//    CreateProfileScreen(
//        allAvatarsList = list
//    )

}