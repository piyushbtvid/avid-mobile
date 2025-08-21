package com.faithForward.media.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazon.device.iap.model.FulfillmentResult
import com.amazon.device.iap.model.Product
import com.amazon.device.iap.model.PurchaseResponse
import com.faithForward.repository.IapRepository
import com.faithForward.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val iapRepository: IapRepository,
    private val networkRepository: NetworkRepository,
) : ViewModel() {

    private val skuList = setOf(
        "com.faithForward.media.subscription.monthly",
        "com.faithForward.media.subscription.yearly",
    )

    private val _products: StateFlow<List<Product>> = iapRepository.products
    val products: StateFlow<List<Product>> = _products
        .map { it.reversed() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    val purchaseResult: SharedFlow<PurchaseResponse?> = iapRepository.purchaseResult
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    fun loadProducts() {
        iapRepository.fetchProducts(skuList)
    }

    fun buy(sku: String) {
        iapRepository.purchase(sku)
    }

    fun handlePurchase(response: PurchaseResponse) {
        viewModelScope.launch {
            if (response.requestStatus == PurchaseResponse.RequestStatus.SUCCESSFUL) {
                val receiptId = response.receipt.receiptId
                val productId = response.receipt.sku

                try {
                    val result = networkRepository.setPurchase(receiptId, productId)
                    if (result.isSuccessful) {
                        Log.e("SUBSSCRIPTION_CHECK","set purchase sucess with $result")
                        iapRepository.notifyFulfillment(receiptId, FulfillmentResult.FULFILLED)
                    } else {
                        Log.e("SUBSSCRIPTION_CHECK","set purchase error with $result")
                      //  iapRepository.notifyFulfillment(receiptId, FulfillmentResult.UNAVAILABLE)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("SUBSSCRIPTION_CHECK", "Error sending purchase to backend", e)
                    iapRepository.notifyFulfillment(receiptId, FulfillmentResult.UNAVAILABLE)
                }
            } else {
                Log.e("IAP", "Purchase failed: ${response.requestStatus}")
            }
        }
    }
}
