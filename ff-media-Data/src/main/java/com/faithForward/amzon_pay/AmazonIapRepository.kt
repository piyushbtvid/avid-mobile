package com.faithForward.amzon_pay

import android.content.Context
import android.util.Log
import com.amazon.device.iap.PurchasingListener
import com.amazon.device.iap.PurchasingService
import com.amazon.device.iap.model.FulfillmentResult
import com.amazon.device.iap.model.Product
import com.amazon.device.iap.model.ProductDataResponse
import com.amazon.device.iap.model.PurchaseResponse
import com.amazon.device.iap.model.PurchaseUpdatesResponse
import com.amazon.device.iap.model.UserDataResponse
import com.faithForward.repository.IapRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AmazonIapRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : IapRepository {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    override val products: StateFlow<List<Product>> = _products

    private val _purchaseResult = MutableStateFlow<PurchaseResponse?>(null)
    override val purchaseResult = _purchaseResult.asStateFlow()



    private val purchasingListener = object : PurchasingListener {
        override fun onUserDataResponse(response: UserDataResponse) {
            Log.d("IAP", "UserDataResponse: ${response.requestStatus}")
        }

        override fun onProductDataResponse(response: ProductDataResponse) {
            if (response.requestStatus == ProductDataResponse.RequestStatus.SUCCESSFUL) {
                _products.value = response.productData.values.toList()
            } else {
                Log.e("IAP", "Failed to fetch product data: ${response.requestStatus}")
            }
        }

        override fun onPurchaseResponse(response: PurchaseResponse) {
            _purchaseResult.tryEmit(response)
        }

        override fun onPurchaseUpdatesResponse(response: PurchaseUpdatesResponse) {
            Log.d("IAP", "Purchase updates: ${response.requestStatus}")
        }
    }

    init {
        // Register once when repository is created
        PurchasingService.registerListener(context, purchasingListener)
    }


    override fun fetchProducts(skus: Set<String>) {
        PurchasingService.getProductData(HashSet(skus))
    }

    override fun purchase(sku: String) {
        PurchasingService.purchase(sku)
    }

    override fun notifyFulfillment(receiptId: String, result: FulfillmentResult) {
        PurchasingService.notifyFulfillment(receiptId, result)
    }
}
