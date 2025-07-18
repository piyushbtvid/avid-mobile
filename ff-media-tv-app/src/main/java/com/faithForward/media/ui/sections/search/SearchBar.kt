package com.faithForward.media.ui.sections.search

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.theme.SearchBasicTextFieldFocusedTextStyle
import com.faithForward.media.ui.theme.SearchBasicTextFieldUnFocusedTextStyle
import com.faithForward.media.ui.theme.SearchHereStyle
import com.faithForward.media.ui.theme.gray
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    isSearchBarFocused: Boolean = false,
    searchQuery: String,
    searchBarFocusRequester: FocusRequester,
    keyboardController: SoftwareKeyboardController?,
    onSearchBarFocusChange: (Boolean) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
) {

    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    Row(modifier = Modifier
        .fillMaxWidth()
        .focusRequester(searchBarFocusRequester)
        .onFocusChanged {
            //   isBoxFocused = it.isFocused
            onSearchBarFocusChange.invoke(it.isFocused)
            if (it.isFocused) {
                //        onIndexChange.invoke(-1)
            }
        }
        .focusable()
        .border(
            1.dp,
            if (isSearchBarFocused) Color.White.copy(alpha = 0.7f) else Color.White.copy(
                alpha = 0.5f
            )
        )
        .background(if (isSearchBarFocused) gray else Color.Transparent)
        .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically) {
        BasicTextField(
            value = searchQuery,
            onValueChange = { value ->
                Log.e("VALUE", "VALUE SIZE IS ${value.length}")
                onSearchQueryChange.invoke(value)
            },
            modifier = Modifier
                .weight(1f)
                .height(26.dp),
            textStyle = if (isSearchBarFocused) SearchBasicTextFieldFocusedTextStyle else SearchBasicTextFieldUnFocusedTextStyle,
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrectEnabled = false, imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                Log.e("SEARCH", "search enter is called")
                keyboardController?.hide()
                scope.launch {
                    delay(200)
                    onSearchClick.invoke()
                }
            }),
            cursorBrush = SolidColor(Color.White),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    TextFieldDefaults.DecorationBox(
                        value = searchQuery,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource, // Note: fixed typo from 'intercationSource'
                        placeholder = {
                            Text(
                                "Search Here", style = SearchHereStyle
                            )
                        },
                        contentPadding = PaddingValues(0.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                }
            },
            singleLine = true
        )
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = if (isSearchBarFocused) Color.White else Color.White.copy(alpha = 0.5f),
            modifier = Modifier
                .padding(start = 8.dp)
                .size(20.dp)
        )
    }

}