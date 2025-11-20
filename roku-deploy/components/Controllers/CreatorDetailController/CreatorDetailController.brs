function init()

    m.top.id = "CreatorDetailController"

    m.scene = getScene()
    m.constants = GetConstants()
    m.http = httpClient()
    m.apiProvider = APIProvider(m.constants)

    m.creatorContentList = m.top.findNode("creatorContentList")
    m.creatorContentList.observeField("ItemSelected", "onCreatorContentListSelected")

    ' m.addButton = m.top.findNode("addButton")
    ' m.thumbUpButton = m.top.findNode("thumbUpButton")
    ' m.thumbDownButton = m.top.findNode("thumbDownButton")

    m.subscribeButton = m.top.findNode("subscribeButton")
    m.accessButton = m.top.findNode("accessButton")

    m.scaledElementsMac = m.top.findNode("scaledElementsMac")
    m.micButton = m.top.findNode("micButton")
    m.searchButton = m.top.findNode("searchButton")
    m.searchButton.observeField("buttonSelected", "onSearchButtonSelected")

    m.micButton.imgSrc = { label: "", unFocusImg: "pkg://images/macIcon.png", focusImg: "pkg://images/focusMacIcon.png", width: 86, height: 86, focusedWidth: 126, focusedHeight: 126 }
    m.searchButton.imgSrc = { label: "", unFocusImg: "pkg://images/searchIcon.png", focusImg: "pkg://images/FocusSearchIcon.png", width: 86, height: 86, focusedWidth: 126, focusedHeight: 126 }

    m.channel_category = m.top.findNode("channel_category")
    m.channel_subscribers = m.top.findNode("channel_subscribers")

    m.creator_poster = m.top.findNode("creator_poster")
    m.creatorPic = m.top.findNode("creatorPic")
    m.creatorName = m.top.findNode("creatorName")
    m.channel_description = m.top.findNode("channel_description")
    m.channel_name = m.top.findNode("channel_name")

    m.channel_category.width = 0
    m.channel_subscribers.width = 0

    m.subscribeButton.imgSrc = { id: "subscribeicon", label: "Subscribe", unFocusImg: "pkg://images/creatorButton.png", focusImg: "pkg://images/creatorDetailBtnFocus.png", width: 269, height: 76 }
    m.accessButton.imgSrc = { id: "access_premium", label: "Access Premium", unFocusImg: "pkg://images/creatorButton.png", focusImg: "pkg://images/creatorDetailBtnFocus.png", width: 340, height: 80 }

    ' m.addButton.imgSrc = { label: "", unFocusImg: "pkg://images/add.png", focusImg: "pkg://images/focusAddIcon.png", width: 65, height: 65 }
    ' m.thumbUpButton.imgSrc = { label: "", unFocusImg: "pkg://images/thumb-up.png", focusImg: "pkg://images/focusThumbUpIcon.png", width: 65, height: 65 }
    ' m.thumbDownButton.imgSrc = { label: "", unFocusImg: "pkg://images/thumbs-down.png", focusImg: "pkg://images/focus-thumb-down.png", width: 65, height: 65 }

    m.leftmenu = m.scene.findNode("leftMenu")
    m.leftmenu.visible = false

    m.creatorPicMaskGroup = m.top.findNode("creatorPicMaskGroup")
    m.creatorPicMaskGroup.translation = "[88, 361]"
    if getDisplayMode() = "FHD"
        m.creatorPicMaskGroup.maskUri = "pkg://images/creatorCircle.png"
        m.creatorPicMaskGroup.maskSize = "[171,171]"
    else if getDisplayMode() = "HD"
        m.creatorPicMaskGroup.maskUri = "pkg://images/menuCollapseFocus.png"
        m.creatorPicMaskGroup.maskSize = "[110,110]"
    end if


end function

function onNavigateTo(param as object)
    m.login_Token = LocalStorage().getValueForKey("login_Token")
    pageState = param.controllerState

    if isValid(param.creatorindex)
        m.creatorindex = param.creatorindex
        loadContent(param.creatorindex)
    end if
    if isValid(pageState) and isvalid(pageState.rowListIndex)
        m.rowListIndex = pageState.rowListIndex
    end if
end function

function onNavigateAway()
    m.top.controllerState = { rowListIndex: m.selectedItemId, }
end function

function loadContent(creator_id)
    startSpinner(m.top)
    creatorDetailsContent = TVService(m.apiProvider).getCreatorsDetailsData(creator_id, m.login_Token)
    creatorDetailsContent.httpResponse.observeField("response", "onGetContentResponse")
    m.http.sendRequest(creatorDetailsContent)
end function

sub onGetContentResponse(event)
    response = event.getData()
    if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
        creatorData = response.data.data.data
        m.creator_poster.uri = creatorData.channel_banner
        m.creatorPic.uri = creatorData.profile_img
        m.creatorName.text = creatorData.name
        m.channel_description.text = creatorData.bio
        if creatorData.channel_name <> invalid
            m.channel_name.text = "Channel Name :  " + creatorData.channel_name
        end if

        m.channel_category.text = creatorData.channel_category '"Reality" + " • " + "Comedy"
        m.channel_subscribers.text = creatorData.channel_subscribers + " Subscribers"

        m.channel_category.width = m.channel_category.boundingRect().width
        m.channel_subscribers.width = m.channel_subscribers.boundingRect().width
    end if

    loadContentList(m.creatorindex)
end sub

function loadContentList(creator_id)
    creatorListContent = TVService(m.apiProvider).getCreatorsContentListData(creator_id, m.login_Token)
    creatorListContent.httpResponse.observeField("response", "getAllPlayListDataUi")
    m.http.sendRequest(creatorListContent)
end function

sub getAllPlayListDataUi(event)
    response = event.getData()
    startSpinner(m.top)

    if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
        contentResponse = response.data.data.data
        feedList = createObject("RoSGNode", "ContentNode")
        itemArray = []
        for each items in contentResponse
            contentNode = createObject("RoSGNode", "feedListItemDataModel")
            contentNode.callfunc("parseData", items)
            itemArray.push(contentNode)
        end for
        feedList.appendChildren(itemArray)
        m.creatorContentList.content = feedList
        m.creatorContentList.visible = true
        'applyFocus(m.creatorContentList, true, "onNavigateTo() - CreatorDetailController.brs")

        if isValid(m.rowListIndex)
            m.creatorContentList.jumpToItem = m.rowListIndex
            applyFocus(m.creatorContentList, true, "onNavigateTo() - CreatorDetailsController.brs")
        else
            applyFocus(m.subscribeButton, true, "onNavigateTo() - CreatorDetailsController.brs")
        end if

    end if
    stopSpinner()
end sub

function onCreatorContentListSelected(event as object)

    selectedIndex = event.getData()

    selectedItem = m.creatorContentList.content.getChild(selectedIndex)
    selected_Grid = m.creatorContentList.content
    m.selectedItemId = selectedIndex

    'pageInfo = createPageInfo(m.constants.CONTROLLERS.DETAIL, { rowItem_Slug: selectedItem.slug, selected_rowItem_Index: selectedIndex })

    if selectedItem.content_type = "Live Channel"
        pageInfo = createPageInfo(m.constants.CONTROLLERS.VIDEO, { rowItem_Slug: selectedItem.slug, isResume: true, content_Id: invalid, content_Type: selectedItem.content_Type, content: invalid, currentIndex: 0 })
    else
        pageInfo = createPageInfo(m.constants.CONTROLLERS.DETAIL, { rowItem_Slug: selectedItem.slug, selected_rowItem_Index: selectedIndex, content_Type: selectedItem.content_Type })
    end if
    m.scene.loadController = pageInfo

end function

function OnKeyEvent(key as string, press as boolean) as boolean
    handled = false
    focusMap = createFocusMap()
    currentFocusComp = getCurrentFocusItem(m.scene)

    if (press)

        if (not handled)
            if (key = m.constants.REMOTE_KEYS.BACK)
                goBackInHistory(m.scene)
                m.leftmenu.visible = true
            end if
        end if
        handled = true
    else
        handled = componentFocusHandler(key, focusMap, currentFocusComp)
    end if

    return handled

end function


function createFocusMap()

    focusMap = {}

    focusMap[m.creatorContentList.id] = { up: invalid, down: invalid, left: m.subscribeButton.id, right: invalid }
    focusMap[m.subscribeButton.id] = { up: m.micButton.id, down: invalid, left: invalid, right: m.accessButton.id }
    focusMap[m.accessButton.id] = { up: m.micButton.id, down: invalid, left: m.subscribeButton.id, right: m.creatorContentList.id }
    ' focusMap[m.addButton.id] = { up: m.subscribeButton.id, down: invalid, left: invalid, right: m.thumbUpButton.id }
    ' focusMap[m.thumbUpButton.id] = { up: m.subscribeButton.id, down: invalid, left: m.addButton.id, right: m.thumbDownButton.id }
    ' focusMap[m.thumbDownButton.id] = { up: m.subscribeButton.id, down: invalid, left: m.thumbUpButton.id, right: m.creatorContentList.id }
    focusMap[m.micButton.id] = { up: invalid, down: m.subscribeButton.id, left: m.subscribeButton.id, right: m.searchButton.id }
    focusMap[m.searchButton.id] = { up: invalid, down: m.subscribeButton.id, left: m.micButton.id, right: invalid }

    return focusMap

end function

function ConvertTimestampToDate(timestamp as integer) as string
    dt = CreateObject("roDateTime")

    dt.FromSeconds(timestamp)

    formattedDate = dt.asDateStringLoc("medium")

    return formattedDate
end function

sub onSearchButtonSelected()
    pageInfo = createPageInfo(m.constants.CONTROLLERS.SEARCH, {})
    m.scene.loadController = pageInfo
end sub
