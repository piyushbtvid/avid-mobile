function init()

    m.top.id = "GenreListController"

    m.constants = GetConstants()
    m.scene = getScene()
    m.http = httpClient()
    m.apiProvider = APIProvider(m.constants)

    m.categoryTitle = m.top.findNode("categoryTitle")

    m.GenreGridList = m.top.findNode("GenreGridList")
    m.GenreGridList.observeField("ItemSelected", "onGenreGridListSelected")
    m.GenreGridList.observeField("ItemFocused", "onGenreGridListFocused")

    m.leftMenu = m.scene.findNode("leftMenu")
    m.leftMenu.visible = false

    m.TopMenu = m.scene.findNode("LiveTopMenu")
    m.TopMenu.visible = false

end function



function onNavigateTo(param as object)
    m.TopMenu.visible = false
    m.categoryTile = param.categorytitle
    pageState = param.controllerState
    if isValid(param.sectionid)
        fetchCategoryById(param.sectionid)
    else if isValid(pageState)
        m.rowListIndex = pageState.rowListIndex
    end if

    if isValid(pageState)
        m.rowListIndex = pageState.rowListIndex
    end if

end function

function onNavigateAway()
    m.top.controllerState = { rowListIndex: m.selectedGridId }
end function

sub fetchCategoryById(sectionID)
    categoryPageContent = TVService(m.apiProvider).getGenreByID(sectionID)
    categoryPageContent.httpResponse.observeField("response", "onGetGenreResponse")
    m.http.sendRequest(categoryPageContent)
end sub

sub onGetGenreResponse(event)

    response = event.getData()


    if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data)
        contentResponse = response.data.data.data
        feedListContent = createObject("RoSGNode", "ContentNode")
        feedListContent.addFields({ row_type: "movies" })
        m.categoryTitle.text = m.categoryTile

        feedListData = []
        for each cateItem in contentResponse
            itemNode = CreateObject("roSGNode", "feedListItemDataModel")
            itemNode.callFunc("parseData", cateItem)
            feedListData.push(itemNode)

        end for
        feedListContent.appendChildren(feedListData)
        m.GenreGridList.content = feedListContent
        m.GenreGridList.visible = true
    end if

    if isValid(m.rowListIndex)
        m.GenreGridList.jumpToItem = m.rowListIndex
        applyFocus(m.GenreGridList, true, "onNavigateTo() - MoviesController.brs")
    else
        applyFocus(m.GenreGridList, true, "onNavigateTo() - GenreListController.brs")
    end if
end sub

sub onGenreGridListSelected(event)
    selectedIndex = event.getData()
    m.selectedGridId = selectedIndex
    selectedItem = m.GenreGridList.content.getChild(selectedIndex)
    selectedGrid = m.GenreGridList.content'.getChild(selectedIndex)
    'pageInfo = createPageInfo(m.constants.CONTROLLERS.DETAIL, { rowItem_Slug: selectedItem.slug, selected_rowItem_Index: selectedIndex, content_Type: selectedItem.content_Type })

    if selectedItem.content_type = "Live Channel"
        pageInfo = createPageInfo(m.constants.CONTROLLERS.VIDEO, { rowItem_Slug: selectedItem.slug, isResume: true, content_Id: invalid, content_Type: selectedItem.content_Type, content: invalid, currentIndex: 0 })
    else
        pageInfo = createPageInfo(m.constants.CONTROLLERS.DETAIL, { rowItem_Slug: selectedItem.slug, selected_rowItem_Index: selectedIndex, content_Type: selectedItem.content_Type })
    end if
    m.scene.loadController = pageInfo
end sub


function OnKeyEvent(key as string, press as boolean) as boolean

    handled = false
    focusMap = createFocusMap()
    currentFocusMap = getCurrentFocusItem(m.scene)

    if (press)
        if key = m.constants.remote_keys.BACK
            goBackInHistory()
            handled = true
        else if key = m.constants.REMOTE_KEYS.LEFT
            handled = true
        else
            handled = componentFocusHandler(key, focusMap, currentFocusMap)
        end if

    end if
    return handled
end function


function createFocusMap()

    focusMap = {}

    focusMap[m.leftMenu.id] = { up: invalid, down: invalid, Left: invalid, Right: m.GenreGridList.id }
    focusMap[m.GenreGridList.id] = { up: invalid, down: invalid, Left: invalid, Right: invalid }

    return focusMap

end function