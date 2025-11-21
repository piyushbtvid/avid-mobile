package com.faithForward.media.ui.subscription

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.faithForward.media.ui.commanComponents.TitleText
import com.faithForward.media.ui.theme.focusedMainColor
import com.faithForward.media.ui.theme.pageBlackBackgroundColor
import com.faithForward.media.ui.theme.whiteMain
import com.faithForward.media.viewModel.SubscriptionEvent
import com.faithForward.media.viewModel.SubscriptionViewModel

@Composable
fun SubscriptionScreen(
    modifier: Modifier = Modifier,
    subscriptionViewModel: SubscriptionViewModel,
    onNavigateToPlayer: () -> Unit,
    onNavigateBack: () -> Unit,
) {

    val context = LocalContext.current
    val buttonFocusRequester = remember { FocusRequester() }
    var buttonFocusedIndex by rememberSaveable { mutableIntStateOf(-1) }

    val products by subscriptionViewModel.products.collectAsState()
    val purchase by subscriptionViewModel.purchaseResult.collectAsState(null)

    var showLoader by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        subscriptionViewModel.loadProducts()
    }

    LaunchedEffect(Unit) {
        subscriptionViewModel.events.collect { event ->
            when (event) {
                is SubscriptionEvent.Success -> {
                    onNavigateToPlayer()
                }

                is SubscriptionEvent.Error -> {
                    showLoader = false
                  //  Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    //  onNavigateBack()
                    try {
                        Log.e("PLAN", "plan request focus ")
                        buttonFocusRequester.requestFocus()
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                SubscriptionEvent.Loading -> {
                    showLoader = true
                }
            }
        }
    }

    LaunchedEffect(products) {
        if (products.isNotEmpty()) {
            try {
                Log.e("PLAN", "plan request focus after products loaded")
                buttonFocusRequester.requestFocus()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    LaunchedEffect(purchase) {
        if (purchase != null) {
            Log.e("PURCHASE", "purchase called in launch effect with $purchase")
            subscriptionViewModel.handlePurchase(purchase!!)
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(pageBlackBackgroundColor)
    ) {
        if (!showLoader) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = 30.dp, bottom = 20.dp)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TitleText(
                    text = "Choose your premium plan",
                    textSize = 28,
                    lineHeight = 28,
                    color = whiteMain,
                    fontWeight = FontWeight.ExtraBold,
                )

                TitleText(
                    modifier = Modifier.width(500.dp),
                    text = "Unlock unlimited entertainment with premium features, exclusive content, and ad-free streaming",
                    textSize = 13,
                    lineHeight = 13,
                    maxLine = 2,
//                    textAlign = TextAlign.Center,
                    color = whiteMain.copy(alpha = 0.7f),
                    fontWeight = FontWeight.W400,
                )

                Spacer(modifier = Modifier.height(5.dp))

                LazyRow(
                    modifier = Modifier.wrapContentSize(),
                    horizontalArrangement = Arrangement.spacedBy(35.dp)
                ) {
                    itemsIndexed(products) { index, product ->

                        val isButtonFocused = index == buttonFocusedIndex


                        SubscriptionItem(
                            isButtonFocused = isButtonFocused,
                            modifier = Modifier
                                .focusRequester(focusRequester = if (index == 0) buttonFocusRequester else FocusRequester())
                                .onFocusChanged {
                                    buttonFocusedIndex = if (it.hasFocus) {
                                        index
                                    } else {
                                        -1
                                    }
                                }
                                .focusable(),
                            subscriptionUiItem = SubscriptionUiItem(
                                amount = product.price,                // from Product
                                time = if (index == 0) "/month" else "/year", // static
                                headLineText = product.title,          // from Product
                                featureTextList = if (index == 0) {
                                    listOf(
                                        "Ad-free streaming",
                                        "4K Ultra HD quality",
                                        "Download for offline viewing",
                                        "Stream on 4 devices",
                                        "Cancel anytime"
                                    )
                                } else {
                                    listOf(
                                        "Everything in Monthly Plan",
                                        "Exclusive premium content",
                                        "Early access to new releases",
                                        "Priority customer support",
                                        "Stream on 6 devices"
                                    )
                                },
                                subHeadLineText = product.description  // from Product
                            ),
                            buttonText = if (index == 0) "Choose Monthly" else "Choose Yearly",
                            onButtonClick = {
                                subscriptionViewModel.buy(product.sku)
                            }
                        )
                    }
                }
            }
        }

        if (showLoader) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = focusedMainColor
            )
        }
    }

}


@Preview(device = "id:tv_1080p")
@Composable
private fun SubscriptionUiPreview() {
    //SubscriptionScreen()
}