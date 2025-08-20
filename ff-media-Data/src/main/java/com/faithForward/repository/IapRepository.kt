package com.faithForward.repository

import com.amazon.device.iap.model.FulfillmentResult
import com.amazon.device.iap.model.Product
import com.amazon.device.iap.model.PurchaseResponse
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface IapRepository {
    val products: StateFlow<List<Product>>
    val purchaseResult: StateFlow<PurchaseResponse?>

    fun fetchProducts(skus: Set<String>)
    fun purchase(sku: String)
    fun notifyFulfillment(receiptId: String, result: FulfillmentResult) // 👈 new
}
