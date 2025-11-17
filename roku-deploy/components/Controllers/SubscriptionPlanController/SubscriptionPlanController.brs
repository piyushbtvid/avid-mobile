function Init()
    m.top.id = "SubscriptionPlanController"

    m.constants = GetConstants()
    m.scene = getScene()
    m.http = httpClient()
    m.apiProvider = APIProvider(m.constants)
    m.localStorage = LocalStorage()

    m.store = m.top.findNode("store")
    m.itemBackFocus = m.top.findNode("itemBackFocus")

    m.premiumGrid = m.top.findNode("premiumGrid")
    m.premiumGrid.observeField("rowItemSelected", "onPremiumItemSelected")


    m.store.observeField("purchases", "onPurchasesReceived")
    m.store.observeField("orderStatus", "onOrderStatusChanged")

    initUIElements()
end function

sub initUIElements()
    m.topNav = m.scene.findNode("topNav")
    m.topNav.visible = false

    m.leftMenu = m.scene.findNode("leftMenu")
    m.leftMenu.visible = false

    m.premiumSubTitle = m.top.findNode("premiumSubTitle")
    m.premiumSubTitle.width = 700
    m.premiumSubTitle.translation = [(1920 - m.premiumSubTitle.width) / 2, 190]

end sub


function onNavigateTo(param as object)
    if isValid(param) and isValid(param.returnToVideo) and param.returnToVideo
        m.returnToVideo = true
        m.pendingVideoInfo = param.videoInfo
    end if
    runSubscriptionFlow()
end function

sub runSubscriptionFlow()
    startSpinner(m.top)
    m.store.command = "getCatalog"
    m.store.ObserveField("catalog", "OnCatalogResponse")
end sub


sub OnCatalogResponse(event as object)
    catalog = event.GetData()
    m.subscriptionPlans = catalog.getChildren(-1, 0)
    planResponse(m.subscriptionPlans)
end sub

sub planResponse(data as object)
    stopSpinner()
    if isValid(data)
        contentNode = CreateObject("roSGNode", "ContentNode")

        row = contentNode.createChild("ContentNode")

        for each plan in data

            item = row.createChild("SubscriptionPlanModel")
            item.setFields({
                id: plan.id
                title: plan.name
                price: plan.cost
                code: plan.code
            })

        end for

        m.premiumGrid.content = contentNode
        m.currentPlans = data

        if contentNode.getChildCount() > 0
            m.premiumGrid.jumpToRowItem = [0, 0]
        end if
    end if


    itemCount = row.getChildCount()

    rowWidth = (400 * itemCount) + (20 * (itemCount - 1))

    centerX = (1920 - rowWidth) / 2

    if itemCount <= 4
        m.premiumGrid.translation = [centerX, 300]
    else
        m.premiumGrid.translation = [130, 300]
    end if
    applyFocus(m.premiumGrid, true, "onNavigateTo() - SubscriptionPlanController.brs")
end sub

sub onPremiumItemSelected(event as object)

    selectedIndex = event.getData()
    rowIndex = selectedIndex[0]
    rowItemIndex = selectedIndex[1]

    row = m.premiumGrid.content.getChild(selectedIndex[0])
    rowItem = row.getChild(selectedIndex[1])


    order = CreateObject("roSGNode", "ContentNode")
    product = order.CreateChild("ContentNode")
    product.AddFields({ code: rowItem.code, name: rowItem.title, qty: 1 })

    m.store.order = order
    m.store.command = "doOrder"
    m.store.ObserveField("orderStatus", "OnOrderStatus")
end sub


sub OnOrderStatus(event as object)

    isPremiumUser = "false"
    orderStatus = event.GetData()

    m.orderConfirmation = orderStatus.getChildren(-1, 0)[0]
    if orderStatus <> invalid and orderStatus.status = 1
        loadPurchase(m.orderConfirmation)
        m.successDialog = CreateObject("roSGNode", "Dialog")
        m.successDialog.title = "Success"
        m.successDialog.message = "You are now a premium member."
        m.scene.dialog = m.successDialog
    end if
    m.store.UnobserveField("orderStatus")
end sub


function loadPurchase(bill)
    login_Token = LocalStorage().getValueForKey("login_Token")

    Data = {
        receipt_id: bill.purchaseId,
        platform: "roku"
    }
    getPurchase = TVService(m.apiProvider).getPurchase(login_Token, Data)
    getPurchase.httpResponse.observeField("response", "onPurchaseResponse")
    m.http.sendRequest(getPurchase)

end function

sub onPurchaseResponse(event)
    response = event.getData()

    if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
        userData = response.data.data.data
        m.scene.subscriptionPlanDetail = userData.subscription

        user_type = userData.user.user_type.toStr()
        m.scene.user_type = user_type
        m.scene.unloadController = true

       

        if m.returnToVideo
            m.successDialog.visible = false
            m.scene.dialog.close = true
            pageInfo = createPageInfo(m.constants.CONTROLLERS.VIDEO, {
                content: m.pendingVideoInfo,
                seasonData: invalid,
                relatedData: invalid
            })
            m.scene.loadController = pageInfo
        else
            goBackInHistory(m.scene)
        end if

    end if
end sub

function onKeyEvent(key as string, press as boolean) as boolean
    if not press return false

    if key = m.constants.REMOTE_KEYS.BACK
        goBackInHistory(m.scene)
        return true
    else if key = m.constants.REMOTE_KEYS.LEFT
        return true
    else if key = m.constants.REMOTE_KEYS.RIGHT
        return true
    else if key = m.constants.REMOTE_KEYS.UP
        return true
    end if

    return false
end function
