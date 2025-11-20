function init()

    m.top.id = "AccountController"

    m.constants = GetConstants()
    m.scene = getScene()
    m.http = httpClient()
    m.apiProvider = APIProvider(m.constants)

    m.accountMenu = m.top.findNode("accountMenu")
    m.AccountMenuList = m.accountMenu.findNode("AccountMenuList")

    m.profileShortName = m.top.findNode("profileShortName")
    m.dataEmptyTitle = m.top.findNode("dataEmptyTitle")

    m.nameLabel = m.top.findNode("nameLabel")

    m.account_setting = m.top.findNode("account_setting")

    m.subscription_setting = m.top.findNode("subscription_setting")
    m.planLabel = m.top.findNode("planLabel")
    m.expiryLabel = m.top.findNode("expiryLabel")
    m.statusLabel = m.top.findNode("statusLabel")

    m.MarkUpList_Title = m.top.findNode("MarkUpList_Title")

    m.MarkUpList = m.top.findNode("MarkUpList")
    m.MarkUpList.observeField("ItemSelected", "onMarkUpListSelected")
    m.MarkUpList.observeField("ItemFocused", "onMarkUpListFocused")

    m.accountMenu.observeFieldScoped("leftNavEscape", "onleftNavFocusEscape")
    m.accountMenu.observeFieldScoped("backNavEscape", "onbackNavFocusEscape")
    m.accountMenu.observefield("indexSelected", "onmenuitemSelected")

    m.leftMenu = m.scene.findNode("leftMenu")
    m.leftMenu.visible = true

    m.TermsOfUse = m.top.findNode("TermsOfUse")
    m.PrivacyPolicy = m.top.findNode("PrivacyPolicy")
    m.CookiePolicy = m.top.findNode("CookiePolicy")

    m.switchProfile = m.top.findNode("switchProfile")
    m.switchProfile.imgSrc = { id: "switchProfiles", label: "Switch Profile", unFocusImg: "pkg://images/LoginButton.png", focusImg: "pkg://images/creatorDetailBtnFocus.png", width: 269, height: 76 }
    m.switchProfile.observeField("buttonSelected", "switchProfilesFun")

    m.subscriptionAndBrowseBtn = m.top.findNode("subscriptionAndBrowseBtn")
    m.subscriptionAndBrowseBtn.observeField("buttonSelected", "subscriptionAndBrowseBtnFun")
end function

sub onmenuitemSelected(event)
    response = event.getData()
    m.rowListIndex = invalid
    m.lastSelectedMenuItem = response
    if response = 0
        loadContinueWatchContent()
    else if response = 1
        loadMyListContent()
    else if response = 2
        subscriptionScreen()
    else if response = 3
        setting()
    end if
end sub

sub subscriptionScreen()
    clearMarkUpList()
    m.dataEmptyTitle.visible = false
    m.subscriptionPlanDetail = m.scene.subscriptionPlanDetail

    if m.subscriptionPlanDetail <> invalid
        m.planLabel.text = "Plan :   " + m.subscriptionPlanDetail.title
        m.expiryLabel.text = "Expires on :   " + m.subscriptionPlanDetail.expiration_date '+ formatDate(expiryDate)
        m.statusLabel.text = "Status :   " + m.subscriptionPlanDetail.status
        m.subscription_setting.visible = true

    else
        m.dataEmptyTitle.visible = true
        m.dataEmptyTitle.text = "You have no active subscription."
        centerAreaX = m.leftMenu.boundingRect().width + 350

        centerAreaWidth = 1920 - centerAreaX
        m.dataEmptyTitle.translation = [
            centerAreaX + (centerAreaWidth - m.dataEmptyTitle.boundingRect().width) / 2,
        (1080 - m.dataEmptyTitle.height) / 2]

        m.subscriptionAndBrowseBtn.imgSrc = { id: "subscribe", label: "Subscribe Now", unFocusImg: "pkg://images/LoginButton.png", focusImg: "pkg://images/creatorDetailBtnFocus.png", width: 269, height: 76 }
        m.subscriptionAndBrowseBtn.translation = [
            centerAreaX + (centerAreaWidth - m.subscriptionAndBrowseBtn.boundingRect().width) / 2,
        (1080 - m.subscriptionAndBrowseBtn.height + 200) / 2]
        m.subscriptionAndBrowseBtn.visible = true
    end if

end sub

function formatDate(dateStr as string) as string
    dt = CreateObject("roDateTime")
    dt.FromISO8601String(dateStr)
    return dt.AsDateString("dd MMM yyyy")
end function

sub setting()
    clearMarkUpList()
    m.dataEmptyTitle.visible = false
    m.account_setting.visible = true
end sub


function onNavigateTo(param as object)
    '?"param in AccountController----"param
    pageState = param.controllerState
    

    if isValid(pageState)
        m.selected_screen = pageState.selected_screen
        if isValid(m.selected_screen) 
            applyFocus(m.subscriptionAndBrowseBtn, true, "onNavigateTo() - AccountController.brs") 
        end if
        m.rowListIndex = pageState.rowListIndex
        m.lastSelectedMenuItem = pageState.lastSelectedMenuItem
        m.accountMenu.itemSelected = m.lastSelectedMenuItem
    end if

    if m.lastSelectedMenuItem = 1
        loadMyListContent()
    else if m.lastSelectedMenuItem = 2
        subscriptionScreen()
    else if m.lastSelectedMenuItem = 3
        setting()
    else
        loadContinueWatchContent()
    end if
end function


function onNavigateAway()
    m.top.controllerState = { rowListIndex: m.selectedGridId, lastSelectedMenuItem: m.lastSelectedMenuItem , selected_screen:true}
end function

function loadContinueWatchContent()
    clearMarkUpList()
    startSpinner(m.top)
    login_Token = LocalStorage().getValueForKey("login_Token")
    continueWatchList = TVService(m.apiProvider).getContinueWatchListData(login_Token)
    continueWatchList.httpResponse.observeField("response", "handleContinueWatchResponse")
    m.http.sendRequest(continueWatchList)
end function


function handleContinueWatchResponse(event)
    stopSpinner()
    response = event.getData()
    m.dataEmptyTitle.visible = false
    if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
        continueWatchData = response.data.data.data
        if continueWatchData = invalid or continueWatchData.count() = 0
            m.dataEmptyTitle.visible = true
            m.dataEmptyTitle.text = "No Shows in progress. Explore our library to find your next binge!."

            centerAreaX = 507

            centerAreaWidth = 1920 - centerAreaX
            
            m.dataEmptyTitle.translation = [
                centerAreaX + (centerAreaWidth - m.dataEmptyTitle.boundingRect().width) / 2,
                (1080 - m.dataEmptyTitle.height) / 2
            ]

            m.subscriptionAndBrowseBtn.imgSrc = { id: "subscribe", label: "Browse", unFocusImg: "pkg://images/LoginButton.png", focusImg: "pkg://images/creatorDetailBtnFocus.png", width: 269, height: 76 }
            m.subscriptionAndBrowseBtn.translation = [
                centerAreaX + (centerAreaWidth - m.subscriptionAndBrowseBtn.boundingRect().width) / 2,
            (1080 - m.subscriptionAndBrowseBtn.height + 200) / 2]
            m.subscriptionAndBrowseBtn.visible = true
        end if
        feedListContent = createObject("RoSGNode", "ContentNode")

        'feedListContent.addFields({row_type: "continue"})

        feedListData = []
        for each Item in continueWatchData
            if Item.content_type <> "Live Channel"
                itemNode = CreateObject("roSGNode", "feedListItemDataModel")
                itemNode.callFunc("parseData", Item)
                itemNode.row_type = "continue"
                feedListData.push(itemNode)
            end if
        end for
        feedListContent.appendChildren(feedListData)
        m.MarkUpList.content = feedListContent
        m.MarkUpList.visible = true
        m.MarkUpList_Title.text = "Continue Watching"

        if isValid(m.rowListIndex)
            m.MarkUpList.jumpToItem = m.rowListIndex
            applyFocus(m.MarkUpList, true, "onNavigateTo() - AccountController.brs")
        else
            applyFocus(m.AccountMenuList, true, "onNavigateTo() - AccountController.brs")
        end if
    end if
end function

sub clearMarkUpList()
    m.MarkUpList_Title.text = ""
    if m.MarkUpList <> invalid
        m.MarkUpList.visible = false
        count = m.MarkUpList.getChildCount()
        m.MarkUpList.removeChildrenIndex(count, 0)
    end if
    if m.account_setting <> invalid
        m.account_setting.visible = false
    end if
    if m.subscription_setting <> invalid
        m.subscription_setting.visible = false
        m.subscriptionAndBrowseBtn.visible = false
    end if
end sub

function loadMyListContent()
    clearMarkUpList()
    startSpinner(m.top)
    login_Token = LocalStorage().getValueForKey("login_Token")
    getMyListApi = TVService(m.apiProvider).getMyListData(login_Token)
    getMyListApi.httpResponse.observeField("response", "handleMyListResponse")
    m.http.sendRequest(getMyListApi)
end function


function handleMyListResponse(event)
    stopSpinner()
    response = event.getData()
    m.dataEmptyTitle.visible = false
    if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
        myListData = response.data.data.data
        if myListData = invalid or myListData.count() = 0
            m.dataEmptyTitle.visible = true
            m.dataEmptyTitle.text = "Start building your watchlist! Explore shows now."
            centerAreaX = m.leftMenu.boundingRect().width + 350

            centerAreaWidth = 1920 - centerAreaX
            m.dataEmptyTitle.translation = [
                centerAreaX + (centerAreaWidth - m.dataEmptyTitle.boundingRect().width) / 2,
                (1080 - m.dataEmptyTitle.height) / 2
            ]

            m.subscriptionAndBrowseBtn.imgSrc = { id: "subscribe", label: "Browse", unFocusImg: "pkg://images/LoginButton.png", focusImg: "pkg://images/creatorDetailBtnFocus.png", width: 269, height: 76 }
            m.subscriptionAndBrowseBtn.translation = [centerAreaX + (centerAreaWidth - m.subscriptionAndBrowseBtn.boundingRect().width) / 2,
            (1080 - m.subscriptionAndBrowseBtn.height + 200) / 2]
            m.subscriptionAndBrowseBtn.visible = true
        end if
        feedListContent = createObject("RoSGNode", "ContentNode")

        feedListData = []
        for each section in myListData
            for each Item in section.content
                itemNode = CreateObject("roSGNode", "feedListItemDataModel")
                itemNode.row_type = "mylist"
                itemNode.callFunc("parseData", Item)
                feedListData.push(itemNode)
            end for
        end for
        feedListContent.appendChildren(feedListData)
        m.MarkUpList.content = feedListContent
        m.MarkUpList.visible = true
        m.MarkUpList_Title.text = "My List"

        if isValid(m.rowListIndex) and m.lastSelectedMenuItem = 1
            applyFocus(m.subscriptionAndBrowseBtn, true, "onNavigateTo() - AccountController.brs")
        else if isValid(m.rowListIndex)
            m.MarkUpList.jumpToItem = m.rowListIndex
            applyFocus(m.MarkUpList, true, "onNavigateTo() - AccountController.brs")
        else
            applyFocus(m.AccountMenuList, true, "onNavigateTo() - AccountController.brs")
        end if
    end if
end function


function onMarkUpListSelected(event as object)
    selectedIndex = event.getData()
    m.selectedGridId = selectedIndex
    selectedItem = m.MarkUpList.content.getChild(selectedIndex)

    if Lcase(selectedItem.content_type) = "episode"
        m.scene.accountItemSelected = selectedIndex
        pageInfo = createPageInfo(m.constants.CONTROLLERS.VIDEO, { rowItem_Slug: selectedItem.series_slug, isResume: true, content_Id: selectedItem.id, content_Type: selectedItem.content_Type, content: invalid, currentIndex: selectedIndex })
        m.scene.loadController = pageInfo
    else if selectedItem.row_type = "mylist"
        pageInfo = createPageInfo(m.constants.CONTROLLERS.DETAIL, { rowItem_Slug: selectedItem.slug, selected_rowItem_Index: selectedIndex })
        m.scene.loadController = pageInfo
    else
        m.scene.accountItemSelected = selectedIndex
        pageInfo = createPageInfo(m.constants.CONTROLLERS.VIDEO, { rowItem_Slug: selectedItem.slug, isResume: true, content_Id: invalid, content_Type: selectedItem.content_Type, content: invalid, currentIndex: 0 })
        m.scene.loadController = pageInfo
    end if

end function


function onMarkUpListFocused(event as object)
    ' ?"will do later"
end function


function onleftNavFocusEscape(event as object) as void
    escapeStatus = event.getData()
    if escapeStatus = true
        OnKeyEvent(m.constants.remote_keys.RIGHT, true)
    end if
end function

function onbackNavFocusEscape(event as object) as void
    escapeStatus = event.getData()
    if escapeStatus = true
        OnKeyEvent(m.constants.remote_keys.BACK, true)
    end if
end function

function OnKeyEvent(key as string, press as boolean) as boolean
    handled = false
    if (press)
        if (key = m.constants.REMOTE_KEYS.BACK)
            if m.MarkUpList.hasFocus()
                applyFocus(m.AccountMenuList, true, "Back from MarkUpList - AccountController.brs")
                handled = true
            else if m.AccountMenuList.hasFocus()
                applyFocus(m.leftMenu, true, "Back from AccountMenuList - AccountController.brs")
                handled = true
            else if m.account_setting.isInFocusChain() or m.subscriptionAndBrowseBtn.hasFocus() or m.switchProfile.hasFocus()
                applyFocus(m.AccountMenuList, true, "Back from settings - AccountController.brs")
                m.AccountMenuList.jumpToItem = m.lastSelectedMenuItem
                handled = true
            end if

        else if key = m.constants.REMOTE_KEYS.LEFT
            if m.MarkUpList.hasFocus()
                applyFocus(m.AccountMenuList, true, "Left from MarkUpList - AccountController.brs")
                m.AccountMenuList.jumpToItem = m.lastSelectedMenuItem
                handled = true
            else if m.account_setting.isInFocusChain() or m.subscriptionAndBrowseBtn.hasFocus() or m.switchProfile.hasFocus()
                applyFocus(m.AccountMenuList, true, "Left from settings - AccountController.brs")
                m.AccountMenuList.jumpToItem = m.lastSelectedMenuItem
                handled = true
            end if

        else if key = m.constants.REMOTE_KEYS.UP
               handled = true

        else if key = m.constants.REMOTE_KEYS.RIGHT
            if m.AccountMenuList.hasFocus()
                if m.MarkUpList.visible and m.MarkUpList.content <> invalid and m.MarkUpList.content.getChildCount() > 0
                    applyFocus(m.MarkUpList, true, "Right to MarkUpList - AccountController.brs")
                    handled = true
                else if m.account_setting.visible
                    ' Focus the first focusable element in account settings
                    if m.switchProfile.visible
                        applyFocus(m.switchProfile, true, "Right to switchProfile - AccountController.brs")
                        handled = true
                    end if
                else if m.subscription_setting.visible
                    ' Focus subscription button when subscription details are shown
                    if m.subscriptionAndBrowseBtn.visible
                        applyFocus(m.subscriptionAndBrowseBtn, true, "Right to subscriptionAndBrowseBtn - AccountController.brs")
                        handled = true
                    end if
                else
                    ' This handles Continue Watching and My List empty states
                    if m.subscriptionAndBrowseBtn.visible
                        applyFocus(m.subscriptionAndBrowseBtn, true, "Right to subscriptionAndBrowseBtn (empty state) - AccountController.brs")
                        handled = true
                    end if
                end if
            end if
        end if
    end if
    return handled
end function


sub switchProfilesFun()
    m.scene.loadController = { page: m.constants.CONTROLLERS.ALLPROFILESHOW, params: {} }
end sub

sub subscriptionAndBrowseBtnFun()
    if m.subscriptionAndBrowseBtn.imgSrc.label = "Browse"
        m.scene.loadController = { page: m.constants.CONTROLLERS.HOME, params: {} }
    else if m.subscriptionAndBrowseBtn.imgSrc.label = "Subscribe Now"
        m.scene.loadController = { page: m.constants.CONTROLLERS.SUBSCRIPTIONPLAN, params: {} }
    end if
end sub