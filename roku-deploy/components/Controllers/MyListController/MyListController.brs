function init()

    m.top.id = "MyListController"

    m.constants = GetConstants()
    m.scene = getScene()
    m.http = httpClient()
    m.apiProvider = APIProvider(m.constants)

    m.myList = m.top.findNode("myList")
    m.myList.observeField("rowItemFocused", "onGridItemFocused")
    m.myList.observeField("rowItemSelected", "onGridItemSelected")

    m.scene.observeFieldScoped("leftNavEscape", "onLeftNavFocusEscape")

    m.sliderTiles = m.top.findNode("sliderTiles")
    m.dataEmptyTitle = m.top.findNode("dataEmptyTitle")

    m.TopMenu = m.scene.findNode("LiveTopMenu")
    m.TopMenu.visible = false
    m.leftMenu = m.scene.findNode("leftMenu")
    m.leftMenu.visible = true

end function

function onNavigateTo(param as object)
    m.TopMenu.visible = false
    pageState = param.controllerState
    if isValid(pageState)
        m.rowListIndex = pageState.rowListIndex
    end if
    loadMyList()
end function


function onNavigateAway()
    m.top.controllerState = { rowListIndex: m.rowListSelectedIndex }
end function

function loadMyList()
    startSpinner(m.top)
    login_Token = LocalStorage().getValueForKey("login_Token")
    creatorPageContent = TVService(m.apiProvider).getMyListData(login_Token)
    creatorPageContent.httpResponse.observeField("response", "onMyListPageResponse")
    m.http.sendRequest(creatorPageContent)
end function

function onMyListPageResponse(event as object) as void
    stopSpinner()
    response = event.getData()

    m.dataEmptyTitle.visible = false

    if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
        contentResponse = response.data.data.data
        if contentResponse = invalid or contentResponse.count() = 0
            m.dataEmptyTitle.visible = true
        end if
        feedListContent = createObject("RoSGNode", "ContentNode")
        sliderInitialized = false
        isFirstSection = true

        for each section in contentResponse

            feedListRow = createObject("RoSGNode", "ContentNode")
            feedListRow.title = section.title
            feedListRow.addFields({ row_type: section.id })

            if not sliderInitialized and isFirstSection and section.content.count() > 0
                sliderItem = createObject("RoSGNode", "feedListContentViewModel")
                if sliderItem <> invalid
                    sliderItem.row_type = section.content[0].content_type
                    sliderItem.update(section.content[0], true)
                    m.sliderTiles.itemContent = sliderItem
                    m.sliderTiles.visible = true
                    sliderInitialized = true
                end if

                for i = 1 to section.content.count() - 1
                    itemNode = CreateObject("roSGNode", "feedListItemDataModel")
                    if itemNode <> invalid
                        itemNode.callFunc("parseData", section.content[i])
                        feedListRow.appendChild(itemNode)
                    end if
                end for
            else
                for each item in section.content
                    itemNode = CreateObject("roSGNode", "feedListItemDataModel")
                    if itemNode <> invalid
                        itemNode.callFunc("parseData", item)
                        itemNode.content_type = item.contentType
                        feedListRow.appendChild(itemNode)
                    end if
                end for
            end if

            if feedListRow.getChildCount() > 0
                feedListContent.appendChild(feedListRow)
            else
                ?"[MyListController] Skipping empty row: "; section.title
            end if

            isFirstSection = false
            m.myList.content = feedListContent
        end for

        if feedListContent.getChildCount() > 0


            if isValid(m.rowListIndex)
                m.myList.jumpToRowItem = m.rowListIndex
                applyFocus(m.myList, true, "onMyListPageResponse() - MyListController.brs")
            else if m.sliderTiles.visible
                applyFocus(m.sliderTiles, true, "onMyListPageResponse() - MyListController.brs")
            else
                applyFocus(m.sliderTiles, true, "onMyListPageResponse() - MyListController.brs")
            end if
        else
            ?"[MyListController] No valid rows to display"
            applyFocus(m.sliderTiles, true, "onMyListPageResponse() - MyListController.brs")
        end if
    end if
end function

function onGridItemSelected(event as object)
    selectedIndex = event.getData()
    rowIndex = selectedIndex[0]
    rowItemIndex = selectedIndex[1]

    row = m.myList.content.getChild(selectedIndex[0])
    rowItem = row.getChild(selectedIndex[1])
    m.rowListSelectedIndex = selectedIndex
    selectedRow = m.myList.content.getChild(rowIndex)

    pageInfo = createPageInfo(m.constants.CONTROLLERS.DETAIL, { rowItem_Slug: rowItem.slug, selected_rowItem_Index: rowItemIndex, content_Type: rowItem.content_Type, continueWatch : false  })
    m.scene.loadController = pageInfo
end function


function onGridItemFocused(event as object)
    m.myList.translation = [146, 50]
    m.sliderTiles.visible = false
end function

function onLeftNavFocusEscape(event as object) as void
    escapeStatus = event.getData()
    if escapeStatus = true
        OnKeyEvent(m.constants.remote_keys.RIGHT, true)
        m.myList.jumpToItem = 0
        m.myList.translation = [137, 740]
        m.myList.visible = true
        m.sliderTiles.visible = true
    end if
end function

function OnKeyEvent(key as string, press as boolean) as boolean

    handled = false
    focusMap = createFocusMap()
    currentFocusMap = getCurrentFocusItem(m.scene)

    if (press)

        if not handled
            if key = m.constants.REMOTE_KEYS.BACK
                if m.leftMenu.isExpanded = false
                    m.leftMenu.isExpanded = true
                    applyFocus(m.leftMenu, true, "onNavigateTo() - HomeController.brs")
                    handled = true
                end if
                handled = true
            else if key = m.constants.remote_keys.RIGHT
                if m.myList.hasFocus()
                    handled = true
                else
                    handled = componentFocusHandler(key, focusMap, currentFocusMap)
                end if
            else if key = m.constants.remote_keys.UP and m.myList.isInFocusChain()
                applyFocus(m.sliderTiles, true, "onNavigateTo() - MyListController.brs")
                m.sliderTiles.visible = true
                m.myList.translation = [137, 740]

                handled = true
            else if key = m.constants.remote_keys.DOWN
                applyFocus(m.myList, true, "onNavigateTo() - MyListController.brs")
                handled = true
            else
                handled = componentFocusHandler(key, focusMap, currentFocusMap)
            end if
        end if

    end if
    return handled
end function


function createFocusMap()
    focusMap = {}
    focusMap[m.leftMenu.id] = { up: invalid, down: invalid, Left: invalid, Right: m.sliderTiles.id }
    focusMap[m.sliderTiles.id] = { up: invalid, down: m.myList.id, Left: invalid: Right: invalid }
    focusMap[m.myList.id] = { up: m.sliderTiles.id, down: invalid, Left: invalid: Right: invalid }
    return focusMap
end function