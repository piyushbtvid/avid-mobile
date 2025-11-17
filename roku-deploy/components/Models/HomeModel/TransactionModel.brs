function init()
    m.top.id = "TransactionModel"
  end function

  function parseData(data)
    if (isValid(data))
        m.top.amount = data.amount
        m.top.cancelled = data.cancelled
        m.top.cancelledTransactionIds = data.cancelledTransactionIds
        m.top.channelId = data.channelId
        m.top.channelName = data.channelName
        m.top.couponCode = data.couponCode
        m.top.currency = data.currency
        m.top.errorCode = data.errorCode
        m.top.errorDetails = data.errorDetails
        m.top.errorMessage = data.errorMessage
        m.top.expirationDate = data.expirationDate
        m.top.isEntitled = data.isEntitled
        m.top.originalPurchaseDate = data.originalPurchaseDate
        m.top.OriginalTransactionId = data.OriginalTransactionId
        m.top.partnerReferenceId = data.partnerReferenceId
        m.top.productId = data.productId
        m.top.productName = data.productName
        m.top.purchaseChannel = data.purchaseChannel
        m.top.purchaseContext = data.purchaseContext
        m.top.purchaseDate = data.purchaseDate
        m.top.purchaseStatus = data.purchaseStatus
        m.top.purchaseType = data.purchaseType
        m.top.quantity = data.quantity
        m.top.rokuCustomerId = data.rokuCustomerId
        m.top.status = data.status
        m.top.tax = data.tax
        m.top.total = data.total
        m.top.transactionId = data.transactionId
    end if
  end function