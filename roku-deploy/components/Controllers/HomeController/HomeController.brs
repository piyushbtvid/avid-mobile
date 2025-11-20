function init()

    m.top.id = "HomeController"

    m.constants = GetConstants()
    m.scene = getScene()
    m.http = httpClient()
    m.apiProvider = APIProvider(m.constants)

    m.feedList = m.top.findNode("feedList")
    m.feedList.observeField("rowItemSelected", "onFeedListItemSelected")
    m.feedList.observeField("rowItemFocused", "onFeedListItemFocused")
    m.genreList = m.top.findNode("genreList")
    m.genreList.observeField("rowItemFocused", "onGenreListFocus")
    m.genreList.observeField("rowItemSelected", "onGenreListSelected")
    m.sliderTiles = m.top.findNode("sliderTiles")
    m.sliderTiles.observeFieldScoped("sliderDownEscape", "onSliderListDownEscape")

    m.feedList.rowLabelFont.uri = "pkg:/images/font/Montserrat-Medium.ttf"
    m.feedList.rowLabelFont.size = 30

    m.leftMenu = m.scene.findNode("leftMenu")
    m.leftMenu.visible = true
    m.leftMenu.itemSelected = 1

    m.scene.observeFieldScoped("leftNavEscape", "onLeftNavFocusEscape")

    m.TopMenu = m.scene.findNode("LiveTopMenu")
    m.TopMenu.visible = false

end function

function onNavigateTo(param as object)
    pageState = param.controllerState
    m.TopMenu.visible = false

    if isValid(pageState)
        m.rowListIndex = pageState.rowlistindex
        m.genreIndex = pageState.genreIndex
        m.feedList.translation = [140, 820]
    end if
    loadContent()
end function

function onNavigateAway()
    m.top.controllerState = { rowListIndex: m.rowListSelectedIndex, genreIndex: m.genreSelectedIndex, sectionID: m.sectionID }

    if isValid(m.navigationTimer)
        m.navigationTimer.unobserveField("fire")
        m.navigationTimer.control = "stop"
        m.top.removeChild(m.navigationTimer)
        m.navigationTimer = invalid
    end if
end function

function loadContent()
    startSpinner(m.top)
    login_Token = LocalStorage().getValueForKey("login_Token")
    homePageContent = TVService(m.apiProvider).getContent(login_Token)
    homePageContent.httpResponse.observeField("response", "onGetContentResponse")
    m.http.sendRequest(homePageContent)
end function

function onGetContentResponse(event)
    stopSpinner()

    response = event.getData()

    if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
        contentResponse = response.data.data.data
        onGetGenreTitleResponse(contentResponse)

        feedListContent = createObject("RoSGNode", "ContentNode")
        sliderInitialized = false

        for each item in contentResponse.sections
            if not isValid(item.content) or item.content.Count() = 0
                ' Skip empty sections
            else
                ' Check if this row should be created (not single item when skipFirstItem is true)
                shouldCreateRow = true

                if not sliderInitialized and item.content.Count() = 1
                    ' Don't create row if this is the slider row and has only one item
                    shouldCreateRow = false
                end if

                if shouldCreateRow
                    feedListRow = feedListContent.createChild("feedListContentViewModel")
                    feedListRow.title = item.title
                    feedListRow.row_type = item.id

                    if not sliderInitialized
                        feedListRow.row_type = item.content[0].content_type
                        feedListRow.update(item.content[0], true)
                        setSliderContent(feedListRow)
                        m.sliderTiles.itemContent = feedListRow
                        sliderInitialized = true

                        feedListData = addItemNodeToFeedlist(item, true)
                        if feedListData.Count() > 0
                            feedListRow.appendChildren(feedListData)
                        end if
                    else
                        feedListData = addItemNodeToFeedlist(item, false)
                        if feedListData.Count() > 0
                            feedListRow.appendChildren(feedListData)
                        end if
                    end if
                else if not sliderInitialized
                    ' Handle the case where slider row has only one item
                    feedListRow = createObject("RoSGNode", "feedListContentViewModel")
                    feedListRow.row_type = item.content[0].content_type
                    feedListRow.update(item.content[0], true)
                    setSliderContent(feedListRow)
                    m.sliderTiles.itemContent = feedListRow
                    sliderInitialized = true
                end if
            end if
        end for

        m.feedList.content = feedListContent
    end if

    if isValid(m.rowListIndex)
        m.feedList.jumpToRowItem = m.rowListIndex
        applyFocus(m.feedList, true, "onNavigateTo() - HomeController.brs")
    else if isValid(m.genreIndex)
        m.genreList.jumpToRowItem = m.genreIndex
        applyFocus(m.genreList, true, "onNavigateTo() - HomeController.brs")
    else
        applyFocus(m.sliderTiles, true, "onNavigateTo() - HomeController.brs")
    end if
end function

function addItemNodeToFeedlist(item as object, skipFirstItem = false as boolean) as object
    feedListData = []
    rowType = item.id
    itemCount = item.content.Count()

    ' If skipping first item and there's only one item, return empty array
    if skipFirstItem and itemCount <= 1
        return feedListData
    end if

    for i = 0 to itemCount - 1
        contentItem = item.content[i]

        if skipFirstItem and i = 0
            continue for
        end if

        if shouldIncludeLiveItem(rowType, contentItem)
            itemNode = CreateObject("roSGNode", "feedListItemDataModel")
            itemNode.row_type = rowType
            itemNode.callFunc("parseData", contentItem)
            feedListData.push(itemNode)
        end if
    end for

    return feedListData
end function


function shouldIncludeLiveItem(rowType as string, contentItem as object) as boolean
    if Lcase(rowType) = "continue-watching" and Lcase(contentItem.content_type) = "live channel"
        return false ' skipping live channels content in continue-watching row
    end if
    return true
end function


function onGetGenreTitleResponse(data)
    if isValid(data)
        contentResponse = data.genres
        feedListContent = createObject("RoSGNode", "ContentNode")
        row = feedListContent.createChild("ContentNode")

        for each item in contentResponse
            itemNode = row.createChild("navigationViewModel")
            itemNode.addField("FHDItemWidth", "float", false)
            itemNode.title = item.name
            itemNode.type = item.slug
            itemNode.sectionId = item.id.toStr()

            textLength = item.name.len()
            if textLength <= 4
                baseWidth = 200
            else if textLength <= 6
                baseWidth = 230
            else if textLength <= 8
                baseWidth = 260
            else if textLength <= 10
                baseWidth = 320
            else
                baseWidth = 369
            end if

            itemNode.FHDItemWidth = baseWidth
        end for
        m.genreList.content = feedListContent
        m.genreList.visible = true
    end if
end function

function onGenreListFocus()
    m.feedList.translation = [140, 820]
    m.feedList.visible = true
    m.genreList.visible = true
    m.sliderTiles.visible = true
end function

sub onGenreListSelected(event)
    selectedIndex = event.getData()
    m.genreSelectedIndex = selectedIndex
    row = m.genreList.content.getChild(selectedIndex[0])
    rowItem = row.getChild(selectedIndex[1])
    pageInfo = createPageInfo(m.constants.CONTROLLERS.GENRELIST, { sectionId: rowItem.sectionId, categoryTitle: rowItem.title })
    m.scene.loadController = pageInfo
end sub

function onFeedListItemSelected(event as object)
    selectedIndex = event.getData()
    rowIndex = selectedIndex[0]
    rowItemIndex = selectedIndex[1]

    row = m.feedList.content.getChild(selectedIndex[0])
    rowItem = row.getChild(selectedIndex[1])
    m.rowListSelectedIndex = selectedIndex
    selectedRow = m.feedList.content.getChild(rowIndex)

    row_type = rowItem.row_type

    if row_type = "continue-watching"
        if Lcase(rowItem.content_type) = "episode"
            pageInfo = createPageInfo(m.constants.CONTROLLERS.VIDEO, { rowItem_Slug: rowItem.series_slug, isResume: true, content_Id: rowItem.id, content_Type: rowItem.content_Type, content: invalid, currentIndex: rowItemIndex })
        else
            pageInfo = createPageInfo(m.constants.CONTROLLERS.VIDEO, { rowItem_Slug: rowItem.slug, isResume: true, content_Id: invalid, content_Type: rowItem.content_Type, content: invalid, currentIndex: 0 })
        end if
    else if rowitem.content_type = "Live Channel"
        pageInfo = createPageInfo(m.constants.CONTROLLERS.VIDEO, { rowItem_Slug: rowItem.slug, isResume: true, content_Id: invalid, content_Type: rowItem.content_Type, content: invalid, currentIndex: 0 })
    else
        pageInfo = createPageInfo(m.constants.CONTROLLERS.DETAIL, { rowItem_Slug: rowItem.slug, selected_rowItem_Index: rowItemIndex, content_Type: rowItem.content_Type })
    end if

    m.scene.loadController = pageInfo
end function

function onFeedListItemFocused(event as object)

    focusedIndex = event.getData()
    feedList = event.getRoSGNode()
    row = feedList.content.getChild(focusedIndex[0])

    m.sliderTiles.visible = false
    m.genreList.visible = false
    m.feedList.translation = [146, 50]

end function

function onLeftNavFocusEscape(event as object) as void
    escapeStatus = event.getData()
    if escapeStatus = true
        OnKeyEvent(m.constants.remote_keys.RIGHT, true)
        m.feedList.translation = [140, 820]
        m.feedList.visible = true
        m.genreList.visible = true
        m.sliderTiles.visible = true
    end if
end function

function OnKeyEvent(key as string, press as boolean) as boolean

    handled = false
    focusMap = createFocusMap()
    currentFocusMap = getCurrentFocusItem(m.scene)
    if (press)
        if key = m.constants.remote_keys.BACK
            if m.leftMenu.isExpanded = false
                m.leftMenu.isExpanded = true
                applyFocus(m.leftMenu, true, "onNavigateTo() - HomeController.brs")
                handled = true
            end if
            handled = true
        else if key = m.constants.remote_keys.RIGHT
            if m.feedList.hasFocus()
                handled = true
            else
                handled = componentFocusHandler(key, focusMap, currentFocusMap)
            end if
        else if key = m.constants.remote_keys.DOWN
            if m.genreList.hasFocus()
                applyFocus(m.feedList, true, "onNavigateTo() - HomeController.brs")
                m.feedList.jumpToRowItem = [0, 0]
            else if m.feedList.hasFocus()
            else
                applyFocus(m.genreList, true, "onNavigateTo() - HomeController.brs")
            end if
            handled = true
        else
            handled = componentFocusHandler(key, focusMap, currentFocusMap)
        end if

    end if
    return handled
end function

function createFocusMap()
    focusMap = {}
    focusMap[m.leftMenu.id] = { up: invalid, down: invalid, Left: invalid, Right: m.sliderTiles.id }
    focusMap[m.feedList.id] = { up: m.genreList.id, down: invalid, Left: m.leftMenu.id, Right: invalid }
    focusMap[m.sliderTiles.id] = { up: invalid, down: m.genreList.id, Left: m.leftMenu.id, Right: invalid }
    focusMap[m.genreList.id] = { up: m.sliderTiles.id, down: m.feedList.id, Left: m.leftMenu.id, Right: invalid }
    return focusMap
end function


