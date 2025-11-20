function init()

    m.top.id = "MoviesController"

    m.constants = GetConstants()
    m.scene = getScene()
    m.http = httpClient()
    m.apiProvider = APIProvider(m.constants)

    m.moviesList = m.top.findNode("moviesList")
    m.moviesLayout = m.top.findNode("moviesLayout")
    m.moviesList.observeField("ItemFocused", "onGridItemFocused")
    m.moviesList.observeField("ItemSelected", "onGridItemSelected")

    m.moviesTitle = m.top.findNode("moviesTitle")
    m.sliderTiles = m.top.findNode("sliderTiles")
    m.scene.observeFieldScoped("leftNavEscape", "onLeftNavFocusEscape")

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

    loadMovies()

end function


function onNavigateAway()

    m.top.controllerState = { rowListIndex: m.selectedGridId }

end function

function loadMovies()

    startSpinner(m.top)
    login_Token = LocalStorage().getValueForKey("login_Token")
    creatorPageContent = TVService(m.apiProvider).getMoviesContent(login_Token)
    creatorPageContent.httpResponse.observeField("response", "onMoviesPageResponse")
    m.http.sendRequest(creatorPageContent)

end function


function onMoviesPageResponse(event)

    stopSpinner()

    response = event.getData()
    feedList = createObject("RoSGNode", "ContentNode")
    feedList.addFields({ row_type: "" })

    itemArray = []
    if isValid(response) and isValid(response.data) and isValid(response.data.data) and isValid(response.data.data.data) and response.data.data.data.count() > 0 
        contentResponse = response.data.data.data
        m.moviesTitle.text = "Movies"
        feedList.row_type = contentResponse[0].content_type
        feedList.update(contentResponse[0], true)
        setSliderContent(feedList)
        m.sliderTiles.itemContent = feedList
        for i = 1 to contentResponse.count() - 1
            contentNode = createObject("RoSGNode", "feedListItemDataModel")
            contentNode.callfunc("parseData", contentResponse[i])
            itemArray.push(contentNode)
        end for
    end if
    feedList.appendChildren(itemArray)
    m.moviesList.content = feedList
    if isValid(m.rowListIndex)
        m.moviesList.jumpToItem = m.rowListIndex
        applyFocus(m.moviesList, true, "onNavigateTo() - MoviesController.brs")
    else
        applyFocus(m.sliderTiles, true, "onNavigateTo() - MoviesController.brs")
    end if
end function

function onGridItemSelected(event as object)
    selectedIndex = event.getData()
    selectedItem = m.moviesList.content.getChild(selectedIndex)
    m.selectedGridId = selectedIndex
    selectedGrid = m.moviesList.content
    pageInfo = createPageInfo(m.constants.CONTROLLERS.DETAIL, { rowItem_Slug: selectedItem.slug, selected_rowItem_Index: selectedIndex })
    m.scene.loadController = pageInfo
end function


function onGridItemFocused(event as object)
    m.moviesLayout.translation = [153, 50]
    m.sliderTiles.visible = false
end function

function onLeftNavFocusEscape(event as object) as void
    escapeStatus = event.getData()
    if escapeStatus = true
        OnKeyEvent(m.constants.remote_keys.RIGHT, true)
        m.moviesLayout.translation = [137, 740]
        'm.moviesList.jumpToItem = 0
        m.moviesList.visible = true
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
                if m.moviesList.hasFocus()
                    handled = true
                else
                    handled = componentFocusHandler(key, focusMap, currentFocusMap)
                end if
            else if key = m.constants.remote_keys.UP and m.moviesList.isInFocusChain()
                applyFocus(m.sliderTiles, true, "onNavigateTo() - MoviesController.brs")
                m.sliderTiles.visible = true
                m.moviesLayout.translation = [137, 740]
                handled = true
            else if key = m.constants.remote_keys.DOWN
                if m.sliderTiles.isInFocusChain()
                    applyFocus(m.moviesList, true, "onNavigateTo() - MoviesController.brs")
                    m.moviesList.jumpToItem = 0
                    handled = true
                end if

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
    focusMap[m.sliderTiles.id] = { up: invalid, down: m.moviesList.id, Left: invalid: Right: invalid }
    focusMap[m.moviesList.id] = { up: m.sliderTiles.id, down: invalid, Left: invalid: Right: invalid }

    return focusMap

end function

